package com.battlebucks.battlebucksgithubapp.utils

import android.app.Service
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.battlebucks.battlebucksgithubapp.BuildConfig

fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
        this, 0
    )
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        this.windowToken, 0
    )
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}

fun View.isEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}

fun getBaseUrl(): String {
    return getURL(BuildConfig.BASEURL)
}

private fun getURL(url: String): String {
    return try {
        val data = Base64.decode(url, Base64.DEFAULT)
        String(data, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}