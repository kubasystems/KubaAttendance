package com.example.kubaattendance.data.network.responses.super_admin

data class HomePageResponse (
    var success : String? = null,
    var message : String? = null,
    var totalGyms : String? = null,
    var totalGymOwners : String? = null,
    var totalGymMembers : String? = null
)