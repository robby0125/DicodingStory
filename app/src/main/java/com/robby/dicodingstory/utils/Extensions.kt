package com.robby.dicodingstory.utils

import android.content.Context
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImageFromUrl(url: String, options: RequestOptions = RequestOptions()) {
    Glide.with(this.context)
        .load(url)
        .apply(options)
        .into(this)
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_datastore")