package com.example.kubaattendance.data.network.responses.gym_owner.id_card

data class IdCardData (
    var gymName : String? = null,
    var gymBranchCode : String? = null,
    var memberImage : String? = null,
    var memberName : String? = null,
    var qrCodeContent : String? = null,
    var memberAddress : String? = null,
    var memberDOJ : String? = null,
    var memberPhone : String? = null,
    var memberEmail : String? = null
)