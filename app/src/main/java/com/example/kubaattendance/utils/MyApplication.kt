package com.example.kubaattendance.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.kubaattendance.actors.gym_device.GymDeviceViewModelFactory
import com.example.kubaattendance.actors.gym_owner.attendance.MemberAttendanceViewModelFactory
import com.example.kubaattendance.actors.gym_owner.pkg.PkgViewModelFactory
import com.example.kubaattendance.actors.gym_owner.home.GoHomeViewModelFactory
import com.example.kubaattendance.actors.gym_owner.id_card.MemberIdCardViewModelFactory
import com.example.kubaattendance.actors.gym_owner.members.GoMembersViewModelFactory
import com.example.kubaattendance.actors.gym_owner.my_gyms.MyGymsViewModelFactory
import com.example.kubaattendance.actors.gym_owner.payments.GoMembPaymentViewModelFactory
import com.example.kubaattendance.actors.super_admin.gym_owner.SaGymOwnersFragmentViewModelFactory
import com.example.kubaattendance.actors.super_admin.gym_owner.create.CreateGymOwnerViewModelFactory
import com.example.kubaattendance.actors.super_admin.gyms.SaGymsFragmentViewModelFactory
import com.example.kubaattendance.actors.super_admin.gyms.create.CreateGymViewModelFactory
import com.example.kubaattendance.actors.super_admin.homepage.SaHomeFragmentViewModelFactory
import com.example.kubaattendance.authentication.AuthViewModelFactory
import com.example.kubaattendance.data.network.MyApi
import com.example.kubaattendance.data.repositories.AuthRepository
import com.example.kubaattendance.data.repositories.GymDeviceRepository
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.data.repositories.SuperAdminRepository
import com.example.kubaattendance.logout.LogoutViewModelFactory
import com.example.kubaattendance.profile.ProfileViewModelFactory
import com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network.NetworkConnectionInterceptor

class MyApplication : Application()
{

    companion object
    {
        lateinit var context: Context
        lateinit var prefs : SharedPreferences
        lateinit var editor : SharedPreferences.Editor

        lateinit var authViewModelFactory: AuthViewModelFactory
        lateinit var gymDeviceViewModelFactory: GymDeviceViewModelFactory
        lateinit var userAuthRepository: AuthRepository
        lateinit var gymDeviceRepository: GymDeviceRepository


        lateinit var superAdminRepository: SuperAdminRepository
        lateinit var saHomeFragmentViewModelFactory: SaHomeFragmentViewModelFactory
        lateinit var saGymDeviceViewModelFactory: SaGymsFragmentViewModelFactory
        lateinit var saGymOwnersFragmentViewModelFactory: SaGymOwnersFragmentViewModelFactory
        lateinit var createGymOwnerViewModelFactory: CreateGymOwnerViewModelFactory
        lateinit var createGymViewModelFactory: CreateGymViewModelFactory

        lateinit var gymOwnerRepository: GymOwnerRepository
        lateinit var myGymsViewModelFactory: MyGymsViewModelFactory
        lateinit var memberIdCardViewModelFactory: MemberIdCardViewModelFactory

        lateinit var profileViewModelFactory: ProfileViewModelFactory
        lateinit var logoutViewModelFactory: LogoutViewModelFactory
        lateinit var goHomeViewModelFactory: GoHomeViewModelFactory

        //gym member
        lateinit var memberAttendanceViewModelFactory: MemberAttendanceViewModelFactory
        lateinit var goMembPaymentViewModelFactory: GoMembPaymentViewModelFactory
        lateinit var goMembersViewModelFactory: GoMembersViewModelFactory
        lateinit var pkgViewModelFactory: PkgViewModelFactory

        lateinit var myApi: MyApi
        lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor
    }
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        //shared preferences
        prefs = getSharedPreferences(Constants.PREF_KEY,0)
        editor = prefs.edit()
        editor.commit()

        networkConnectionInterceptor = NetworkConnectionInterceptor()
        myApi = MyApi(networkConnectionInterceptor)
        userAuthRepository = AuthRepository(myApi)
        gymDeviceRepository = GymDeviceRepository(myApi)
        authViewModelFactory = AuthViewModelFactory(userAuthRepository)
        gymDeviceViewModelFactory = GymDeviceViewModelFactory(gymDeviceRepository)

        superAdminRepository = SuperAdminRepository(myApi)
        saHomeFragmentViewModelFactory = SaHomeFragmentViewModelFactory(superAdminRepository)
        saGymDeviceViewModelFactory = SaGymsFragmentViewModelFactory(superAdminRepository)
        saGymOwnersFragmentViewModelFactory = SaGymOwnersFragmentViewModelFactory(superAdminRepository)
        createGymOwnerViewModelFactory = CreateGymOwnerViewModelFactory(superAdminRepository)
        createGymViewModelFactory = CreateGymViewModelFactory(superAdminRepository)

        profileViewModelFactory = ProfileViewModelFactory(userAuthRepository)
        logoutViewModelFactory = LogoutViewModelFactory()

        gymOwnerRepository = GymOwnerRepository(myApi)
        goHomeViewModelFactory = GoHomeViewModelFactory(gymOwnerRepository)
        memberAttendanceViewModelFactory = MemberAttendanceViewModelFactory(gymOwnerRepository)
        goMembPaymentViewModelFactory = GoMembPaymentViewModelFactory(gymOwnerRepository)
        goMembersViewModelFactory = GoMembersViewModelFactory(gymOwnerRepository)
        pkgViewModelFactory = PkgViewModelFactory(gymOwnerRepository)
        myGymsViewModelFactory = MyGymsViewModelFactory(gymOwnerRepository)
        memberIdCardViewModelFactory = MemberIdCardViewModelFactory(gymOwnerRepository)




    }

}