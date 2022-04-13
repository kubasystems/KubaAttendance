package com.example.kubaattendance.utils.custom_spinner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CustomSpinnerViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CustomSpinnerViewModel() as T
    }
}