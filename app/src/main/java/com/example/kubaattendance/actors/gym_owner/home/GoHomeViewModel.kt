package com.example.kubaattendance.actors.gym_owner.home

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import java.lang.Exception
import java.net.SocketTimeoutException

class GoHomeViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {

    var inCount : String? = null
    var outCount : String? = null
    var totalCount : String? = null

    var gym_id : String? = null
    var gym_name : String = "Select Gym"

    var iBaseListner : IGoHome? = null

    fun onSelectGymButtonClicked(view : View)
    {
        iBaseListner!!.onSelectGymButtonClicked()
    }
    fun getGymDetails()
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress("Getting Gyms...")
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
                iBaseListner!!.onFailure(e.message!!)
            }
        }
    }
    fun getHomePageInfo(gym_id : String,flag : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        when(flag)
        {
            "F" -> iBaseListner!!.showProgress("Loading, please wait...")
            else -> {}
        }

        Coroutines.main {

            try {

                var homeResponse = gymOwnerRepository.getGymOwnerHomePageInfo(gym_id)

                homeResponse.success?.let {

                    iBaseListner!!.onResponse(homeResponse)
                    return@main
                }
                iBaseListner!!.onFailure(homeResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iBaseListner!!.onFailure(e.message!!)
            }
        }
    }
    fun onGetTodaysAttendance(view : View)
    {
        iBaseListner!!.onGetTodaysAttendance()
    }
}
