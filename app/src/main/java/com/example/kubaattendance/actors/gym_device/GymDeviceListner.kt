package com.example.kubaattendance.actors.gym_device

import com.example.kubaattendance.data.models.GdTodaysAttendance

interface GymDeviceListner {
    fun showLogoutDialog()
    fun onStarted(message: String)
    fun onSuccess(anyObject : Any)
    fun onSuccess(anyObject : List<GdTodaysAttendance>)
    fun onFailure(message : String)
    fun onBackButtonClicked()
}