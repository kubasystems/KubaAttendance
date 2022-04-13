package com.example.kubaattendance.actors.super_admin.gym_owner.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.SuperAdminRepository

class CreateGymOwnerViewModelFactory(private var superAdminRepository: SuperAdminRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateGymOwnerViewModel(superAdminRepository) as T
    }
}