package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.UserRepository
import hr.itrojnar.instagram.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        _userState.value = UserState.Loading
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUserDetail()
                if (user != null) {
                    _userState.value = UserState.Loaded(user)
                } else {
                    _userState.value = UserState.Error
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error
            }
        }
    }

    fun clearResources() {
        // Clear or close any resources or objects you've held onto.
        // This could be stopping listeners, clearing lists, nullifying large objects, etc.
        _userState.value = UserState.Default
    }
}

sealed class UserState {
    object Loading : UserState()
    data class Loaded(val user: User) : UserState()
    object Error : UserState()
    object Default : UserState()
}