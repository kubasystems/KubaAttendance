package com.example.kubaattendance.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.AuthRepository

class ProfileViewModelFactory(private val userAuthRepository : AuthRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userAuthRepository) as T
    }
}