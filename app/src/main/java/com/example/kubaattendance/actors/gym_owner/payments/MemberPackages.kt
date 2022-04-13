package com.example.kubaattendance.actors.gym_owner.payments

data class MemberPackages (
    var package_id : String? = null,
    var gym_id : String? = null,
    var pkg_name : String? = null,
    var pkg_amount : String? = null,
    var pkg_from_date : String? = null,
    var pkg_to_date : String? = null
)