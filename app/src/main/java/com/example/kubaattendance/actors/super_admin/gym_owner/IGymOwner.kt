package com.example.kubaattendance.actors.super_admin.gym_owner

import android.view.View
import com.example.kubaattendance.utils.IBaseListner

interface IGymOwner : IBaseListner {

    fun showProgress(message : String)
    fun onActivateDeactivateGymOwnerBtnClicked(gym_owners_user_id : String,is_active : String)
    fun createGymOwnerFabClicked()

}