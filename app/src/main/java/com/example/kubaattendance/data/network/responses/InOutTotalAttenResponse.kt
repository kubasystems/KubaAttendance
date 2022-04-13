package com.example.kubaattendance.data.network.responses

data class InOutTotalAttenResponse (
    val success : String?,
    val message : String?,
    val todaysInCount : String?,
    val todaysOutCount : String?,
    val todaysTotalCount : String?
)