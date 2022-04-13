package com.example.kubaattendance.actors.gym_owner.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymOwnerRepository

class GoMembersViewModelFactory(private val gymOwnerRepository: GymOwnerRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GoMembersViewModel(gymOwnerRepository) as T
    }
}