package com.example.kubaattendance.data.repositories

import com.example.kubaattendance.data.network.MyApi
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.super_admin.GymOwnersResponse
import com.example.kubaattendance.data.network.responses.super_admin.GymsResponse
import com.example.kubaattendance.data.network.responses.super_admin.HomePageResponse
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.SafeApiRequest

class SuperAdminRepository(private val api : MyApi) : SafeApiRequest() {

    suspend fun createGymOwner(athorised_user : String,user_type : String, full_name : String, mobile : String, email : String,address : String,username : String,password : String,device_id : String) : CommonResponse{
        return apiRequest { api.createGymOwner(athorised_user,user_type,full_name,mobile,email,address,username,password,device_id) }
    }
    suspend fun getAllGymOwners() : GymOwnersResponse{
        return apiRequest { api.getAllGymOwners() }
    }
    suspend fun getHomePageInfo() : HomePageResponse{
        return apiRequest { api.getHomePageInfo() }
    }

    suspend fun getAllGyms() : GymsResponse{
        return  apiRequest { api.getAllGyms() }
    }

    suspend fun createGym(athorised_user : String, device_id : String, gym_owner_user_id : String, gym_owner_id : String, gym_name : String, gym_branch_id : String, mobile : String, email : String, address : String) : CommonResponse{
        return apiRequest { api.createGym(athorised_user, device_id, gym_owner_user_id, gym_owner_id, gym_name, gym_branch_id, mobile, email, address) }
    }
    suspend fun activeDeactiveUser(athorised_user : String, user_id : String, active_deactive_status : String) : CommonResponse{
        return apiRequest { api.activeDeactiveUser(athorised_user, user_id, active_deactive_status) }
    }
    suspend fun deleteUser(athorised_user : String, user_id : String) : CommonResponse{
        return apiRequest { api.deleteUser(athorised_user, user_id) }
    }
    suspend fun activeDeactiveGym(athorised_user : String, gym_id : String, active_deactive_status : String) : CommonResponse{
        return apiRequest { api.activeDeactiveGym(athorised_user, gym_id, active_deactive_status) }
    }
    suspend fun updateGymDeviceId(gym_id : String, device_id : String) : CommonResponse{
        return apiRequest { api.updateGymDeviceId(gym_id, device_id) }
    }


}