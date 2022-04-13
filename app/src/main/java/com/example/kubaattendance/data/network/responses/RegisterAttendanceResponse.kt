package com.example.kubaattendance.data.network.responses

data class RegisterAttendanceResponse (
    val success : String?,
    val message : String?,
    val todaysInCount : String?,
    val todaysOutCount : String?,
    val todaysTotalCount : String?
)