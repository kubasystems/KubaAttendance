package com.example.kubaattendance.actors.super_admin.gyms.create

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class CreateGymViewModel(private val superAdminRepository: SuperAdminRepository) : ViewModel() {


    var iCreateGym : ICreateGym? = null
    var iBaseListner : IBaseListner? = null


    var ownerNames : String = "Select Gym Owner*"//Autocomplete textview
    var gymID : String? = null
    var gymName : String? = null
    var gymDeviceID : String? = null
    var gymBranchId : String? = null
    var gymMobile : String? = null
    var gymEmail : String? = null
    var gymAddress : String? = null
    var gym_owner_user_id : String? = null
    var gym_owner_id : String? = null

    fun getAllGymOwners() {

        if(!T.isNetworkAvailable())
        {
            iCreateGym!!.showToast(Constants.CONNECTION)
            return
        }
        iCreateGym!!.showProgress("Loading Gym Owners...")
        Coroutines.main {

            try
            {
                var gymOwnersResponse = superAdminRepository.getAllGymOwners()
                gymOwnersResponse.success?.let {
                    iCreateGym!!.onResponse(gymOwnersResponse)
                    return@main
                }
                iCreateGym!!.onFailure(gymOwnersResponse.message!!)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun onSelectGymOwnerButtonClicked(view : View) {
        iCreateGym!!.onSelectGymOwnerButtonClicked()
    }


    fun onCreateGymBtnClicked(view : View)
    {
        if(!T.isNetworkAvailable())
        {
            iCreateGym!!.showToast(Constants.CONNECTION)
            return
        }
        if(ownerNames.equals("Select Gym Owner*"))
        {
            iCreateGym!!.showToast("Enter Owner name")
            return
        }
        if(gymName.isNullOrEmpty())
        {
            iCreateGym!!.showToast("Enter Gym name")
            return
        }
        if(gymBranchId.isNullOrEmpty())
        {
            iCreateGym!!.showToast("Enter Gym Branch ID")
            return
        }
        if(gymMobile.isNullOrEmpty())
        {
            iCreateGym!!.showToast("Enter Gym mobile")
            return
        }
        if(gymEmail.isNullOrEmpty())
        {
            iCreateGym!!.showToast("Enter Gym Email")
            return
        }
        if(gymAddress.isNullOrEmpty())
        {
            iCreateGym!!.showToast("Enter Gym Address")
            return
        }
        iCreateGym!!.showProgress()
        Coroutines.main {
            try {

                var commonResponse = superAdminRepository.createGym(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    T.getDeviceId(),
                    gym_owner_user_id!!,//get this from api
                    gym_owner_id!!,
                    gymName!!,
                    gymBranchId!!,
                    gymMobile!!,
                    gymEmail!!,
                    gymAddress!!
                )
                commonResponse.success?.let {

                    iCreateGym!!.onResponse(commonResponse)
                    return@main
                }
                iCreateGym!!.onFailure(commonResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iCreateGym!!.onFailure(e.message!!)
                //e.printStackTrace()
            }
        }

    }
    fun onUpdateDeviceIdFbClicked(view : View)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        if(gymDeviceID.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter Gym Device ID")
            return
        }
        iBaseListner!!.showProgress()
        Coroutines.main {
            try {

                var commonResponse = superAdminRepository.updateGymDeviceId(
                    gymID!!,//get this from api
                    gymDeviceID!!)
                commonResponse.success?.let {

                    iBaseListner!!.onResponse(commonResponse)
                    return@main
                }
                iBaseListner!!.onFailure(commonResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iBaseListner!!.onFailure(e.message!!)
                //e.printStackTrace()
            }
        }
    }
}
