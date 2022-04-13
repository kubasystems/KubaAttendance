package com.example.kubaattendance.actors.gym_owner.home

import com.example.kubaattendance.utils.IBaseListner

interface IGoHome : IBaseListner{

    fun showProgress(message : String)
    fun onSelectGymButtonClicked()
    fun onGetTodaysAttendance()
}