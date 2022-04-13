package com.example.kubaattendance.actors.super_admin.gym_owner

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class SaGymOwnersFragmentViewModel(private val superAdminRepository: SuperAdminRepository) : ViewModel() {

    var iBaseListner : IGymOwner? = null


    fun getAllGymOwners() {

        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress()
        Coroutines.main {

            try
            {
                var gymOwnersResponse = superAdminRepository.getAllGymOwners()
                gymOwnersResponse.success?.let {
                    iBaseListner!!.onResponse(gymOwnersResponse)
                    return@main
                }
                iBaseListner!!.onFailure(gymOwnersResponse.message!!)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun deleteUser(gym_owners_user_id : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress("Deleting...")
        Coroutines.main {

            try
            {
                var commonResponse = superAdminRepository.deleteUser(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    gym_owners_user_id)
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
    fun activateDeactivateGymOwner(gym_owners_user_id : String,is_active : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress("Updating status...")
        Coroutines.main {

            try
            {
                var commonResponse = superAdminRepository.activeDeactiveUser(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    gym_owners_user_id,
                    is_active)
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

    fun onActivateDeactivateGymOwnerBtnClicked(gym_owners_user_id : String,is_active : String)
    {
        iBaseListner!!.onActivateDeactivateGymOwnerBtnClicked(gym_owners_user_id,is_active)
    }
    fun createGymOwnerFabClicked(view : View)
    {
        iBaseListner!!.createGymOwnerFabClicked()
    }
}