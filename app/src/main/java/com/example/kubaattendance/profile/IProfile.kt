package com.example.kubaattendance.profile

import com.example.kubaattendance.utils.IBaseListner

interface IProfile : IBaseListner {

    fun displayProfile(result : Any)
    fun updateProfile(result : Any)
    fun showProgress(message : String)
    fun selectProfileImage()
}