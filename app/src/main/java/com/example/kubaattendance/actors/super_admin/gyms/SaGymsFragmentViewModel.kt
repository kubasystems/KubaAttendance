package com.example.kubaattendance.actors.super_admin.gyms

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class SaGymsFragmentViewModel(private val superAdminRepository: SuperAdminRepository) : ViewModel() {

    var iBaseListner : IGyms? = null


    fun getAllGyms() {

        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress()

        Coroutines.main {

            try
            {
                var gymsResponse = superAdminRepository.getAllGyms()
                gymsResponse.success?.let {
                    iBaseListner!!.onResponse(gymsResponse)
                    return@main
                }
                iBaseListner!!.onFailure(gymsResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e("TAGG : ","e : "+e)
                e.printStackTrace()
            }
        }

    }


    fun activeDeactiveGymVM(gym_id : String, active_deactive_status : String)
    {
        iBaseListner!!.activeDeactiveGym(gym_id,active_deactive_status)
    }
    fun activeDeactiveGym(gym_id : String, active_deactive_status : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }

        when(active_deactive_status)
        {
            "0" -> iBaseListner!!.showProgress("Activating Gym...")
            "1" -> iBaseListner!!.showProgress("De-activating Gym...")
        }
        Coroutines.main {

            try
            {
                var commonResponse = superAdminRepository.activeDeactiveGym(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    gym_id,
                    active_deactive_status)
                commonResponse.success?.let {

                    iBaseListner!!.onResponse(commonResponse)
                    return@main
                }
                iBaseListner!!.onFailure(commonResponse.message!!)

            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iBaseListner!!.showToast(e.message!!)
            }
        }

    }
    fun createGymFabClicked(view : View)
    {
        iBaseListner!!.createGymFabClicked()
    }
    fun onDeviceIdBtnClicked(dGymInfo : DGymInfo)
    {
        iBaseListner!!.onDeviceIdBtnClicked(dGymInfo)
    }
    fun updateGymDeviceId(gym_id : String, device_id : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }

        iBaseListner!!.showProgress("Updating Device...")
        Coroutines.main {

            try
            {
                var commonResponse = superAdminRepository.activeDeactiveGym(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    gym_id,
                    device_id)
                commonResponse.success?.let {

                    iBaseListner!!.onResponse(commonResponse)
                    return@main
                }
                iBaseListner!!.onFailure(commonResponse.message!!)

            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iBaseListner!!.showToast(e.message!!)
            }
        }

    }
}