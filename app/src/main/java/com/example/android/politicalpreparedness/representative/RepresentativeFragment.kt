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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    //TODO: Declare ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        statesArray = requireContext().resources.getStringArray(R.array.states)
        statesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            statesArray
        )
        binding.state.adapter = statesAdapter

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }

        return binding.root

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
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
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        hideKeyboard()
        // Get location from LocationServices
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val address = geoCodeLocation(location)
                    if (address != null) {
                        val stateIndex = statesArray.indexOf(address.state)
                        binding.apply {
                            addressLine1.setText(address.line1)
                            addressLine2.setText(address.line2)
                            city.setText(address.city)
                            zip.setText(address.zip)
                            state.setSelection(if (stateIndex != -1) stateIndex else 0)
                        }
                    }
                }
            }

    }

    // The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    private fun geoCodeLocation(location: Location): Address? {
        val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
        return geocoder?.getFromLocation(location.latitude, location.longitude, 1)
            ?.map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            ?.first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}