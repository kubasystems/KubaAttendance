package com.example.kubaattendance.actors.gym_owner.payments.payment_history

import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages
import java.io.Serializable

class PackageHistoryDataHolder(data : ArrayList<MemberPackages>) : Serializable{

    var data = ArrayList<MemberPackages>()
    init
    {
        this.data = data
    }
}