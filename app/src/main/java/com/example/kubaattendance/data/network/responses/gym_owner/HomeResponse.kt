package com.example.kubaattendance.data.network.responses.gym_owner

data class HomeResponse (
    var success : String? = null,
    var message : String? = null,
    var todaysInCount : String? = null,
    var todaysOutCount : String? = null,
    var todaysTotalCount : String? = null
)