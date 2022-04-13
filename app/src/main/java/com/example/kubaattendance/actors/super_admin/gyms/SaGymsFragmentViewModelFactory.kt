package com.example.kubaattendance.actors.super_admin.gyms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.SuperAdminRepository

class SaGymsFragmentViewModelFactory(private val superAdminRepository: SuperAdminRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaGymsFragmentViewModel(superAdminRepository) as T
    }
}