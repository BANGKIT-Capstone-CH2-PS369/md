package com.example.pindairasa.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pindairasa.repository.repo
import kotlinx.coroutines.launch

class MainViewModel(private val repository: repo) : ViewModel(){
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}