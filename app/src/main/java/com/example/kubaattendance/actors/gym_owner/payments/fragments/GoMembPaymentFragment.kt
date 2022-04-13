package com.example.kubaattendance.actors.gym_owner.payments.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.payments.GoMembPaymentViewModel
import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages
import com.example.kubaattendance.actors.gym_owner.payments.PaymentHistoryInfo
import com.example.kubaattendance.actors.gym_owner.payments.PaymentHistoryItem
import com.example.kubaattendance.actors.gym_owner.payments.interfaces.IMemberPayments
import com.example.kubaattendance.actors.gym_owner.payments.payment_history.PackageHistoryDataHolder
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.gym_owner.MemberPackagesDetailsResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MembersResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.data.network.responses.gym_owner.PaymentHistoryResponse
import com.example.kubaattendance.databinding.GoMembPaymentFragmentBinding
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.example.kubaattendance.utils.toast
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.go_memb_payment_fragment.*

class GoMembPaymentFragment : BundleFragment(), IMemberPayments
{

    private lateinit var binding: GoMembPaymentFragmentBinding
    private lateinit var viewModel: GoMembPaymentViewModel
    private lateinit var paymentResult: PaymentHistoryResponse
    var gym_idd = ""
    var member_idd = ""

    var member_gyms =  ArrayList<DOwnerInfo>()
    var gym_members =  ArrayList<DOwnerInfo>()

