package com.example.kubaattendance.actors.gym_owner.payments.interfaces

interface ICreateMemberPayment :
    IMemberPayments {
    fun payingAmountRadioChecked(amountType : String)
}