package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        // Add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var statesArray: Array<String>
    private lateinit var statesAdapter: ArrayAdapter<String>
    private lateinit var representativeListAdapter: RepresentativeListAdapter

    // Declare ViewModel
    private val _viewModel by viewModels<RepresentativeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        statesArray = requireContext().resources.getStringArray(R.array.states)
        statesAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, statesArray
        )
        binding.state.adapter = statesAdapter

        // Define and assign Representative adapter
        representativeListAdapter = RepresentativeListAdapter()
        binding.representativeRecyclerView.apply {
            setHasFixedSize(true)
            adapter = representativeListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        // Populate Representative adapter
        _viewModel.representatives.observe(viewLifecycleOwner) {
            representativeListAdapter.submitList(it)
        }

        // Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            findMyRepresentative()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            // Request Location permissions
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        // Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        hideKeyboard()
        // Get location from LocationServices
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val address = geoCodeLocation(location)
                if (address != null) {
                    _viewModel.updateAddress(address)
                    binding.state.setNewValue(address.state)
                    _viewModel.fetchRepresentative()
                }
            }
        }
    }

    // The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    private fun geoCodeLocation(location: Location): Address? {
        val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
        val addresses: MutableList<android.location.Address>?
        try {
            addresses = geocoder?.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
            Toast.makeText(
                context, getString(R.string.this_location_is_not_supported), Toast.LENGTH_LONG
            ).show()
            return null
        }

        return addresses?.map { address ->
            if (address.thoroughfare == null || address.locality == null || address.adminArea == null || address.postalCode == null) {
                Toast.makeText(
                    context, getString(R.string.this_location_is_not_supported), Toast.LENGTH_LONG
                ).show()
                null
            } else {
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
        }?.first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun findMyRepresentative() {
        if (validateEnteredData()) {
            binding.apply {
                _viewModel.updateAddress(
                    line1 = addressLine1.text.toString(),
                    line2 = addressLine2.text.toString(),
                    city = city.text.toString(),
                    state = state.selectedItem.toString(),
                    zip = zip.text.toString()
                )
            }
            _viewModel.fetchRepresentative()
        }
    }

    private fun validateEnteredData(): Boolean {
        binding.apply {
            return if (!addressLine1.text.isNullOrEmpty() && !city.text.isNullOrEmpty() && !zip.text.isNullOrEmpty()) {
                true
            } else {
                Toast.makeText(
                    this@DetailFragment.context,
                    getString(R.string.please_provide_the_address_of_representative),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

}