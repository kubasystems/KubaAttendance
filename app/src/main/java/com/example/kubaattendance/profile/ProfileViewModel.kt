package com.example.kubaattendance.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.AuthRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class ProfileViewModel(private val userAuthRepository : AuthRepository) : ViewModel() {

    var id : String? = null
    var name : String? = null
    var mobile : String? = null
    var email : String? = null
    var address : String? = null
    var photo : String? = null
    var registered_at : String? = null

    var iProfile : IProfile? = null

    fun getProfile() {

        if (!T.isNetworkAvailable())
        {
            iProfile!!.showToast(Constants.CONNECTION)
            return
        }

        iProfile!!.showProgress("Getting Profile, please wait...")
        Coroutines.main {
            try
            {
                var profileResponse = userAuthRepository.getProfile(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!,
                    MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!
                )
                profileResponse.success?.let {

                    iProfile!!.displayProfile(profileResponse)
                    return@main
                }
                iProfile!!.onFailure(profileResponse.message!!)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun onSubmitProfileInfoBtnClicked(view : View)
    {
        if(!T.isNetworkAvailable())
        {
            iProfile!!.showToast(Constants.CONNECTION)
            return
        }
        if (name.isNullOrEmpty())
        {
            iProfile!!.showToast("Enter name")
            return
        }
        if (mobile.isNullOrEmpty())
        {
            iProfile!!.showToast("Enter mobile")
            return
        }
        if (email.isNullOrEmpty())
        {
            iProfile!!.showToast("Enter email")
            return
        }
        if (address.isNullOrEmpty())
        {
            iProfile!!.showToast("Enter address")
            return
        }
        iProfile!!.showProgress("Updating profile, please wait...")
        Coroutines.main {

            var commonResponse = userAuthRepository.updateProfile(
                MyApplication.prefs.getString(Constants.USER_ID,"0")!!,
                MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!,
                name!!,
                mobile!!,
                email!!,
                address!!)
            commonResponse.success?.let {

                iProfile!!.updateProfile(commonResponse)
                return@main
            }
            iProfile!!.onFailure(commonResponse.message!!)
        }

    }
    fun selectProfileImage(view : View)
    {
        iProfile!!.selectProfileImage()
    }
    fun updateProfileImage(image_code : String)
    {
        Log.e(Constants.KUBA_LOGS,"Constants.USER_ID : "+MyApplication.prefs.getString(Constants.USER_ID,"0")!!)
        Log.e(Constants.KUBA_LOGS,"Constants.USER_TYPE : "+MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!)
        Log.e(Constants.KUBA_LOGS,"image_code : "+image_code)
        if(!T.isNetworkAvailable())
        {
            iProfile!!.showToast(Constants.CONNECTION)
            return
        }

        iProfile!!.showProgress("Updating image...")
        try
        {
            Coroutines.main {

                var commonResponse = userAuthRepository.updateProfileImage(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!,
                    MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!,
                    image_code)
                commonResponse.success?.let {

                    iProfile!!.displayProfile(commonResponse)
                    return@main
                }
                iProfile!!.onFailure(commonResponse.message!!)
            }
        }
        catch (e : Exception)
        {
            Log.e(Constants.KUBA_LOGS,"e : "+e)
            e.printStackTrace()
        }
    }
}
