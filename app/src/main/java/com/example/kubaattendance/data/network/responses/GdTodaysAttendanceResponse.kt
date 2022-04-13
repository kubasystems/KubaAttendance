package com.example.kubaattendance.data.network.responses

import com.example.kubaattendance.data.models.GdTodaysAttendance


data class GdTodaysAttendanceResponse (
    val success : String?,
    val message : String?,
    val attendance : List<GdTodaysAttendance>
)