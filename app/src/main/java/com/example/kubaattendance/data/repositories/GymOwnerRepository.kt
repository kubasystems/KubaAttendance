package com.example.kubaattendance.data.repositories

import com.example.kubaattendance.data.network.MyApi
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.GdTodaysAttendanceResponse
import com.example.kubaattendance.data.network.responses.gym_owner.*
import com.example.kubaattendance.data.network.responses.gym_owner.id_card.MemberIdCardResponse
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.SafeApiRequest

class GymOwnerRepository(private val myApi: MyApi) : SafeApiRequest() {

    suspend fun getGymOwnerHomePageInfo(gym_id : String) : HomeResponse{
        return apiRequest { myApi.getGymOwnerHomePageInfo(gym_id) }
    }

    suspend fun getMembers(gym_id : String,athorised_user : String) : MembersResponse{
        return apiRequest { myApi.getMembers(gym_id,athorised_user) }
    }
    suspend fun createGymMember(createGymMemberJsonArr : String) : CommonResponse{
        return apiRequest { myApi.createGymMember(createGymMemberJsonArr) }
    }
    suspend fun getPackages(gym_id : String) : PackagesResponse{
        return apiRequest { myApi.getPackages(gym_id) }
    }
    suspend fun createPackage(athorised_user : String,gym_id : String,package_name : String,package_amount : String,package_duration : String,package_month_year_status : String,package_discount : String) : CommonResponse{
        return apiRequest { myApi.createPackage(athorised_user,gym_id,package_name,package_amount,package_duration,package_month_year_status,package_discount) }
    }
    suspend fun getAllGymOwnerGyms(user_id : String) : MyGymsResponse {
        return apiRequest { myApi.getAllGymOwnerGyms(user_id) }
    }
    suspend fun getMemberPaymentDetails(gym_id : String,member_id : String) : PaymentHistoryResponse {
        return apiRequest { myApi.getMemberPaymentDetails(gym_id,member_id) }
    }
    suspend fun getMemberPackagesPaymentDetails(gym_id : String,member_id : String,user_id : String,pkg_from_date : String,pkg_to_date : String) : PaymentHistoryResponse {
        return apiRequest { myApi.getMemberPackagesPaymentDetails(gym_id,member_id,user_id,pkg_from_date,pkg_to_date) }
    }
    suspend fun getMemberPackagesDetails(gym_id : String,member_id : String,user_id : String) : MemberPackagesDetailsResponse {
        return apiRequest { myApi.getMemberPackagesDetails(gym_id,member_id,user_id) }
    }
    suspend fun getPackageAndReceivedAmtDetails(gym_id : String,member_id: String,user_id : String) : PackageAndReceivedAmtDetailsResponse{
        return apiRequest { myApi.getPackageAndReceivedAmtDetails(gym_id,member_id,user_id) }
    }

    suspend fun createGymMemberPayment(memberPaymentArr : String) : CommonResponse{
        return apiRequest { myApi.createGymMemberPayment(memberPaymentArr) }
    }
    suspend fun getIdCardDetails(username : String,member_id: String) : MemberIdCardResponse{
        return apiRequest { myApi.getIdCardDetails(username,member_id) }
    }
    suspend fun activeDeactiveUser(athorised_user : String, user_id : String, active_deactive_status : String) : CommonResponse{
        return apiRequest { myApi.activeDeactiveUser(athorised_user, user_id, active_deactive_status) }
    }
    suspend fun getTodaysAttendance(gym_id : String,input_date : String,username : String) : GdTodaysAttendanceResponse {
        return apiRequest{myApi.getTodaysAttendance(gym_id,input_date,username)}
    }
}