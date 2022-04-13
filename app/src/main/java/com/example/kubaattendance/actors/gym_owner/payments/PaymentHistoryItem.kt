package com.example.kubaattendance.actors.gym_owner.payments

import com.example.kubaattendance.R
import com.example.kubaattendance.databinding.LayoutPaymentHistoryItemBinding
import com.example.kubaattendance.utils.T
import com.xwray.groupie.databinding.BindableItem

class PaymentHistoryItem(private val paymentHistoryInfo: PaymentHistoryInfo) : BindableItem<LayoutPaymentHistoryItemBinding>(){
    override fun getLayout(): Int = R.layout.layout_payment_history_item



    override fun bind(viewBinding: LayoutPaymentHistoryItemBinding, position: Int) {
        viewBinding.paymentHistoryInfo = paymentHistoryInfo

        if(paymentHistoryInfo.created_at!!.contains(" "))
        {
            var dateData = paymentHistoryInfo.created_at!!.split(" ")
            viewBinding.createdAtTv.setText(""+ T.getFormatedDate(dateData.get(0))+" "+dateData.get(1))
        }

    }
}