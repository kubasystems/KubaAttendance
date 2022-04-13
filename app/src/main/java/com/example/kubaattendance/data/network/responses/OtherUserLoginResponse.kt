package com.example.kubaattendance.data.network.responses

import com.example.kubaattendance.authentication.other_user.UserInfo

data class OtherUserLoginResponse (
    val success : String?,
    val message : String?,
    val user_info : List<UserInfo>
)