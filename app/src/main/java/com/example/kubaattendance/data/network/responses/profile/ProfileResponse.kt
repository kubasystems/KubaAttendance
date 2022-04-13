package com.example.kubaattendance.data.network.responses.profile

data class ProfileResponse (
    var success : String? = null,
    var message : String? = null,
    var profile : List<ProfileInfo>
)