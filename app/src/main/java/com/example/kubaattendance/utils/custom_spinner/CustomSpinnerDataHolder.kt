package com.example.kubaattendance.utils.custom_spinner

import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import java.io.Serializable

class CustomSpinnerDataHolder(data : ArrayList<DOwnerInfo>) : Serializable {

    var  data = ArrayList<DOwnerInfo>()
    init
    {
        this.data = data
    }
}