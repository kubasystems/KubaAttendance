package com.example.kubaattendance.actors.gym_device

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.actors.gym_device.activity.ViewAttendanceActivity
import com.example.kubaattendance.data.repositories.GymDeviceRepository
import com.example.kubaattendance.util.ApiException
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.util.NoInternetException
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import kuba.gym.kuba_gymatten.utils.lazyDeferred

class GymDeviceViewModel(private val repository : GymDeviceRepository) : ViewModel()
{
    var todaysInCount: String? = null
    var todaysOutCount: String? = null
    var todaysTotalCount: String? = null
    var gym_name: String? = null

    var gymDeviceListner: GymDeviceListner? = null
    var updateTimer: UpdateTimer? = null

    var TAG = "GYM_DEVICE_VIEWMODEL : "

    //to store todays attendance
    //check attendance is in or out

    fun onBackButtonClicked(view : View)
    {
        gymDeviceListner!!.onBackButtonClicked()
    }
    fun onRegisterAttendance(qrcode_id : String)
    {

        var qr_code = qrcode_id
        var gym_id = MyApplication.prefs.getString(Constants.GYM_ID,"NA")
        var punch_timestamp = T.getSystemDateTime()


        Log.e(Constants.KUBA_LOGS,"qr_code : "+qr_code+"\r\ngym_id : "+gym_id+"\r\npunch_timestamp : "+punch_timestamp)
        gymDeviceListner?.onStarted("Attending, please wait...")
        Coroutines.main {
            try
            {

                val registerAttendanceResponse = repository.registerAttendance(qr_code,gym_id!!,punch_timestamp!!)
                registerAttendanceResponse.success?.let {
                    gymDeviceListner?.onSuccess(registerAttendanceResponse)
                    return@main
                }
                gymDeviceListner?.onFailure(registerAttendanceResponse.message!!)
            } catch (e: ApiException) {
                gymDeviceListner?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                gymDeviceListner?.onFailure(e.message!!)
            }
        }
    }

    fun getTodaysAttendance()
    {
        gymDeviceListner!!.onStarted("Fetching attendance, please wait...")

        Coroutines.main {
            try
            {
                var username = MyApplication.prefs.getString(Constants.USER_NAME,"NA")
                var dateData = T.getSystemDateTime()!!.split(" ")
                val gdTodaysAttendanceResponse = repository.getTodaysAttendance(MyApplication.prefs.getString(Constants.GYM_ID,"0")!!,dateData.get(0),username!!)
                gdTodaysAttendanceResponse.success?.let {
                    gymDeviceListner?.onSuccess(gdTodaysAttendanceResponse)
                    return@main
                }
                gymDeviceListner?.onFailure(gdTodaysAttendanceResponse.message!!)

            } catch (e: ApiException) {
                gymDeviceListner?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                gymDeviceListner?.onFailure(e.message!!)
            }
        }
    }
    fun onLogoutButtonClicked(view : View)
    {
        gymDeviceListner!!.showLogoutDialog()
    }
    //to get todays attendance
    fun logoutApp()
    {
        gymDeviceListner?.onStarted("Logout, please wait...")
        Coroutines.main {
            try
            {
                var id = MyApplication.prefs.getString(Constants.ID,"NA")
                val logoutAppResponse = repository.logoutApp(id!!)
                logoutAppResponse.success?.let {
                    gymDeviceListner?.onSuccess(logoutAppResponse)
                    return@main
                }
                gymDeviceListner?.onFailure(logoutAppResponse.message!!)

            } catch (e: ApiException) {
                gymDeviceListner?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                gymDeviceListner?.onFailure(e.message!!)
            }
        }


    }
    fun getInOutTotalAttendanceCount()
    {
        gymDeviceListner?.onStarted("Refreshing, please wait...")
        Coroutines.main {
            try
            {
                var gym_id = MyApplication.prefs.getString(Constants.GYM_ID,"NA")
                Log.e(Constants.KUBA_LOGS,"gym_id : "+gym_id)
                val inOutTotalAttenResponse = repository.getInOutTotalAttendanceCount(gym_id!!)
                inOutTotalAttenResponse.success?.let {
                    gymDeviceListner?.onSuccess(inOutTotalAttenResponse)
                    return@main
                }
                gymDeviceListner?.onFailure(inOutTotalAttenResponse.message!!)

            } catch (e: ApiException) {
                gymDeviceListner?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                gymDeviceListner?.onFailure(e.message!!)
            }
        }
    }
    //display timer on the screen
    fun startTimerUpdate()
    {
        val timerHandler = Handler(Looper.getMainLooper())
        timerHandler.post(object : Runnable{
            override fun run() {
                updateTimer!!.updateTimerTimestamp(""+T.getSystemDateTime())
                timerHandler.postDelayed(this,1000)
            }

        })
    }
    fun viewAttendance(view : View)
    {
        Intent(view.context,ViewAttendanceActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
}