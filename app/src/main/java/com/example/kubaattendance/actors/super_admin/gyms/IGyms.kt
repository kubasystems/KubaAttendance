package com.example.kubaattendance.actors.super_admin.gyms

import com.example.kubaattendance.utils.IBaseListner

interface IGyms : IBaseListner{
    fun showProgress(message : String)
    fun activeDeactiveGym(gym_id : String, active_deactive_status : String)
    fun createGymFabClicked()
    fun onDeviceIdBtnClicked(dGymInfo : DGymInfo)

}