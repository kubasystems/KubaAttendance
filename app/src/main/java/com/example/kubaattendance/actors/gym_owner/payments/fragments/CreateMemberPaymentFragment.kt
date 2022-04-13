package com.example.kubaattendance.actors.gym_owner.payments.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.payments.GoMembPaymentViewModel
import com.example.kubaattendance.actors.gym_owner.payments.interfaces.ICreateMemberPayment
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MembersResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.data.network.responses.gym_owner.PackageAndReceivedAmtDetailsResponse
import com.example.kubaattendance.databinding.LayoutCreateMemberPaymentFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.layout_create_member_payment_fragment.*

class CreateMemberPaymentFragment : BundleFragment(),ICreateMemberPayment{
    override fun selectPAckageButtonClicked() {

    }

    lateinit var binding : LayoutCreateMemberPaymentFragmentBinding
    lateinit var viewModel : GoMembPaymentViewModel

    var gym_idd = ""
    var member_idd = ""

    var member_gyms =  ArrayList<DOwnerInfo>()
    var gym_members =  ArrayList<DOwnerInfo>()

    lateinit var customSpinnerDataHolderGyms: CustomSpinnerDataHolder
    lateinit var customSpinnerDataHolderMembers: CustomSpinnerDataHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.layout_create_member_payment_fragment,container,false)
        viewModel = ViewModelProviders.of(this,MyApplication.goMembPaymentViewModelFactory).get(
            GoMembPaymentViewModel::class.java)
        binding.goMembPaymentViewModel = viewModel
        viewModel.iCreateMemberPayment = this
        viewModel.iMemberPayments = this

        //setSpiner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get gym details
        viewModel.getGymDetails()
    }
    override fun payingAmountRadioChecked(amountType: String) {

        when(amountType)
        {
            "pending"->{
                radioButton_pending_amt.setChecked(true)
                radioButton_other_amt.setChecked(false)
                viewModel.remainingAmount = viewModel.remainingAmount
                viewModel.otherAmount = "Other"
                viewModel.paiyingAmount = viewModel.remainingAmount.replace("Remaining amount - ","")

                binding.goMembPaymentViewModel = viewModel

            }
            "other"->{
                radioButton_pending_amt.setChecked(false)
                radioButton_other_amt.setChecked(true)
                viewModel.remainingAmount = viewModel.remainingAmount
                viewModel.otherAmount = "Other"
                viewModel.paiyingAmount = ""

                binding.goMembPaymentViewModel = viewModel
            }
            else->{

            }
        }

    }
    override fun showProgress(message: String) {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }

    override fun showProgress() {

    }

    override fun selectGymButtonClicked() {
        //setSpiner()
        if(customSpinnerDataHolderGyms.data.size > 0)
        {
            var action  = CreateMemberPaymentFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolderGyms)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gym owners not found")
    }

    override fun selectMemberButtonClicked() {

        if(customSpinnerDataHolderMembers.data.size > 0)
        {
            var action  = CreateMemberPaymentFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolderMembers)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gym members not found")
    }
    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)

        if(requestCode == 855)
        {
            if(bundle != null)
            {
                var flag = bundle.getString("flag")

                if(flag.equals("set"))
                {
                    var gym_owner_id = bundle.getString("gym_owner_id")
                    var gym_owner_name = bundle.getString("gym_owner_name")
                    var gymsMembersFlag = bundle.getString("gym_owner_mobile")


                    if(gymsMembersFlag.equals("gyms"))
                    {
                        gym_idd = gym_owner_id!!
                        viewModel.gymId = gym_owner_id
                        viewModel.gymName = gym_owner_name!!
                        binding.goMembPaymentViewModel = viewModel
                        //get gym members of selected gyms
                        viewModel.getMembers(gym_owner_id)
                    }
                    else if(gymsMembersFlag.equals("members"))
                    {
                        member_idd = gym_owner_id!!
                        viewModel.memberId = gym_owner_id
                        viewModel.memberName = gym_owner_name!!
                        binding.goMembPaymentViewModel = viewModel

                        viewModel.getPackageAndReceivedAmtDetails(gym_idd,member_idd)

                    }
                }

            }
        }
    }


    override fun createMemberPaymentButtonClicked() {

    }

    override fun hideProgress() {

    }

    override fun onResponse(result: Any) {
        progress_layout.visibility = View.GONE

        if(result is MyGymsResponse)
        {
            member_gyms.clear()
            if(result.success.equals("1"))
            {



                for (i in 0 until result.gyms.size)
                {
                    var gym = result.gyms.get(i)
                    member_gyms.add(
                        DOwnerInfo(
                            "",
                            gym.gym_id,
                            "",
                            gym.gym_name,
                            "gyms",
                            "")
                    )


                }
                customSpinnerDataHolderGyms = CustomSpinnerDataHolder(member_gyms)
            }
            else
                activity!!.toast(result.message!!)


        }
        else if(result is MembersResponse)
        {
            gym_members.clear()
            if(result.success.equals("1"))
            {
                if(result.members.size > 0)
                {
                    select_member_btn.visibility = View.VISIBLE
                    for (i in 0 until result.members.size)
                    {
                        var gym = result.members.get(i)
                        gym_members.add(
                            DOwnerInfo(
                                "",
                                gym.gym_member_id,
                                "",
                                gym.gym_member_name,
                                "members",
                                "")
                        )


                    }
                    customSpinnerDataHolderMembers = CustomSpinnerDataHolder(gym_members)
                }
                else
                {
                    select_member_btn.visibility = View.GONE
                    activity!!.toast("Oops ! Selected gym members not available")
                }

            }
            else
            {
                activity!!.toast(result.message!!)
                select_member_btn.visibility = View.GONE
                package_details_card.visibility = View.GONE
                paiying_amt_details_card.visibility = View.GONE
            }



        }
        else if(result is PackageAndReceivedAmtDetailsResponse)
        {
            if(result.success.equals("1"))
            {
                package_details_card.visibility = View.VISIBLE
                paiying_amt_details_card.visibility = View.VISIBLE
                viewModel.packageName = "Running Package : "+result.pkg_name
                viewModel.packageId = result.package_id
                viewModel.pkg_amount = result.pkg_amount
                viewModel.totalAmountReceived = "Total Amount Received : "+result.total_amt_received
                var pendingAmount = result.pkg_amount!!.toDouble() - result.total_amt_received!!.toDouble()
                viewModel.remainingAmount = "Remaining amount - "+pendingAmount

                if(pendingAmount > 0)
                {
                    viewModel.otherAmount = pendingAmount.toString()
                    payingAmountRadioChecked("pending")
                }
                else
                {
                    viewModel.otherAmount = ""
                    payingAmountRadioChecked("other")
                }


                binding.goMembPaymentViewModel = viewModel
            }
            else
            {
                activity!!.toast(result.message!!)
                package_details_card.visibility = View.GONE
                paiying_amt_details_card.visibility = View.GONE
            }

        }
        else if(result is CommonResponse)
        {
            T.displaySuccessFailureDialog(
                activity!!,
                object : ISweetDialog{
                    override fun onConfirmClickListener() {
                        navigateUp(867,Bundle().also {

                        })
                    }

                    override fun onCancelClickListener() {
                        navigateUp(867,Bundle().also {

                        })
                    }

                },
                Constants.SA_SUCCESS,
                "Success",
                result.message!!,
                "Ok",
                "Cancel"
            )

        }

    }

    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)

    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }


}