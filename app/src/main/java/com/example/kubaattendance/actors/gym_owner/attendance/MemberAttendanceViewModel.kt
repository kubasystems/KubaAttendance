package com.example.kubaattendance.actors.gym_owner.attendance

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.ApiException
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.util.NoInternetException
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import java.lang.Exception

class MemberAttendanceViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {

    var iMemberAttendance : IMemberAttendance? = null

    var gym_name : String = "Select Gym"
    var select_date : String = "Select Date"
    var gym_id : String? = null

    fun selectDateButtonClicked(view : View)
    {
        iMemberAttendance!!.selectDateButtonClicked()
    }
    fun onSelectGymButtonClicked(view : View)
    {
        iMemberAttendance!!.selectGymButtonClicked()
    }
    fun getGymDetails()
    {
        if(!T.isNetworkAvailable())
        {
            iMemberAttendance!!.showToast(Constants.CONNECTION)
            return
        }
        iMemberAttendance!!.onStarted("Getting Gyms...")
        Coroutines.main {

            try {

                var myGymsResponse = gymOwnerRepository.getAllGymOwnerGyms(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!)

                myGymsResponse.success?.let {

                    iMemberAttendance!!.onResponse(myGymsResponse)
                    return@main
                }
                iMemberAttendance!!.onFailure(myGymsResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iMemberAttendance!!.onFailure(e.message!!)
            }
        }
    }
    fun getTodaysAttendance(input_date : String,gym_id : String,flag : String)
    {
        Log.e(Constants.KUBA_LOGS,"input_date : "+input_date)
        Log.e(Constants.KUBA_LOGS,"gym_id : "+gym_id)

        when(flag)
        {
            "F" -> iMemberAttendance!!.onStarted("Fetching attendance, please wait...")
            else -> {}
        }

        Coroutines.main {
            try
            {
                var username = MyApplication.prefs.getString(Constants.USER_NAME,"NA")

                val gdTodaysAttendanceResponse = gymOwnerRepository.getTodaysAttendance(gym_id,input_date,username!!)
                gdTodaysAttendanceResponse.success?.let {
                    iMemberAttendance?.onResponse(gdTodaysAttendanceResponse)
                    return@main
                }
                iMemberAttendance?.onFailure(gdTodaysAttendanceResponse.message!!)

            } catch (e: ApiException) {
                iMemberAttendance?.onFailure(e.message!!)
            }catch (e: NoInternetException) {
                iMemberAttendance?.onFailure(e.message!!)
            }
        }
    }
}
