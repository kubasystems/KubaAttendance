package com.example.kubaattendance.actors.gym_owner.pkg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymOwnerRepository

class PkgViewModelFactory(private val gymOwnerRepository: GymOwnerRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PkgViewModel(gymOwnerRepository) as T
    }
}