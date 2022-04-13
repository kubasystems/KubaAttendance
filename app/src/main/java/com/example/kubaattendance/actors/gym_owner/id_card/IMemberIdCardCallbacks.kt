package com.example.kubaattendance.actors.gym_owner.id_card

import com.example.kubaattendance.utils.IBaseListner

interface IMemberIdCardCallbacks : IBaseListner {
    fun shareIdCardBtnClicked(member_details : String)
    fun saveIdCardBtnClicked(member_details : String)
}