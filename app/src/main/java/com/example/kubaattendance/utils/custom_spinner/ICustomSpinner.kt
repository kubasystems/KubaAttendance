package com.example.kubaattendance.utils.custom_spinner

import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo

interface ICustomSpinner {
    fun returnDataToFragment(dOwnerInfo: DOwnerInfo)
}