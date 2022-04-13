package com.example.kubaattendance.actors.gym_owner.payments.interfaces

import com.example.kubaattendance.utils.IBaseListner

interface IMemberPayments : IBaseListner{

    fun showProgress(message : String)
    fun selectGymButtonClicked()
    fun selectMemberButtonClicked()
    fun createMemberPaymentButtonClicked()
    fun selectPAckageButtonClicked()

}