package com.battlebucks.battlebucksgithubapp.ui.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    fun onUsernameChanged(v: String) { _username.value = v }

    fun trimmedUsername() = _username.value.trim()
}