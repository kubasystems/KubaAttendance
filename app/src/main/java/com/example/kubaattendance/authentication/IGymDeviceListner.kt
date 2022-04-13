package com.example.kubaattendance.authentication

import com.example.kubaattendance.utils.IBaseListner

interface IGymDeviceListner : IBaseListner {

    fun selectBranchCodeBtnClicked()
    fun getBranchCodeBtnClicked()
    fun onOtherUserLoginButtonClicked()
}