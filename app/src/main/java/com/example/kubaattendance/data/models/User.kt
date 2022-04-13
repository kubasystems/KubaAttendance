package com.example.kubaattendance.data.models


data class User (
    var id : String? = null,
    var username : String? = null,
    var password : String? = null,
    var name : String? = null,
    var email : String? = null,
    var user_type : String? = null,
    var status : String? = null,
    var created_date : String? = null
)