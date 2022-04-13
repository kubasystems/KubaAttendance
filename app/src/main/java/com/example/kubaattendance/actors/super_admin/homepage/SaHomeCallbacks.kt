package com.example.kubaattendance.actors.super_admin.homepage

import com.example.kubaattendance.utils.IBaseListner

interface SaHomeCallbacks : IBaseListner{

    fun totalGymsViewClicked()
    fun totalGymOwnerViewClicked()
    fun totalGymMembersViewClicked()

}