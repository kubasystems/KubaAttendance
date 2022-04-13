package com.example.kubaattendance.authentication

import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.actors.gym_device.activity.GdHomeActivity
import com.example.kubaattendance.authentication.other_user.IOtherUser
import com.example.kubaattendance.data.repositories.AuthRepository
import com.example.kubaattendance.splash.IModuleChooser
import com.example.kubaattendance.util.ApiException
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.util.NoInternetException
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {


    var gym_name: String? = null
    var gym_owner_name: String? = null
    var gym_owner_mobile: String? = null
    var email: String? = null

    var username: String? = null
    var password: String? = null
    var gym_branch_id: String? = null

    var confirm_password: String? = null
    var authListner: AuthListner? = null
    var iOtherUser: IOtherUser? = null
    var iModuleChooser: IModuleChooser? = null
    var iGymDeviceListner: IGymDeviceListner? = null

    var TAG = "AUTH_VIEW_MODEL"

    fun onGymDeviceLoginBtnClicked(view: View)
    {
        iModuleChooser!!.onGymDeviceLoginBtnClicked()
    }
    fun onUsersLoginBtnClicked(view: View)
    {
        iModuleChooser!!.onUsersLoginBtnClicked()
    }
    fun onBackButtonClicked(view: View)
    {
        iModuleChooser?.onBackButtonClicked()
        authListner?.onBackPressed()
    }
    //handle login button clicked
    fun onOtherUserLoginButtonClicked(view: View)
    {
        if (!T.isNetworkAvailable())
        {
            iOtherUser?.showToast(Constants.CONNECTION)
            return
        }

        if (username.isNullOrEmpty() || password.isNullOrEmpty())
        {
            iOtherUser?.showToast("Username and Password can't be empty")
            return
        }
        iOtherUser?.showProgress()
        Coroutines.main {
            try
            {

                val otherUserLoginResponse = repository.userLogin(username!!, password!!)
                otherUserLoginResponse.success?.let {
                    iOtherUser?.onResponse(otherUserLoginResponse)
                    return@main
                }
                iOtherUser?.onFailure(otherUserLoginResponse.message!!)
            }
            catch (e: ApiException)
            {
                authListner?.onFailure(e.message!!)
            }
            catch (e: NoInternetException)
            {
                authListner?.onFailure(e.message!!)
            }
        }

    }
    fun onLoginButtonClicked(view: View)
    {

        if (username.isNullOrEmpty() || password.isNullOrEmpty())
        {
            iGymDeviceListner?.showToast("Username and Password can't be empty")
            return
        }
        iGymDeviceListner?.showProgress()
        Coroutines.main {
            try
            {
                var device_id = Settings.Secure.getString(MyApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID + "")
               Log.e(TAG,"device_id : "+device_id)
                val gymDeviceAuthResponse = repository.userLogin(username!!, password!!,gym_branch_id!!,device_id)
                gymDeviceAuthResponse.success?.let {
                    iGymDeviceListner?.onResponse(gymDeviceAuthResponse)
                    return@main
                }
                iGymDeviceListner?.onFailure(gymDeviceAuthResponse.message!!)
            }
            catch (e: Exception)
            {
                iGymDeviceListner?.onFailure(e.message!!)
            }
        }
    }
    //handle register button clicked
    fun onRegisterButtonClicked(view: View) {

        if (gym_name.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Gym name can't be empty")
            return
        }
        if (gym_name.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Owner name can't be empty")
            return
        }
        if (gym_owner_mobile.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Mobile can't be empty")
            return
        }
        if (gym_owner_mobile!!.length < 10)
        {
            authListner?.onDisplayToast("Mobile should be 10 digit")
            return
        }
        if (email.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Email can't be empty")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            authListner?.onDisplayToast("Invalid email address entered")
            return
        }
        if (username.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Username can't be empty")
            return
        }
        if (password.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Password can't be empty")
            return
        }
        if (confirm_password.isNullOrEmpty())
        {
            authListner?.onDisplayToast("Confirm Password can't be empty")
            return
        }
        if (!password.equals(confirm_password))
        {
            authListner?.onDisplayToast("Password and Confirm password need to be match")
            return
        }
        authListner?.onStarted()
        Coroutines.main {
            try {
                var device_id = Settings.Secure.getString(MyApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID + "");

                val registerResponse = repository.userRegister(gym_name!!,gym_owner_name!!,gym_owner_mobile!!,email!!,username!!, password!!,device_id,"gym_device")
                registerResponse.success?.let {
                    authListner?.onSuccess(registerResponse)
                    return@main
                }
                authListner?.onFailure(registerResponse.message!!)
            } catch (e: ApiException) {
                authListner?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                authListner?.onFailure(e.message!!)
            }
        }
    }
    fun onBackPress(view: View)
    {
        iOtherUser!!.onBackPress()
    }

}