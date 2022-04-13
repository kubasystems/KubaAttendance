package com.example.kubaattendance.data.network.responses.gym_owner

import com.example.kubaattendance.actors.gym_owner.members.data_classes.DMemberInfo

data class MembersResponse(
    var success : String? = null,
    var message : String? = null,
    var members : List<DMemberInfo>
)