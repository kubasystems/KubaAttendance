package com.example.kubaattendance.data.network.responses
data class AuthResponse (
    val success : String?,
    val message : String?,
    val id : String?,
    val type_id : String?,
    val gym_id : String?,
    val user_id : String?,
    val gym_owner_id : String?,
    val gym_branch_id : String?,
    val gym_name : String?,
    val username : String?,
    val password : String?,
    val mobile : String?,
    val email : String?,
    val device_id : String?
)
