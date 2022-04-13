package com.example.kubaattendance.actors.gym_owner.my_gyms

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class MyGymsViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {

    var iBaseListner : IBaseListner? = null

    fun getAllGymOwnerGyms()
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress()
        //Log.e(Constants.KUBA_LOGS,"USER_ID : "+MyApplication.prefs.getString(Constants.USER_ID,"0")!!)
        Coroutines.main {

            try {

                var myGymsResponse = gymOwnerRepository.getAllGymOwnerGyms(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!)

                myGymsResponse.success?.let {

                    iBaseListner!!.onResponse(myGymsResponse)
                    return@main
                }
                iBaseListner!!.onFailure(myGymsResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iBaseListner!!.hideProgress()
                iBaseListner!!.showToast(e.message!!)
            }
        }
    }
}