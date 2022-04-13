package com.example.kubaattendance.actors.gym_owner.my_gyms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymOwnerRepository

class MyGymsViewModelFactory(private val gymOwnerRepository: GymOwnerRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyGymsViewModel(gymOwnerRepository) as T
    }
}