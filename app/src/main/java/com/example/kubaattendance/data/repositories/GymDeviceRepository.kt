package com.example.kubaattendance.data.repositories

import com.example.kubaattendance.data.network.MyApi
import com.example.kubaattendance.data.network.responses.GdTodaysAttendanceResponse
import com.example.kubaattendance.data.network.responses.InOutTotalAttenResponse
import com.example.kubaattendance.data.network.responses.LogoutAppResponse
import com.example.kubaattendance.data.network.responses.RegisterAttendanceResponse
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.SafeApiRequest

class GymDeviceRepository (private val api : MyApi) : SafeApiRequest(){

    //api calls
    //get todays attendance


    //api calls
    suspend fun registerAttendance(qr_code : String,gym_id : String,punch_timestamp : String) : RegisterAttendanceResponse {
        return apiRequest{api.registerAttendance(qr_code,gym_id!!,punch_timestamp!!)}
    }

    //logout app
    suspend fun logoutApp(id : String) : LogoutAppResponse {
        return apiRequest{api.logoutApp(id)}
    }

    //get in out total count of gym
    suspend fun getInOutTotalAttendanceCount(gym_id : String) : InOutTotalAttenResponse {
        return apiRequest{api.getInOutTotalAttendanceCount(gym_id)}
    }
    suspend fun getTodaysAttendance(gym_id : String,input_date : String,username : String) : GdTodaysAttendanceResponse {
        return apiRequest{api.getTodaysAttendance(gym_id,input_date,username)}
    }

}