package com.example.kubaattendance.actors.gym_owner.pkg

import com.example.kubaattendance.utils.IBaseListner

interface ICreatePackages : IBaseListner{

    fun showProgress(message : String)
    fun durationHintChanged(package_type : String)
    fun selectGymBtnClicked()

}