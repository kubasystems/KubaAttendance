package com.example.kubaattendance.authentication

import com.example.kubaattendance.data.models.User
import com.example.kubaattendance.data.network.responses.AuthResponse
import com.example.kubaattendance.data.network.responses.RegisterResponse


interface AuthListner {

    fun onStarted()
    fun onBackPressed()
    fun onDisplayToast(message : String)
    fun onSuccess(authResponse: AuthResponse)
    fun onSuccess(registerResponse : RegisterResponse)
    fun onFailure(message : String)
}