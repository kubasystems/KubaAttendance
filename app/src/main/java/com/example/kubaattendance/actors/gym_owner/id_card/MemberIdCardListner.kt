package com.example.kubaattendance.actors.gym_owner.id_card

import com.example.kubaattendance.utils.IBaseListner

interface MemberIdCardListner : IBaseListner {

    fun onSetMemberImage(image_path : String)
    fun onSetMemberQrcode(qrcode_data : String)
}