package com.example.kubaattendance.utils

interface IBaseListner {
    fun showProgress()
    fun hideProgress()
    fun onResponse(data : Any)
    fun onFailure(message : String)
    fun showToast(message: String)
}