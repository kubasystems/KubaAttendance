package com.example.kubaattendance.data.network.responses.gym_owner

import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages

data class MemberPackagesDetailsResponse (
    var success : String? = null,
    var message : String? = null,
    var member_packages : List<MemberPackages>
)