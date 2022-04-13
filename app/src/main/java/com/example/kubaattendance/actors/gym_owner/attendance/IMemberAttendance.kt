package com.example.kubaattendance.actors.gym_owner.attendance

import com.example.kubaattendance.utils.IBaseListner

interface IMemberAttendance : IBaseListner {

    fun onStarted(message : String)
    fun selectGymButtonClicked()
    fun selectDateButtonClicked()
}