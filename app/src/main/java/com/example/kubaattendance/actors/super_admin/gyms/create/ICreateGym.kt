package com.example.kubaattendance.actors.super_admin.gyms.create

import com.example.kubaattendance.utils.IBaseListner

interface ICreateGym : IBaseListner {

    fun showProgress(message : String)
    fun onSelectGymOwnerButtonClicked()
}