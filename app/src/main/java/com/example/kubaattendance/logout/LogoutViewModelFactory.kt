package com.example.kubaattendance.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LogoutViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogoutViewModel() as T
    }
}