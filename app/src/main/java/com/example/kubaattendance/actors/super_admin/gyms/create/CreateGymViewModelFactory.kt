package com.example.kubaattendance.actors.super_admin.gyms.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.SuperAdminRepository

class CreateGymViewModelFactory(private var superAdminRepository: SuperAdminRepository) :  ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateGymViewModel(superAdminRepository) as T
    }
}