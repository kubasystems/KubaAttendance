package com.example.kubaattendance.actors.gym_owner.members

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.actors.gym_owner.members.data_classes.DMemberInfo
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class GoMembersViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {

    //create gym member fields
    var operation_flag : String = ""
    var gym_name : String = "Select Gym"
    var progressMessage : String = "Please wait..."
    var member_id : String? = null
    var gym_member_user_id : String? = null
    var member_full_name : String? = null
    var member_email : String? = null
    var member_mobile : String? = null
    var member_address : String? = null
    var member_package_plan : String = "Package Plan"
    var member_package_from_date : String = "Package From Date"
    var member_paid_amount : String? = null
    var member_username : String? = null
    var member_password : String? = null
    var member_confirm_password : String? = null

    var gym_id : String? = null
    var package_id : String? = null
    var package_amount : String = "0"
    var package_to_date : String = "0"

    var packageName : String? = null
    var monthOryear : String? = null
    var duration : String? = null

    var iGoMembers : IGoMembers? = null
    var iGoCreateMember : IGoCreateMember? = null

    lateinit var createGymMemberJsonArr : JSONArray
    lateinit var createGymMemberJsonObj : JSONObject

    fun getPackages(gym_id : String) {

        if(!T.isNetworkAvailable())
        {
            iGoCreateMember!!.showToast(Constants.CONNECTION)
            return
        }

        iGoCreateMember!!.showProgress("Getting packages...")
        Coroutines.main {
            try {
                var packagesResponse = gymOwnerRepository.getPackages(gym_id)
                packagesResponse.success?.let {

                    iGoCreateMember!!.onResponse(packagesResponse)
                    return@main
                }
                iGoCreateMember!!.onFailure(packagesResponse.message!!)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }
    fun getGymDetails()
    {
        if(!T.isNetworkAvailable())
        {
            if(operation_flag.equals("iGoMembers"))
                iGoMembers!!.showToast(Constants.CONNECTION)
            else if(operation_flag.equals("iGoCreateMember"))
                iGoCreateMember!!.showToast(Constants.CONNECTION)

            return
        }
        if(operation_flag.equals("iGoMembers"))
            iGoMembers!!.showProgress("Getting Gyms...")
        else if(operation_flag.equals("iGoCreateMember"))
            iGoCreateMember!!.showProgress("Getting Gyms...")
        Coroutines.main {

            try {

                var myGymsResponse = gymOwnerRepository.getAllGymOwnerGyms(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!)

                myGymsResponse.success?.let {

                    if(operation_flag.equals("iGoMembers"))
                        iGoMembers!!.onResponse(myGymsResponse)
                    else if(operation_flag.equals("iGoCreateMember"))
                        iGoCreateMember!!.onResponse(myGymsResponse)

                    return@main
                }
                if(operation_flag.equals("iGoMembers"))
                    iGoMembers!!.onFailure(myGymsResponse.message!!)
                else if(operation_flag.equals("iGoCreateMember"))
                    iGoCreateMember!!.onFailure(myGymsResponse.message!!)


            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                if(operation_flag.equals("iGoMembers"))
                    iGoMembers!!.onFailure(e.message!!)
                else if(operation_flag.equals("iGoCreateMember"))
                    iGoCreateMember!!.onFailure(e.message!!)
            }
        }
    }
    fun getMembers(gym_id : String) {

        Log.e(Constants.KUBA_LOGS,"gym_id : "+gym_id)
        Log.e(Constants.KUBA_LOGS,"USER_NAME : "+MyApplication.prefs.getString(Constants.USER_NAME,"0"))
        if(!T.isNetworkAvailable())
        {
            iGoMembers!!.showToast(Constants.CONNECTION)
            return
        }

        iGoMembers!!.showProgress("Getting Members...")
        Coroutines.main {
            try
            {
                var membersResponse = gymOwnerRepository.getMembers(
                    gym_id,
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!
                )
                membersResponse.success?.let {

                    iGoMembers!!.onResponse(membersResponse)
                    return@main
                }
                iGoMembers!!.onFailure(membersResponse.message!!)
            }
            catch (e : Exception)
            {
                iGoMembers!!.onFailure(e.message!!)
            }
        }


    }

    //create gym member actions
    fun onCreateGymMemberClicked(view : View)
    {
        iGoCreateMember!!.onCreateGymMemberClicked()
    }
    fun onCreateGymMember(flag : String)
    {
        if(!T.isNetworkAvailable())
        {
            iGoCreateMember!!.showToast(Constants.CONNECTION)
            return
        }
        when(flag)
        {
            "create_member" -> {

                progressMessage = "Creating member..."
                //validate fields
                if (operation_flag.equals("iGoCreateMember"))
                {
                    if(gym_name.isNullOrEmpty() || member_full_name.isNullOrBlank())
                    {
                        iGoCreateMember!!.showToast("Select gym first")
                        return
                    }
                }
                if(member_full_name.isNullOrEmpty() || member_full_name.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter full name.")
                    return
                }
                if(member_mobile.isNullOrEmpty() || member_mobile.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter mobile number.")
                    return
                }
                if(!CommonValidation.validateMobileLengh(member_mobile!!))
                    return

                if(member_email.isNullOrEmpty() || member_email.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter email.")
                    return
                }
                if(!CommonValidation.validateEmail(member_email!!))
                    return

                if(member_address.isNullOrEmpty() || member_address.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter address.")
                    return
                }
                if(member_package_plan.equals("Select Package Plan"))
                {
                    iGoCreateMember!!.showToast("Select package plan.")
                    return
                }
                if(member_package_from_date.equals("Select Package From Date"))
                {
                    iGoCreateMember!!.showToast("Select Package from date.")
                    return
                }
                if(member_paid_amount.isNullOrEmpty() || member_paid_amount.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter paid amount.")
                    return
                }
                if(member_username.isNullOrEmpty() || member_username.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter user name name.")
                    return
                }
                if(member_password.isNullOrEmpty() || member_password.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter password.")
                    return
                }
                if(!CommonValidation.validateConfirmPassword(member_password!!,member_confirm_password!!))
                    return

                createGymMemberJsonArr = JSONArray()
                createGymMemberJsonObj = JSONObject()

                createGymMemberJsonObj.put("flag",flag)
                createGymMemberJsonObj.put("athorised_user",MyApplication.prefs.getString(Constants.USER_NAME,"0")!!)
                createGymMemberJsonObj.put("user_type",MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!)
                createGymMemberJsonObj.put("gym_id",gym_id!!)
                createGymMemberJsonObj.put("full_name",member_full_name!!)
                createGymMemberJsonObj.put("mobile",member_mobile!!)
                createGymMemberJsonObj.put("email",member_email!!)
                createGymMemberJsonObj.put("address",member_address!!)
                createGymMemberJsonObj.put("package_id",package_id!!)
                createGymMemberJsonObj.put("package_amount",package_amount!!)
                createGymMemberJsonObj.put("package_from_date",member_package_from_date!!)
                createGymMemberJsonObj.put("package_to_date",package_to_date!!)
                createGymMemberJsonObj.put("paid_amount",member_paid_amount!!)
                createGymMemberJsonObj.put("username",member_username!!)
                createGymMemberJsonObj.put("password",member_password!!)

                createGymMemberJsonArr.put(createGymMemberJsonObj)
            }
            "edit_info" -> {

                progressMessage = "Updating info..."
                if(member_full_name.isNullOrEmpty() || member_full_name.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter full name.")
                    return
                }
                if(member_mobile.isNullOrEmpty() || member_mobile.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter mobile number.")
                    return
                }
                if(!CommonValidation.validateMobileLengh(member_mobile!!))
                    return

                if(member_email.isNullOrEmpty() || member_email.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter email.")
                    return
                }
                if(!CommonValidation.validateEmail(member_email!!))
                    return

                if(member_address.isNullOrEmpty() || member_address.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter address.")
                    return
                }

                createGymMemberJsonArr = JSONArray()
                createGymMemberJsonObj = JSONObject()

                createGymMemberJsonObj.put("flag",flag)
                createGymMemberJsonObj.put("athorised_user",MyApplication.prefs.getString(Constants.USER_NAME,"0")!!)
                createGymMemberJsonObj.put("user_type",MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!)
                createGymMemberJsonObj.put("gym_id",gym_id!!)
                createGymMemberJsonObj.put("gym_member_user_id",gym_member_user_id!!)
                createGymMemberJsonObj.put("full_name",member_full_name!!)
                createGymMemberJsonObj.put("mobile",member_mobile!!)
                createGymMemberJsonObj.put("email",member_email!!)
                createGymMemberJsonObj.put("address",member_address!!)

                createGymMemberJsonArr.put(createGymMemberJsonObj)
            }
            "assign_new_pkg" -> {

                progressMessage = "Assigning Package..."
                if(member_full_name.isNullOrEmpty() || member_full_name.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter full name.")
                    return
                }
                if(member_package_plan.equals("Select Package Plan"))
                {
                    iGoCreateMember!!.showToast("Select package plan.")
                    return
                }
                if(member_package_from_date.equals("Select Package From Date"))
                {
                    iGoCreateMember!!.showToast("Select Package from date.")
                    return
                }
                if(member_paid_amount.isNullOrEmpty() || member_paid_amount.isNullOrBlank())
                {
                    iGoCreateMember!!.showToast("Enter paid amount.")
                    return
                }

                createGymMemberJsonArr = JSONArray()
                createGymMemberJsonObj = JSONObject()

                createGymMemberJsonObj.put("flag",flag)
                createGymMemberJsonObj.put("athorised_user",MyApplication.prefs.getString(Constants.USER_NAME,"0")!!)
                createGymMemberJsonObj.put("user_type",MyApplication.prefs.getString(Constants.USER_TYPE,"0")!!)
                createGymMemberJsonObj.put("gym_id",gym_id!!)
                createGymMemberJsonObj.put("member_id",member_id!!)
                createGymMemberJsonObj.put("full_name",member_full_name!!)
                createGymMemberJsonObj.put("package_id",package_id!!)
                createGymMemberJsonObj.put("package_amount",package_amount!!)
                createGymMemberJsonObj.put("package_from_date",member_package_from_date!!)
                createGymMemberJsonObj.put("package_to_date",package_to_date!!)
                createGymMemberJsonObj.put("paid_amount",member_paid_amount!!)

                createGymMemberJsonArr.put(createGymMemberJsonObj)
            }
            else -> {

            }

        }



        iGoCreateMember!!.showProgress(progressMessage)
        Coroutines.main {

            Log.e(Constants.KUBA_LOGS,createGymMemberJsonArr.toString())
            try
            {
                var commonResponse = gymOwnerRepository.createGymMember(createGymMemberJsonArr.toString())
                commonResponse.success?.let {

                    iGoCreateMember!!.onResponse(commonResponse)
                    return@main
                }
                iGoCreateMember!!.onFailure(commonResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iGoCreateMember!!.onFailure(e.message!!)
            }
        }
    }

    fun onSelectGymButtonClicked(view : View)
    {
        if(operation_flag.equals("iGoMembers"))
            iGoMembers!!.selectGymButtonClicked()
        else if(operation_flag.equals("iGoCreateMember"))
            iGoCreateMember!!.selectGymButtonClicked()
    }
    fun onSelectPackageFromDate(view : View)
    {
        if(member_package_plan.equals("Package Plan"))
        {
            iGoCreateMember!!.showToast("Select package plan first")
            return
        }
        iGoCreateMember!!.selectPackageFromDateButtonClicked()
    }
    fun onSelectPackageButtonClicked(view : View)
    {
        iGoCreateMember!!.onSelectPackageButtonClicked()
    }
    fun createMemberFabButtonClicked(view : View)
    {

        if(gym_name.equals("Select Gym"))
        {
            iGoMembers!!.showToast("Select Gym first")
            return
        }
        iGoMembers!!.createMemberFabButtonClicked()
    }
    fun onActivateDeactivateGymOwnerBtnClicked(gym_member_user_id: String, is_active: String)
    {
        iGoMembers!!.onActivateDeactivateGymOwnerBtnClicked(gym_member_user_id,is_active)
    }
    fun activateDeactivateGymOwner(gym_member_user_id : String,is_active : String)
    {
        if(!T.isNetworkAvailable())
        {
            iGoMembers!!.showToast(Constants.CONNECTION)
            return
        }
        iGoMembers!!.showProgress("Updating status...")
        Coroutines.main {

            try
            {
                var commonResponse = gymOwnerRepository.activeDeactiveUser(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    gym_member_user_id,
                    is_active)
                commonResponse.success?.let {

                    iGoMembers!!.onResponse(commonResponse)
                    return@main
                }
                iGoMembers!!.onFailure(commonResponse.message!!)

            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iGoMembers!!.showToast(e.message!!)
            }
        }
    }
    fun menuMembersOptionClicked(v : View,dMemberInfo : DMemberInfo)
    {
        iGoMembers!!.menuMembersOptionClicked(v,dMemberInfo)
    }
}
