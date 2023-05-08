package com.robby.dicodingstory.utils

import android.content.Context
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_datastore")

fun ImageView.loadImageFromUrl(url: String, options: RequestOptions = RequestOptions()) {
    Glide.with(this.context)
        .load(url)
        .apply(options)
        .into(this)
}

fun String.withDateFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    val parsedInput = inputFormat.parse(this) as Date
    val parsedOutput = outputFormat.format(parsedInput)
    val date = outputFormat.parse(parsedOutput) as Date

    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}