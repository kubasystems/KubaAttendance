package com.example.kubaattendance.logout

import android.view.View
import androidx.lifecycle.ViewModel

class LogoutViewModel : ViewModel() {

    var iLogoutCallBacks : ILogoutCallBacks? = null

    fun onLogoutBtnClicked(view : View)
    {
        iLogoutCallBacks!!.onLogoutBtnClicked()
    }
    fun onCancelBtnClicked(view : View)
    {
        iLogoutCallBacks!!.onCancelBtnClicked()
    }
}
