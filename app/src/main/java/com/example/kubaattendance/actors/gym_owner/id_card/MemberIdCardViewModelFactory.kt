package com.example.kubaattendance.actors.gym_owner.id_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymOwnerRepository

class MemberIdCardViewModelFactory(private val gymOwnerRepository: GymOwnerRepository) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemberIdCardViewModel(gymOwnerRepository) as T
    }
}