package com.example.kubaattendance.actors.super_admin.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.SuperAdminRepository

class SaHomeFragmentViewModelFactory(private val superAdminRepository: SuperAdminRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaHomeFragmentViewModel(superAdminRepository) as T
    }
}