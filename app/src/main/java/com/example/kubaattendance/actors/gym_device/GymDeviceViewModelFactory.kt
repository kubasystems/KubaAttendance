package com.example.kubaattendance.actors.gym_device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymDeviceRepository

class GymDeviceViewModelFactory (private val gymDeviceRepository: GymDeviceRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GymDeviceViewModel(gymDeviceRepository) as T
    }

}