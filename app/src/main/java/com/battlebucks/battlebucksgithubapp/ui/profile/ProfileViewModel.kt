package com.battlebucks.battlebucksgithubapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubRepoDto
import com.battlebucks.battlebucksgithubapp.data.network.model.response.GithubUserDto
import com.battlebucks.battlebucksgithubapp.data.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface ProfileUiState {
    data object Idle : ProfileUiState
    data object Loading : ProfileUiState
    data class Success(val user: GithubUserDto) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: GithubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    var reposPaging: MutableStateFlow<PagingData<GithubRepoDto>> =
        MutableStateFlow(PagingData.empty())

    fun load(username: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val user = repo.getUser(username)
                _uiState.value = ProfileUiState.Success(user)
                repo.getUserReposPaged(username)
                    .cachedIn(viewModelScope)
                    .collect { paging -> reposPaging.value = paging }
            } catch (e: HttpException) {
                _uiState.value = when (e.code()) {
                    404 -> ProfileUiState.Error("User not found")
                    403 -> ProfileUiState.Error("Rate limit exceeded. Try again later.")
                    else -> ProfileUiState.Error("Server error: ${e.code()}")
                }
            } catch (_: IOException) {
                _uiState.value = ProfileUiState.Error("Network error. Check your connection.")
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
}