package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @Author: nawalalghamdi
 * @Date: 12/09/2023
 */

class DateAdapter {

    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    @FromJson
    fun dateFromJson (date: String): Date {
        return formatter.parse(date)
    }

    @ToJson
    fun dateToJson (date: Date): String {
        return formatter.format(date)
    }
}