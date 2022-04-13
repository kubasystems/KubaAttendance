package com.example.kubaattendance.data.repositories

import com.example.kubaattendance.data.network.MyApi
import com.example.kubaattendance.data.network.responses.AuthResponse
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.OtherUserLoginResponse
import com.example.kubaattendance.data.network.responses.RegisterResponse
import com.example.kubaattendance.data.network.responses.gym_device.GymDeviceAuthResponse
import com.example.kubaattendance.data.network.responses.profile.ProfileResponse
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.SafeApiRequest


class AuthRepository(private val api : MyApi) : SafeApiRequest(){

    //Api calls goes here
    //for user login
    suspend fun userLogin(username : String, password : String, gym_branch_id : String,device_id : String) : GymDeviceAuthResponse {
        return apiRequest{api.userLogin(username,password,gym_branch_id,device_id,"gym_device")}
    }

    //register user
    suspend fun userRegister(gym_name : String,gym_owner_name : String, gym_owner_mobile : String,email : String,username : String, password : String, device_id : String,user_type : String) : RegisterResponse {
        return apiRequest{api.userRegister(gym_name,gym_owner_name,gym_owner_mobile,email,username,password,device_id,user_type)}
    }
    suspend fun userLogin(username : String, password : String) : OtherUserLoginResponse {
        return apiRequest{api.otherUserLogin(username,password)}
    }

    //get profile
    suspend fun getProfile(user_id : String,user_type : String) : ProfileResponse{
        return apiRequest { api.getProfile(user_id,user_type) }
    }

    suspend fun updateProfile(user_id : String,user_type : String,name : String,mobile : String,email : String,address : String) : CommonResponse{
        return apiRequest { api.updateProfile(user_id,user_type,name,mobile,email,address) }
    }
    suspend fun updateProfileImage(user_id : String,user_type : String,image_code : String) : CommonResponse{
        return apiRequest { api.updateProfileImage(user_id,user_type,image_code) }
    }
}