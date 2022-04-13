package com.example.kubaattendance.actors.super_admin.gym_owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.SuperAdminRepository

class SaGymOwnersFragmentViewModelFactory(private val superAdminRepository: SuperAdminRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaGymOwnersFragmentViewModel(superAdminRepository) as T
    }
}