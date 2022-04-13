package com.example.kubaattendance.data.network.responses.profile

data class ProfileInfo (
    var name : String? = null,
    var mobile : String? = null,
    var email : String? = null,
    var address : String? = null,
    var photo : String? = null,
    var is_active : String? = null,
    var created_at : String? = null
)