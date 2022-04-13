package com.example.kubaattendance.actors.gym_owner.payments.payment_history

data class PackageHistoryInfo (
    var packageId : String? = null,
    var packageName : String? = null,
    var packageAmount : String? = null,
    var packageFromDate : String? = null,
    var packageToDate : String? = null
)