    lateinit var customSpinnerDataHolderGyms: CustomSpinnerDataHolder
    lateinit var customSpinnerDataHolderMembers: CustomSpinnerDataHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding  = DataBindingUtil.inflate(inflater,R.layout.go_memb_payment_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.goMembPaymentViewModelFactory).get(GoMembPaymentViewModel::class.java)
        binding.goMembPaymentViewModel = viewModel
        viewModel.iMemberPayments = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(gym_members.size > 0)
        {
            select_member_btn.visibility = View.VISIBLE
            setPaymentDetails(paymentResult)
        }
        else
        //get gym details
            viewModel.getGymDetails()
    }
    override fun showProgress() {

    }
    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }
    override fun onResponse(result: Any) {
        progress_layout.visibility = View.GONE

        if(result is MyGymsResponse)
        {
            if(result.success.equals("1"))
            {
                member_gyms.clear()
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
            {
                activity!!.toast(result.message!!)
                select_member_btn.visibility = View.GONE
                package_details_card.visibility = View.GONE
                payment_history_details_card.visibility = View.GONE
            }



        }
        else if(result is MembersResponse)
        {
            if(result.success.equals("1"))
            {
                gym_members.clear()
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
                viewModel.memberName = "Select Member"
                binding.goMembPaymentViewModel = viewModel

                activity!!.toast(result.message!!)
                select_member_btn.visibility = View.GONE
                package_details_card.visibility = View.GONE
                payment_history_details_card.visibility = View.GONE
            }



        }
        else if(result is PaymentHistoryResponse)
        {
            paymentResult = result
            setPaymentDetails(result)
        }
        else if(result is MemberPackagesDetailsResponse)
        {
            if(result.success.equals("1"))
            {
                var packageHistoryDataHolder = PackageHistoryDataHolder(result.member_packages as ArrayList<MemberPackages>)
                var action = GoMembPaymentFragmentDirections.actionOpenPackageHistoryFragment()
                action.setPackageHistoryDataHolder(packageHistoryDataHolder)
                navigate(action,856)
            }
            else
                activity!!.toast(result.message!!)

        }
    }
    fun setPaymentDetails(result : PaymentHistoryResponse)
    {
        viewModel.packageName = null
        viewModel.pkg_amount = null
        viewModel.pkg_from_date = null
        viewModel.pkg_to_date = null
        viewModel.pkg_duration = null
        viewModel.pkg_discount = null
        viewModel.discountApplied = null
        viewModel.totalAmountReceived = null
        viewModel.totalPendingAmount = null

        if(result.success.equals("1"))
        {
            if(result.running_pkginfo.size > 0)
            {
                package_details_card.visibility = View.VISIBLE

                //set running package details info
                var running_pkginfo = result.running_pkginfo.get(0)
                viewModel.packageName = running_pkginfo.pkg_name
                viewModel.pkg_amount = running_pkginfo.pkg_amount
                viewModel.pkg_from_date = T.getFormatedDate(running_pkginfo.pkg_from_date!!)
                viewModel.pkg_to_date = T.getFormatedDate(running_pkginfo.pkg_to_date!!)
                if(running_pkginfo.pkg_monthOryear.equals("y"))
                    viewModel.pkg_duration = running_pkginfo.pkg_duration+" Year"
                else if(running_pkginfo.pkg_monthOryear.equals("m"))
                    viewModel.pkg_duration = running_pkginfo.pkg_duration+" Month"
                viewModel.pkg_discount = running_pkginfo.pkg_discount

                if(viewModel.pkg_discount!!.toDouble() > 0)
                {
                    discount_applied_li.visibility = View.VISIBLE
                    //calculate discount on total amount received
                    var discount_applied = (running_pkginfo.pkg_amount!!.toDouble() / 100) * running_pkginfo.pkg_discount!!.toDouble()
                    viewModel.discountApplied = discount_applied.toString()
                    viewModel.totalAmountReceived = ""+result.total_amt_received!!.toDouble()
                    //calculate total pending amount
                    var totalPendingAmt = (running_pkginfo.pkg_amount!!.toDouble() - viewModel.totalAmountReceived!!.toDouble()) - discount_applied
                    viewModel.totalPendingAmount = totalPendingAmt.toString()
                    binding.goMembPaymentViewModel = viewModel
                }
                else
                {
                    viewModel.totalAmountReceived = result.total_amt_received
                    //calculate total pending amount
                    var totalPendingAmt = running_pkginfo.pkg_amount!!.toDouble() - viewModel.totalAmountReceived!!.toDouble()
                    viewModel.totalPendingAmount = totalPendingAmt.toString()
                    binding.goMembPaymentViewModel = viewModel
                }

            }
            else
            {
                package_details_card.visibility = View.GONE
                activity!!.toast("Oops ! Package details not available")
            }



            //set the payment history info
            if(result.received_amt_history.size > 0)
            {

                payment_history_details_card.visibility = View.VISIBLE
                initiRecyclerView(result.received_amt_history.toQuoteItem())
            }
            else
            {
                payment_history_details_card.visibility = View.GONE
                activity!!.toast("Oops ! payment history details not available")
            }

        }
        else
            activity!!.toast(result.message!!)
    }


    fun initiRecyclerView(pkgInfoItem: List<PaymentHistoryItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(pkgInfoItem)
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }
    private fun List<PaymentHistoryInfo>.toQuoteItem() : List<PaymentHistoryItem>{
        return this.map {
            PaymentHistoryItem(it)
        }
    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }
    override fun showProgress(message: String) {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }

    override fun selectGymButtonClicked() {

        if(customSpinnerDataHolderGyms.data.size > 0)
        {
            var action = GoMembPaymentFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolderGyms)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gym owners not found")
    }
    override fun selectMemberButtonClicked() {

        if(customSpinnerDataHolderMembers.data.size > 0)
        {
            var action = GoMembPaymentFragmentDirections.customSpinnerData()
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
                        //get payment details
                        viewModel.getMemberPaymentDetails(gym_idd,member_idd)
                    }
                }

            }
        }
        else if(requestCode == 856)
        {
            if(bundle != null)
            {
                var flag = bundle.getString("flag")

                if(flag.equals("set"))
                {
                   // var package_id = bundle.getString("package_id")
                   // var pkg_name = bundle.getString("pkg_name")
                   // var pkg_amount = bundle.getString("pkg_amount")
                    var pkg_from_date = bundle.getString("pkg_from_date")
                    var pkg_to_date = bundle.getString("pkg_to_date")

                    viewModel.getMemberPackagesPaymentDetails(
                        gym_idd,
                        member_idd,
                        pkg_from_date!!,
                        pkg_to_date!!)
                }

            }

        }
        else if(requestCode == 856)
        {
            if(bundle != null)
            {
                viewModel.getMemberPaymentDetails(gym_idd,member_idd)
            }
        }
    }
    override fun createMemberPaymentButtonClicked() {
        navigate(GoMembPaymentFragmentDirections.actionCreateMemberPayment(),867)
    }
    override fun selectPAckageButtonClicked() {

        //get payment details
        viewModel.getMemberPackagesDetails(gym_idd,member_idd)


    }
}
