package com.example.kubaattendance.authentication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_device.activity.GdHomeActivity
import com.example.kubaattendance.data.network.responses.gym_device.GymDeviceAuthResponse
import com.example.kubaattendance.databinding.ActivityLoginBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), IGymDeviceListner {


    var PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val authViewModel = ViewModelProviders.of(this, MyApplication.authViewModelFactory).get(AuthViewModel::class.java)
        binding.authViewModel = authViewModel
        authViewModel.iGymDeviceListner = this

        device_id_tv.setText("Device Id : "+ Settings.Secure.getString(MyApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID + ""))
        requestCameraPermission()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }
    private val PERMISSIONS_REQUEST_CAMERA = 0
    private fun requestCameraPermission()
    {
        if(!T.hasPermissions(this,PERMISSIONS))
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA)
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CAMERA)
        }
    }
    override fun selectBranchCodeBtnClicked() {

    }

    override fun getBranchCodeBtnClicked() {

    }

    override fun onOtherUserLoginButtonClicked() {

    }

    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Authenticating, please wait...")
    }

    override fun hideProgress() {

    }

    override fun onResponse(data: Any) {

        progress_layout.visibility = View.GONE

        var authResponse = data as GymDeviceAuthResponse
        if(authResponse.success.equals("1"))
        {
            if(authResponse.user_info.size > 0)
            {
                var dataa = authResponse.user_info.get(0)

                MyApplication.editor.putString(Constants.ID,dataa!!.id).commit()//owner_id
                MyApplication.editor.putString(Constants.GYM_ID,dataa!!.gym_id).commit()//owner_id
                MyApplication.editor.putString(Constants.USER_TYPE,dataa!!.user_type).commit()
                MyApplication.editor.putString(Constants.USER_TYPE_ID,dataa!!.user_type_id).commit()
                MyApplication.editor.putString(Constants.GYM_OWNER_ID,dataa!!.gym_owner_id).commit()
                MyApplication.editor.putString(Constants.GYM_BRANCH_ID,dataa!!.gym_branch_id).commit()
                MyApplication.editor.putString(Constants.GYM_NAME,dataa!!.gym_name).commit()
                MyApplication.editor.putString(Constants.USER_NAME,dataa!!.username).commit()
                MyApplication.editor.putString(Constants.MOBILE,dataa!!.mobile).commit()
                MyApplication.editor.putString(Constants.EMAIL,dataa!!.email).commit()


                toast("Successfully login")
                val intent = Intent(this, GdHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
                toast("Oops ! user info not found")
        }
        else
            toast(authResponse.message!!)

    }

    override fun showToast(message: String) {
        toast(message)

    }
    override fun onBackPressed()
    {
        finish()
    }


    override fun onFailure(message: String) {

        progress_layout.visibility = View.GONE
        toast(message)
    }

}
