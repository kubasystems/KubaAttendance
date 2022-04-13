package com.example.kubaattendance.data.network.responses.gym_owner

import com.example.kubaattendance.actors.gym_owner.pkg.data_class.DPkgInfo

data class PackagesResponse (
    var success : String? = null,
    var message : String? = null,
    var pkg_info : List<DPkgInfo>
)