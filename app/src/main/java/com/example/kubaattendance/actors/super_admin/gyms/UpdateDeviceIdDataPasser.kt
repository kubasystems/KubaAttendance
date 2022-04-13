package com.example.kubaattendance.actors.super_admin.gyms

import java.io.Serializable

class UpdateDeviceIdDataPasser(private val dGymInfo : DGymInfo) : Serializable {
    var dGymInfo1 : DGymInfo? = null
    init
    {
        dGymInfo1 = dGymInfo
    }
}