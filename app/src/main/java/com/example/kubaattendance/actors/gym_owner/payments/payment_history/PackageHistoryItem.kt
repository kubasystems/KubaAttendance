package com.example.kubaattendance.actors.gym_owner.payments.payment_history

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages
import com.example.kubaattendance.databinding.PackageHistoryItemBinding
import com.xwray.groupie.databinding.BindableItem

class PackageHistoryItem(private val memberPackages: MemberPackages, private val iPackageHistory: IPackageHistory) : BindableItem<PackageHistoryItemBinding>(){
    override fun getLayout(): Int = R.layout.package_history_item

    override fun bind(viewBinding: PackageHistoryItemBinding, position: Int) {
        viewBinding.memberPackages = memberPackages

        viewBinding.itemLi.setOnClickListener {
            iPackageHistory.returnResult(memberPackages)
        }
    }
}