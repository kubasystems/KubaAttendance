package com.example.kubaattendance.authentication.other_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_member.GmHomeActivity
import com.example.kubaattendance.actors.gym_owner.GoHomeActivity
import com.example.kubaattendance.actors.gym_reception_user.ReceptionPersonActivity
import com.example.kubaattendance.actors.super_admin.SaHomeActivity
import com.example.kubaattendance.authentication.AuthViewModel
import com.example.kubaattendance.data.network.responses.OtherUserLoginResponse
import com.example.kubaattendance.databinding.ActivityOtherUserBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.toast
import kotlinx.android.synthetic.main.activity_other_user.*

class OtherUserLoginActivity : AppCompatActivity(),IOtherUser{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_other_user)

        val binding : ActivityOtherUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_other_user)
        val authViewModel = ViewModelProviders.of(this, MyApplication.authViewModelFactory).get(
            AuthViewModel::class.java)
        binding.authViewModel = authViewModel
        authViewModel.iOtherUser = this
        device_id_tv.setText("Device Id : "+ Settings.Secure.getString(MyApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID + ""))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
    override fun onResponse(otherUserLoginResponse: OtherUserLoginResponse) {
        progress_layout.visibility = View.GONE
        if(otherUserLoginResponse.success.equals("1"))
        {
            var userInfo = otherUserLoginResponse.user_info.get(0)

            MyApplication.editor.putString(Constants.ID,userInfo.id).commit()
            MyApplication.editor.putString(Constants.USER_ID,userInfo.user_id).commit()
            MyApplication.editor.putString(Constants.NAME,userInfo.name).commit()
            MyApplication.editor.putString(Constants.MOBILE,userInfo.mobile).commit()
            MyApplication.editor.putString(Constants.EMAIL,userInfo.email).commit()
            MyApplication.editor.putString(Constants.ADDRESS,userInfo.address).commit()
            MyApplication.editor.putString(Constants.PHOTO,userInfo.photo).commit()
            MyApplication.editor.putString(Constants.CREATED_DATE,userInfo.created_at).commit()
            MyApplication.editor.putString(Constants.USER_TYPE,userInfo.user_type).commit()
            MyApplication.editor.putString(Constants.USER_NAME,userInfo.username).commit()
            MyApplication.editor.putString(Constants.PASSWORD,userInfo.password).commit()
            MyApplication.editor.putString(Constants.IS_ACTIVE,userInfo.is_active).commit()

            if(userInfo.user_type.equals("super_admin"))
            {
                Intent(this,SaHomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            else if(userInfo.user_type.equals("gym_owner"))
            {
                Intent(this,GoHomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            else if(userInfo.user_type.equals("gym_member"))
            {
                Intent(this,
                    GmHomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            else if(userInfo.user_type.equals("reception_person"))
            {
                Intent(this,ReceptionPersonActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }

        }
        else
        {
            toast(otherUserLoginResponse.message!!)
        }

    }

    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Authenticating, please wait...")
    }

    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE
    }

    override fun onBackPress() {
        finish()
    }

    override fun showToast(message: String) {

    }
    override fun onFailure(message: String) {
        toast(message)
    }

}
