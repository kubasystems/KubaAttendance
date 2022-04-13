package com.example.kubaattendance.data.network.responses.super_admin

import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo

data class GymOwnersResponse (
    var success : String? = null,
    var message : String? = null,
    var gym_details : List<DOwnerInfo>
)