package com.example.kubaattendance.actors.gym_owner.pkg

import android.view.View
import com.example.kubaattendance.utils.IBaseListner

interface IPkgs : IBaseListner{

    fun showProgress(message : String)
    fun selectGymBtnClicked()
    fun createPkgFabClicked(view : View)
}