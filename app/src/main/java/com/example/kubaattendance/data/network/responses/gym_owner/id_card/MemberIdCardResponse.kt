package com.example.kubaattendance.data.network.responses.gym_owner.id_card

data class MemberIdCardResponse (
    var success : String? = null,
    var message : String? = null,
    var member_id_info : List<IdCardData>

)