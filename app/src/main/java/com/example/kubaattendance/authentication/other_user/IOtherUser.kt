package com.example.kubaattendance.authentication.other_user

import com.example.kubaattendance.data.network.responses.OtherUserLoginResponse
import com.example.kubaattendance.utils.IBaseListner

interface IOtherUser : IBaseListner{
    fun onResponse(otherUserLoginResponse: OtherUserLoginResponse)
    fun onBackPress()

}