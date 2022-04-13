package com.example.kubaattendance.actors.gym_owner.payments.payment_history

import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages

interface IPackageHistory
{
    fun returnResult(packageHistoryInfo: MemberPackages)
}