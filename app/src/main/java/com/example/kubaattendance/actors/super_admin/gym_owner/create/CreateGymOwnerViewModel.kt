package com.example.kubaattendance.actors.super_admin.gym_owner.create

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class CreateGymOwnerViewModel(private var superAdminRepository: SuperAdminRepository) : ViewModel() {

    var fullName : String? = null
    var mobile : String? = null
    var email : String? = null
    var address : String? = null
    var photo : String? = null
    var username : String? = null
    var password : String? = null

    var iBaseListner : IBaseListner? = null

    var TAG = "CREATE_GYMOWNER_VIEWMODEL"

    fun onCreateGymOwnerClicked(view : View)
    {
        //validate fields
        if(fullName.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter Full name")
            return
        }
        if(mobile.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter mobile")
            return
        }
        if(email.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter email")
            return
        }
        if(address.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter address")
            return
        }
        if(username.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter username")
            return
        }
        if(password.isNullOrEmpty())
        {
            iBaseListner!!.showToast("Enter password")
            return
        }
        /*Log.e(TAG,"USER_NAME : "+MyApplication.prefs.getString(Constants.USER_NAME,"0")!!)
        Log.e(TAG,"USER_TYPE : "+MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!)
        Log.e(TAG,"fullName : "+fullName!!)
        Log.e(TAG,"mobile : "+mobile!!)
        Log.e(TAG,"email : "+email!!)
        Log.e(TAG,"address : "+address!!)
        Log.e(TAG,"username : "+username!!)
        Log.e(TAG,"password : "+password!!)
        Log.e(TAG,"getDeviceId : "+T.getDeviceId())*/

        iBaseListner!!.showProgress()
        Coroutines.main {
            try
            {
                var commonResponse = superAdminRepository.createGymOwner(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    "gym_owner",
                    fullName!!,
                    mobile!!,
                    email!!,
                    address!!,
                    username!!,
                    password!!,
                    T.getDeviceId())
                commonResponse.success?.let {

                    iBaseListner!!.onResponse(commonResponse)
                    return@main
                }
                iBaseListner!!.onFailure(commonResponse.message!!)
            }
            catch (e : Exception)
            {
                iBaseListner!!.onFailure(e.message!!)
                Log.e(TAG,"e : "+e)
                e.printStackTrace()
            }
        }
    }
    fun onSelectOwnerPhotoClicked(view : View)
    {
        iBaseListner!!.showToast("Select photo")
    }
}
