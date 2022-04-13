package com.example.kubaattendance.actors.super_admin.homepage

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.T

class SaHomeFragmentViewModel (private val superAdminRepository: SuperAdminRepository) : ViewModel(){

    var totalGymsCount : String? = null
    var totalGymOwnerCount : String? = null
    var totalGymMembersCount : String? = null
    var iSaHomeCallbacks : SaHomeCallbacks? = null

    var  TAG = "SA_HOME_FRAGMENT_VIEW_MODEL"

    fun getHomePageInfo() {

        if(!T.isNetworkAvailable())
        {
            iSaHomeCallbacks!!.showToast("Please, check your internet connection.")
            return
        }

        iSaHomeCallbacks!!.showProgress()
        Coroutines.main {
            try
            {
                val homePageResponse = superAdminRepository.getHomePageInfo()
                homePageResponse.success?.let {
                    iSaHomeCallbacks!!.onResponse(homePageResponse)
                    return@main
                }
                iSaHomeCallbacks!!.showToast(homePageResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(TAG,"e : "+e)
                //iSaHomeCallbacks!!.showToast(""+e)
            }
        }
    }

    fun getTotalGymsDetails(v : View)
    {
        //total gyms details
        iSaHomeCallbacks!!.totalGymsViewClicked()

    }
    fun getTotalGymOwnerDetails(v : View)
    {
        //total gym owner details
        iSaHomeCallbacks!!.totalGymOwnerViewClicked()
    }
    fun getTotalGymMembersDetails(v : View)
    {
        //gym members details
        iSaHomeCallbacks!!.totalGymMembersViewClicked()
    }
}