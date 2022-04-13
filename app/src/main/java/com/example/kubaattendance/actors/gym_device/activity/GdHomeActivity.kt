package com.example.kubaattendance.actors.gym_device.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.budiyev.android.codescanner.*
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_device.GymDeviceListner
import com.example.kubaattendance.actors.gym_device.GymDeviceViewModel
import com.example.kubaattendance.actors.gym_device.UpdateTimer
import com.example.kubaattendance.authentication.LoginActivity
import com.example.kubaattendance.data.models.GdTodaysAttendance
import com.example.kubaattendance.data.network.responses.InOutTotalAttenResponse
import com.example.kubaattendance.data.network.responses.LogoutAppResponse
import com.example.kubaattendance.data.network.responses.RegisterAttendanceResponse
import com.example.kubaattendance.databinding.ActivityGdHomeBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.toast
import kotlinx.android.synthetic.main.activity_gd_home.*
import kotlinx.android.synthetic.main.layout_success_fail.*

class GdHomeActivity : AppCompatActivity(), GymDeviceListner,UpdateTimer {



    private lateinit var codeScanner: CodeScanner
    lateinit var gymDeviceViewModel : GymDeviceViewModel
    lateinit var binding : ActivityGdHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding  = DataBindingUtil.setContentView(this, R.layout.activity_gd_home)
        gymDeviceViewModel = ViewModelProviders.of(this, MyApplication.gymDeviceViewModelFactory).get(GymDeviceViewModel::class.java)
        binding.gymDeviceViewModel = gymDeviceViewModel
        gymDeviceViewModel.gymDeviceListner = this
        gymDeviceViewModel.updateTimer = this

        prepareScaner()
        setDataToUi()




        //call timer to displa watch on page
        gymDeviceViewModel.startTimerUpdate()

        //in out and total count when app first launch
        if(T.isNetworkAvailable())
            gymDeviceViewModel.getInOutTotalAttendanceCount()
        else
            toast(Constants.CONNECTION)
    }

    override fun onBackButtonClicked() {

    }
    override fun updateTimerTimestamp(timeStamp: String) {

        var dateData = timeStamp.split(" ")
        timer_date_txt.setText(dateData.get(0))
        timer_time_txt.setText(dateData.get(1))
    }

    private fun setDataToUi() {

        //set data to ui
        binding.gymDeviceViewModel = gymDeviceViewModel
        gymDeviceViewModel.gym_name = MyApplication.prefs.getString(Constants.GYM_NAME,"0")
        gymDeviceViewModel.todaysInCount = MyApplication.prefs.getString(Constants.TODAY_IN_COUNT,"0")
        gymDeviceViewModel.todaysOutCount = MyApplication.prefs.getString(Constants.TODAY_OUT_COUNT,"0")
        gymDeviceViewModel.todaysTotalCount = MyApplication.prefs.getString(Constants.TODAY_TOTAL_COUNT,"0")
    }

    private fun prepareScaner() {

        codeScanner = CodeScanner(this, scanner_view)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_FRONT // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not


        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //toast(""+it.text)
                gymDeviceViewModel.onRegisterAttendance(it.text)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                toast("Camera initialization error: ${it.message}")
            }
        }
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    override fun onStarted(message: String) {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }

    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
    }
    var attendanceFlag = ""
    override fun onSuccess(anyResponse : Any) {
        progress_layout.visibility = View.GONE

        if(anyResponse is RegisterAttendanceResponse)
        {
            codeScanner.startPreview()
            var success = anyResponse.success
            if(success.equals("1"))
            {
                attendanceFlag = "1"
                //put data in local storage
                MyApplication.editor.putString(Constants.TODAY_IN_COUNT,anyResponse.todaysInCount).commit()
                MyApplication.editor.putString(Constants.TODAY_OUT_COUNT,anyResponse.todaysOutCount).commit()
                MyApplication.editor.putString(Constants.TODAY_TOTAL_COUNT,anyResponse.todaysTotalCount).commit()
                setDataToUi()
                displayDialogMsg()
            }
            else
            {
                attendanceFlag = "0"
                displayDialogMsg()
            }

        }
        else if(anyResponse is LogoutAppResponse)
        {
            var success = anyResponse.success
            if(success.equals("1"))
            {
                toast("Logout successfully")
                MyApplication.editor.clear().commit()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            else
            {
                var message = anyResponse.message
                toast(message!!)
            }

        }
        else if(anyResponse is InOutTotalAttenResponse)
        {
            var success = anyResponse.success
            if(success.equals("1"))
            {
                //put data in local storage
                MyApplication.editor.putString(Constants.TODAY_IN_COUNT,anyResponse.todaysInCount).commit()
                MyApplication.editor.putString(Constants.TODAY_OUT_COUNT,anyResponse.todaysOutCount).commit()
                MyApplication.editor.putString(Constants.TODAY_TOTAL_COUNT,anyResponse.todaysTotalCount).commit()

                //refresh count on ui
                setDataToUi()

            }
            else
            {
                var message = anyResponse.message
                toast(message!!)
            }

        }


    }
    private fun displayDialogMsg() {
        val mRunnable: Runnable
        val mHandler = Handler()
        when(attendanceFlag)
        {
            "1"->{
                hide_ss.visibility = View.VISIBLE
                image_im.setImageResource(R.drawable.ic_success)
                message_tv.setText("Your attendance registered")
            }
            "0"->{
                hide_ss.visibility = View.VISIBLE
                image_im.setImageResource(R.drawable.ic_fail)
                message_tv.setText("Your attendance not registered")
            }
            else->{}
        }
        mRunnable = Runnable {
            // TODO Auto-generated method stub
            hide_ss.visibility = View.GONE
        }
        mHandler.postDelayed(mRunnable, 1500)
    }


    override fun onSuccess(anyObject: List<GdTodaysAttendance>) {

    }
    override fun showLogoutDialog() {



        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Logout")
            .setConfirmText("Logout")
            .setCancelText("Cancel")
            .setContentText("Do you want to logout app.")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()

                //logout app here
                if (T.isNetworkAvailable())
                    gymDeviceViewModel.logoutApp()
                else
                    toast(Constants.CONNECTION)

            }
            .setCancelClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }
}
