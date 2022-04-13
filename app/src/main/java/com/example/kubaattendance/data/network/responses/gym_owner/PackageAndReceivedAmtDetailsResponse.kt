package com.example.kubaattendance.data.network.responses.gym_owner

data class PackageAndReceivedAmtDetailsResponse (
    var success : String? = null,
    var message : String? = null,
    var pkg_amount : String? = null,
    var pkg_name : String? = null,
    var package_id : String? = null,
    var total_amt_received : String? = null
)