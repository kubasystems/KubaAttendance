package com.example.kubaattendance.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.AuthRepository

class AuthViewModelFactory(private val userAuthRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(userAuthRepository) as T
    }

}