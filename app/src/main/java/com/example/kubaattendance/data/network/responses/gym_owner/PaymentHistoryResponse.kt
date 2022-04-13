package com.example.kubaattendance.data.network.responses.gym_owner

import com.example.kubaattendance.actors.gym_owner.payments.PaymentHistoryInfo
import com.example.kubaattendance.actors.gym_owner.payments.RunningPackageInfo

data class PaymentHistoryResponse (
    var success : String? = null,
    var message : String? = null,
    var total_amt_received : String? = null,
    var running_pkginfo : List<RunningPackageInfo>,
    var received_amt_history : List<PaymentHistoryInfo>
)