package com.example.kubaattendance.data.network.responses.gym_device

data class GymDeviceAuthResponse (
    var success : String? = null,
    var message : String? = null,
    var user_info : List<GymDeviceData>

)