package com.example.android.politicalpreparedness.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

/**
 * @Author: nawalalghamdi
 * @Date: 18/09/2023
 */

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    //This binding adapter is to initialize the ListAdapter with list data
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}
