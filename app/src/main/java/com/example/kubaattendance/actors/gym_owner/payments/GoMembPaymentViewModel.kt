package com.example.kubaattendance.actors.gym_owner.payments

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.actors.gym_owner.payments.interfaces.ICreateMemberPayment
import com.example.kubaattendance.actors.gym_owner.payments.interfaces.IMemberPayments
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class GoMembPaymentViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {


    var iMemberPayments : IMemberPayments? = null
    var iCreateMemberPayment : ICreateMemberPayment? = null


    var gymId : String? = null
    var gymName : String = "Select Gym"

    var memberId : String? = null
    var memberName : String = "Select Member"

    var packageId : String? = null
    var packageName : String? = null
    var pkg_amount : String? = null
    var pkg_from_date : String? = null
    var pkg_to_date : String? = null
    var pkg_duration : String? = null
    var pkg_discount : String? = null

    var totalAmountReceived : String? = null
    var totalPendingAmount : String? = null
    var discountApplied : String? = null

    var remainingAmount : String = "Remaining amount"
    var paiyingAmount : String? = null
    var otherAmount : String? = null
    var remark : String? = null

    fun getGymDetails()
    {
        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }
        iMemberPayments!!.showProgress("Getting Gyms...")
        Coroutines.main {

            try {

                var myGymsResponse = gymOwnerRepository.getAllGymOwnerGyms(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!)

                myGymsResponse.success?.let {

                    iMemberPayments!!.onResponse(myGymsResponse)
                    return@main
                }
                iMemberPayments!!.onFailure(myGymsResponse.message!!)
            }
            catch (e : Exception)
            {
                Log.e(Constants.KUBA_LOGS,"e : "+e)
                iMemberPayments!!.onFailure(e.message!!)
            }
        }
    }
    fun getMembers(gym_id : String) {

        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }

        iMemberPayments!!.showProgress("Getting Members...")
        Coroutines.main {
            try
            {
                var membersResponse = gymOwnerRepository.getMembers(
                    gym_id,
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!
                )
                membersResponse.success?.let {

                    iMemberPayments!!.onResponse(membersResponse)
                    return@main
                }
                iMemberPayments!!.onFailure(membersResponse.message!!)
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }


    }
    fun getPackageAndReceivedAmtDetails(gym_id : String,member_id: String) {

        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }

        iMemberPayments!!.showProgress("Getting Details...")
        Coroutines.main {
            try
            {
                var packageAndReceivedAmtDetailsResponse = gymOwnerRepository.getPackageAndReceivedAmtDetails(
                    gym_id,
                    member_id,
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!
                )
                packageAndReceivedAmtDetailsResponse.success?.let {

                    iMemberPayments!!.onResponse(packageAndReceivedAmtDetailsResponse)
                    return@main
                }
                iMemberPayments!!.onFailure(packageAndReceivedAmtDetailsResponse.message!!)
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }


    }
    fun selectGymButtonClicked(view : View) {
        iMemberPayments!!.selectGymButtonClicked()
    }

    fun selectMemberButtonClicked(view : View) {

        if(gymName.equals("Select Gym"))
        {
            iMemberPayments!!.showToast("Select Gym first")
            return
        }
        iMemberPayments!!.selectMemberButtonClicked()
    }
    fun createMemberPaymentButtonClicked(view : View)
    {
        iMemberPayments!!.createMemberPaymentButtonClicked()
    }

    //create member payment fragment
    fun remainingAmountChecked(view : View)
    {
         iCreateMemberPayment!!.payingAmountRadioChecked("pending")
    }
    fun otherAmountChecked(view : View)
    {
        iCreateMemberPayment!!.payingAmountRadioChecked("other")
    }
    fun submitMemberPaymentFabClicked(view : View)
    {
        if(!T.isNetworkAvailable())
        {
            iCreateMemberPayment!!.showToast(Constants.CONNECTION)
            return
        }
        if(paiyingAmount.isNullOrBlank() || paiyingAmount.isNullOrEmpty())
        {
            iCreateMemberPayment!!.showToast("Enter Paying amount")
            return
        }
        if(remainingAmount.contains("Remaining amount - "))
        {
            if(paiyingAmount!!.toDouble() > remainingAmount.replace("Remaining amount - ","").toDouble())
            {
                iCreateMemberPayment!!.showToast("Paying amount is greater than pending amount")
                return
            }
        }
        if(remainingAmount.isNullOrEmpty() || remainingAmount.isNullOrBlank())
        {
            remainingAmount = 0.0.toString()
            if(paiyingAmount!!.toDouble() > remainingAmount.toDouble())
            {
                iCreateMemberPayment!!.showToast("Paying amount is greater than pending amount")
                return
            }
        }
        if(remark.isNullOrBlank() || paiyingAmount.isNullOrEmpty())
        {
            remark = "Not Available"
        }

        //ready to create member payment

        var memberPaymentArr = JSONArray()
        var memberPaymentObj = JSONObject()

        memberPaymentObj.put("updated_by",MyApplication.prefs.getString(Constants.USER_NAME,"0"))
        memberPaymentObj.put("gym_id",gymId)
        memberPaymentObj.put("member_id",memberId)
        memberPaymentObj.put("package_id",packageId)
        memberPaymentObj.put("package_amount",pkg_amount)
        memberPaymentObj.put("paid_amount",paiyingAmount)
        memberPaymentObj.put("remark",remark)

        memberPaymentArr.put(memberPaymentObj)

        iMemberPayments!!.showProgress("Submiting Payment...")
        Coroutines.main {

            try
            {
                var commonResponse = gymOwnerRepository.createGymMemberPayment(memberPaymentArr.toString())
                commonResponse.success?.let {
                    iMemberPayments!!.onResponse(commonResponse)
                    return@main
                }
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }
    }

    fun getMemberPaymentDetails(gym_id : String,member_id : String)
    {
        Log.e(Constants.KUBA_LOGS,"gym_id : "+gym_id)
        Log.e(Constants.KUBA_LOGS,"member_id : "+member_id)
        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }
        iMemberPayments!!.showProgress("Getting payment details...")
        Coroutines.main {

            try
            {
                var paymentHistoryRsponse = gymOwnerRepository.getMemberPaymentDetails(gym_id,member_id)
                paymentHistoryRsponse.success?.let {
                    iMemberPayments!!.onResponse(paymentHistoryRsponse)
                    return@main
                }
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }
    }
    fun getMemberPackagesDetails(gym_id : String,member_id : String)
    {
        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }
        iMemberPayments!!.showProgress("Getting package details...")
        Coroutines.main {

            try
            {
                var memberPackagesDetailsResponse = gymOwnerRepository.getMemberPackagesDetails(
                    gym_id,
                    member_id,
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!
                )
                memberPackagesDetailsResponse.success?.let {
                    iMemberPayments!!.onResponse(memberPackagesDetailsResponse)
                    return@main
                }
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }
    }
    fun getMemberPackagesPaymentDetails(
        gym_id : String,
        member_id : String,
        pkg_from_date : String,
        pkg_to_date : String
    )
    {


        if(!T.isNetworkAvailable())
        {
            iMemberPayments!!.showToast(Constants.CONNECTION)
            return
        }
        iMemberPayments!!.showProgress("Getting payment details...")
        Coroutines.main {

            try
            {
                var paymentHistoryRsponse = gymOwnerRepository.getMemberPackagesPaymentDetails(
                    gym_id,
                    member_id,
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!,
                    pkg_from_date,
                    pkg_to_date)
                paymentHistoryRsponse.success?.let {
                    iMemberPayments!!.onResponse(paymentHistoryRsponse)
                    return@main
                }
            }
            catch (e : Exception)
            {
                iMemberPayments!!.onFailure(e.message!!)
            }
        }
    }
    fun selectPAckageButtonClicked(v : View) {
        iMemberPayments!!.selectPAckageButtonClicked()
    }
}
