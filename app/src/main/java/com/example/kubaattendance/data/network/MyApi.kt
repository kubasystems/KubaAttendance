package com.example.kubaattendance.data.network

import com.example.kubaattendance.data.network.responses.*
import com.example.kubaattendance.data.network.responses.gym_device.GymDeviceAuthResponse
import com.example.kubaattendance.data.network.responses.gym_owner.*
import com.example.kubaattendance.data.network.responses.gym_owner.id_card.MemberIdCardResponse
import com.example.kubaattendance.data.network.responses.profile.ProfileResponse
import com.example.kubaattendance.data.network.responses.super_admin.GymOwnersResponse
import com.example.kubaattendance.data.network.responses.super_admin.GymsResponse
import com.example.kubaattendance.data.network.responses.super_admin.HomePageResponse
import com.example.kubaattendance.utils.Constants
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface MyApi{

    //---------------Gym Owner----------------

    @FormUrlEncoded//if we use post request
    @POST("createGymMemberPayment")
    suspend fun  createGymMemberPayment(
        @Field("memberPaymentArr")
        memberPaymentArr : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("getPackageAndReceivedAmtDetails")
    suspend fun  getPackageAndReceivedAmtDetails(
        @Field("gym_id")
        gym_id : String,
        @Field("member_id")
        member_id : String,
        @Field("user_id")
        user_id : String
    ) : Response<PackageAndReceivedAmtDetailsResponse>

    @FormUrlEncoded//if we use post request
    @POST("getIdCardDetails")
    suspend fun  getIdCardDetails(
        @Field("username")
        username : String,
        @Field("member_id")
        member_id : String
    ) : Response<MemberIdCardResponse>

    @FormUrlEncoded//if we use post request
    @POST("getMemberPaymentDetails")
    suspend fun  getMemberPaymentDetails(
        @Field("gym_id")
        gym_id : String,
        @Field("member_id")
        member_id : String
    ) : Response<PaymentHistoryResponse>

    @FormUrlEncoded//if we use post request
    @POST("getMemberPackagesPaymentDetails")
    suspend fun  getMemberPackagesPaymentDetails(
        @Field("gym_id")
        gym_id : String,
        @Field("member_id")
        member_id : String,
        @Field("user_id")
        user_id : String,
        @Field("pkg_from_date")
        pkg_from_date : String,
        @Field("pkg_to_date")
        pkg_to_date : String
    ) : Response<PaymentHistoryResponse>


    @FormUrlEncoded//if we use post request
    @POST("getMemberPackagesDetails")
    suspend fun  getMemberPackagesDetails(
        @Field("gym_id")
        gym_id : String,
        @Field("member_id")
        member_id : String,
        @Field("user_id")
        user_id : String
    ) : Response<MemberPackagesDetailsResponse>

    @FormUrlEncoded//if we use post request
    @POST("getAllGymOwnerGyms")
    suspend fun  getAllGymOwnerGyms(
        @Field("user_id")
        user_id : String
    ) : Response<MyGymsResponse>

    @FormUrlEncoded//if we use post request
    @POST("createPackage")
    suspend fun  createPackage(
        @Field("athorised_user")
        athorised_user : String,
        @Field("gym_id")
        gym_id : String,
        @Field("package_name")
        package_name : String,
        @Field("package_amount")
        package_amount : String,
        @Field("package_duration")
        package_duration : String,
        @Field("package_month_year_status")
        package_month_year_status : String,
        @Field("package_discount")
        package_discount : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("getPackages")
    suspend fun  getPackages(
        @Field("gym_id")
        gym_id : String) : Response<PackagesResponse>

    @FormUrlEncoded//if we use post request
    @POST("createGymMember")
    suspend fun  createGymMember(
        @Field("createGymMemberJsonArr")
        createGymMemberJsonArr : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("getMembers")
    suspend fun  getMembers(
        @Field("gym_id")
        gym_id : String,
        @Field("athorised_user")
        athorised_user : String
    ) : Response<MembersResponse>

    @FormUrlEncoded//if we use post request
    @POST("getGymOwnerHomePageInfo")
    suspend fun  getGymOwnerHomePageInfo(
        @Field("gym_id")
        gym_id : String
    ) : Response<HomeResponse>

    //---------------Super Admin----------------

    @FormUrlEncoded//if we use post request
    @POST("updateProfile")
    suspend fun  updateProfile(
        @Field("user_id")
        user_id : String,
        @Field("user_type")
        user_type : String,
        @Field("name")
        name : String,
        @Field("mobile")
        mobile : String,
        @Field("email")
        email : String,
        @Field("address")
        address : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("updateProfileImage")
    suspend fun  updateProfileImage(
        @Field("user_id")
        user_id : String,
        @Field("user_type")
        user_type : String,
        @Field("image_code")
        image_code : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("getProfile")
    suspend fun  getProfile(
        @Field("user_id")
        user_id : String,
        @Field("user_type")
        user_type : String
    ) : Response<ProfileResponse>

    @FormUrlEncoded//if we use post request
    @POST("createGymOwner")
    suspend fun  createGymOwner(
        @Field("athorised_user")
        athorised_user : String,
        @Field("user_type")
        user_type : String,
        @Field("full_name")
        full_name : String,
        @Field("mobile")
        mobile : String,
        @Field("email")
        email : String,
        @Field("address")
        address : String,
        @Field("username")
        username : String,
        @Field("password")
        password : String,
        @Field("device_id")
        device_id : String
    ) : Response<CommonResponse>



    @POST("getAllGymOwners")
    suspend fun  getAllGymOwners() : Response<GymOwnersResponse>


    @POST("getHomePageInfo")
    suspend fun  getHomePageInfo() : Response<HomePageResponse>


    @POST("getAllGyms")
    suspend fun  getAllGyms() : Response<GymsResponse>

    @FormUrlEncoded//if we use post request
    @POST("userLogin")
    suspend fun  otherUserLogin(
        @Field("username")
        username : String,
        @Field("password")
        password : String
    ) : Response<OtherUserLoginResponse>

    @FormUrlEncoded//if we use post request
    @POST("createGym")
    suspend fun  createGym(
        @Field("athorised_user")
        athorised_user : String,
        @Field("device_id")
        device_id : String,
        @Field("gym_owner_user_id")
        gym_owner_user_id : String,
        @Field("gym_owner_id")
        gym_owner_id : String,
        @Field("gym_name")
        gym_name : String,
        @Field("gym_branch_id")
        gym_branch_id : String,
        @Field("mobile")
        mobile : String,
        @Field("email")
        email : String,
        @Field("address")
        address : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("updateGymDeviceId")
    suspend fun  updateGymDeviceId(
        @Field("gym_id")
        gym_id : String,
        @Field("device_id")
        device_id : String
    ) : Response<CommonResponse>


    @FormUrlEncoded//if we use post request
    @POST("activeDeactiveUser")
    suspend fun  activeDeactiveUser(
        @Field("athorised_user")
        athorised_user : String,
        @Field("user_id")
        user_id : String,
        @Field("active_deactive_status")
        active_deactive_status : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("deleteUser")
    suspend fun  deleteUser(
        @Field("athorised_user")
        athorised_user : String,
        @Field("user_id")
        user_id : String
    ) : Response<CommonResponse>

    @FormUrlEncoded//if we use post request
    @POST("activeDeactiveGym")
    suspend fun  activeDeactiveGym(
        @Field("athorised_user")
        athorised_user : String,
        @Field("gym_id")
        gym_id : String,
        @Field("active_deactive_status")
        active_deactive_status : String
    ) : Response<CommonResponse>

    //for login
    @FormUrlEncoded//if we use post request
    @POST("userLogin")
    suspend fun  userLogin(
        @Field("username")
        email : String,
        @Field("password")
        password : String,
        @Field("gym_branch_id")
        gym_branch_id : String,
        @Field("device_id")
        device_id : String,
        @Field("user_type")
        user_type : String
    ) : Response<GymDeviceAuthResponse>

    //user register

    @FormUrlEncoded//if we use post request
    @POST("userRegister")
    suspend fun  userRegister(
        @Field("gym_name")
        gym_name : String,
        @Field("gym_owner_name")
        gym_owner_name : String,
        @Field("mobile")
        gym_owner_mobile : String,
        @Field("email")
        email : String,
        @Field("username")
        username : String,
        @Field("password")
        password : String,
        @Field("device_id")
        device_id : String,
        @Field("user_type")
        user_type : String
    ) : Response<RegisterResponse>

    //gym_device api calls

    //get member attendance
    @FormUrlEncoded//if we use post request
    @POST("getTodaysAttendance")
    suspend fun  getTodaysAttendance(
        @Field("gym_id")
        gym_id : String,
        @Field("input_date")
        input_date : String,
        @Field("username")
        username : String
    ) : Response<GdTodaysAttendanceResponse>

    //get member attendance
    @FormUrlEncoded//if we use post request
    @POST("logoutApp")
    suspend fun  logoutApp(
        @Field("id")
        id : String
    ) : Response<LogoutAppResponse>

    //register member attendance
    @FormUrlEncoded//if we use post request
        @POST("registerAttendance")
    suspend fun  registerAttendance(
        @Field("qr_code")
        qr_code : String,
        @Field("gym_id")
        gym_id : String,
        @Field("punch_timestamp")
        punch_timestamp : String
    ) : Response<RegisterAttendanceResponse>

    //in out and total count when app first launch
    @FormUrlEncoded//if we use post request
    @POST("getInOutTotalAttendanceCount")
    suspend fun  getInOutTotalAttendanceCount(
        @Field("gym_id")
        member_qrcode_id : String
    ) : Response<InOutTotalAttenResponse>

    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : MyApi{

            val okHttpClient = OkHttpClient.Builder()
                //.certificatePinner(certificatePinner)
                .callTimeout(30,TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                //.baseUrl("http://"+Constants.DOMIAN_PATH+"/api/")//cpanel
                .baseUrl("http://"+Constants.DOMIAN_PATH+"/api/")//cpanel
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}