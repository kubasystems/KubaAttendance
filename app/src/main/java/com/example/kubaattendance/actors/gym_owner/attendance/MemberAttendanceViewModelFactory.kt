package com.example.kubaattendance.actors.gym_owner.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kubaattendance.data.repositories.GymOwnerRepository

class MemberAttendanceViewModelFactory(private val gymOwnerRepository: GymOwnerRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemberAttendanceViewModel(gymOwnerRepository) as T
    }
}