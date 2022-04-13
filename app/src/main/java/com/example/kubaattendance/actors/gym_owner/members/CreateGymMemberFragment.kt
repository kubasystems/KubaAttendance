package com.example.kubaattendance.actors.gym_owner.members

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.data.network.responses.gym_owner.PackagesResponse
import com.example.kubaattendance.databinding.CreateGymMemberFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.activity_go_home.*
import kotlinx.android.synthetic.main.create_gym_member_fragment.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class CreateGymMemberFragment : BundleFragment(),IGoCreateMember{

    private lateinit var viewModel: GoMembersViewModel
    private lateinit var binding: CreateGymMemberFragmentBinding
    private lateinit var flag: String

    var gym_names =  ArrayList<DOwnerInfo>()
    var package_names =  ArrayList<DOwnerInfo>()

    private var passingData : String? = null
    lateinit var customSpinnerDataHolderPackage: CustomSpinnerDataHolder
    lateinit var customSpinnerDataHolderGyms: CustomSpinnerDataHolder


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.create_gym_member_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.goMembersViewModelFactory).get(GoMembersViewModel::class.java)
        binding.goMembersViewModel = viewModel
        viewModel.iGoCreateMember = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments!!.let {
            passingData = CreateGymMemberFragmentArgs.fromBundle(it).passingData
            if(!passingData.equals("0"))
            {
                var receivedData = passingData!!.split("#")

                flag = receivedData.get(0)

                var gym_id = receivedData.get(1)
                var gym_name = receivedData.get(2)

                viewModel.gym_id = gym_id!!
                viewModel.gym_name = gym_name!!
                viewModel.operation_flag = "iGoMembers"
                binding.goMembersViewModel = viewModel
                viewModel.iGoCreateMember = this

                viewModel.getPackages(viewModel.gym_id!!)

            }
        }
        try
        {
            var receivedData = passingData!!.split("#")
            gym_name_til.visibility = View.VISIBLE
            select_gym_cv.visibility = View.GONE
            when(flag)
            {
                "create_member" -> {
                    personal_details_card.visibility = View.VISIBLE
                    package_details_card.visibility = View.VISIBLE
                    login_details_card.visibility = View.VISIBLE
                    activity!!.toolBar.title = "Create New Member"
                    gym_name_et.keyListener = null
                }
                "edit_info" -> {
                    personal_details_card.visibility = View.VISIBLE
                    package_details_card.visibility = View.GONE
                    login_details_card.visibility = View.GONE
                    gym_name_et.keyListener = null
                    activity!!.toolBar.title = "Edit Member Info"

                    viewModel.gym_member_user_id = receivedData.get(3)
                    viewModel.member_full_name = receivedData.get(4)
                    viewModel.member_mobile = receivedData.get(5)
                    viewModel.member_email = receivedData.get(6)
                    viewModel.member_address = receivedData.get(7)

                    binding.goMembersViewModel = viewModel
                }
                "assign_new_pkg" -> {
                    personal_details_card.visibility = View.VISIBLE
                    gym_name_et.keyListener = null
                    fName_et.keyListener = null
                    activity!!.toolBar.title = "Assign New Package"

                    viewModel.member_id = receivedData.get(3)
                    viewModel.member_full_name = receivedData.get(5)
                    viewModel.gym_member_user_id = receivedData.get(4)
                    binding.goMembersViewModel = viewModel

                    mobile_ti.visibility = View.GONE
                    email_ti.visibility = View.GONE
                    address_ti.visibility = View.GONE

                    package_details_card.visibility = View.VISIBLE
                    login_details_card.visibility = View.GONE
                }
                else -> {

                }

            }
        }
        catch (e : UninitializedPropertyAccessException)
        {
            flag = "create_member"
            gym_name_til.visibility = View.GONE
            select_gym_cv.visibility = View.VISIBLE
            viewModel.operation_flag = "iGoCreateMember"
            viewModel.getGymDetails()
        }

    }
    override fun onCreateGymMemberClicked() {
        viewModel.onCreateGymMember(flag)
    }
    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Creating Gym Member, please wait...")
    }
    override fun hideProgress() {
    }
    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE

        if(data is CommonResponse)
        {
            if(data.success.equals("1"))
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog {
                        override fun onConfirmClickListener() {

                            navigateUp(856,Bundle().also {
                                it.putString("refresh_flag","1")
                                it.putString("gym_id",viewModel.gym_id)
                            })
                        }

                        override fun onCancelClickListener() {
                            navigateUp(856,Bundle().also {
                                it.putString("refresh_flag","1")
                                it.putString("gym_id",viewModel.gym_id)
                            })
                        }

                    },
                    Constants.SA_SUCCESS,
                    "Success",
                    data.message!!,
                    "Ok",
                    "Cancel")
            }
            else
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog {
                        override fun onConfirmClickListener() {

                        }

                        override fun onCancelClickListener() {

                        }

                    },
                    Constants.SA_ERROR,
                    "Fail",
                    data.message!!,
                    "Ok",
                    "Cancel")
            }
        }
        else if(data is PackagesResponse)
        {
            package_names.clear()
            if(data.success.equals("1"))
            {
                for (i in 0 until data.pkg_info.size)
                {
                    var packageD = data.pkg_info.get(i)
                    package_names.add(DOwnerInfo(
                        "",
                        packageD.id,
                        packageD.amount,
                        packageD.name,
                        packageD.monthOryear,
                        packageD.duration))
                }
            }


            customSpinnerDataHolderPackage = CustomSpinnerDataHolder(package_names)
        }
        else if(data is MyGymsResponse)
        {
            gym_names.clear()
            if(data.success.equals("1"))
            {
                for (i in 0 until data.gyms.size)
                {
                    var gym = data.gyms.get(i)
                    gym_names.add(DOwnerInfo(
                        "",
                        gym.gym_id,
                        "",
                        gym.gym_name,
                        "",
                        ""))
                }
                customSpinnerDataHolderGyms = CustomSpinnerDataHolder(gym_names)
            }
            else
                activity!!.toast(data.message!!)
        }
    }
    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {

        gym_names.clear()
        package_names.clear()
        progress_layout.visibility = View.GONE
        activity!!.toast(message)

    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun onSelectPackageButtonClicked() {

        try
        {
            if(customSpinnerDataHolderPackage.data.size > 0)
            {
                var action = CreateGymMemberFragmentDirections.customSpinnerData()
                action.setCustomSpinnerDataHolder(customSpinnerDataHolderPackage)
                navigate(action,855)
            }
            else
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog {
                        override fun onConfirmClickListener() {

                        }

                        override fun onCancelClickListener() {

                        }

                    },
                    Constants.SA_WARNNING,
                    "Package",
                    "Package details not found",
                    "Ok",
                    "Cancel")
            }
        }
        catch (e : UninitializedPropertyAccessException)
        {
            activity!!.toast("Package not found, Select gym first")
        }

    }
    override fun selectGymButtonClicked() {
        if(customSpinnerDataHolderGyms.data.size > 0)
        {
            var action = GoMembersFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolderGyms)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gym owners not found")
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
                    //var gym_owner_photo = bundle.getString("gym_owner_photo")
                    var gym_owner_id = bundle.getString("gym_owner_id")
                    var gym_owners_user_id = bundle.getString("gym_owners_user_id")
                    var gym_owner_name = bundle.getString("gym_owner_name")
                    var gym_owner_mobile = bundle.getString("gym_owner_mobile")
                    var gym_owner_email = bundle.getString("gym_owner_email")

                    if(gym_owner_mobile.isNullOrBlank() || gym_owner_mobile.isNullOrEmpty())
                    {
                        viewModel.gym_id = gym_owner_id
                        viewModel.gym_name = gym_owner_name!!
                        binding.goMembersViewModel = viewModel
                        viewModel.getPackages(viewModel.gym_id!!)
                    }
                    else
                    {
                        if(gym_owners_user_id.isNullOrEmpty())
                            gym_owners_user_id = "0"

                        viewModel.package_id = gym_owner_id
                        viewModel.package_amount = gym_owners_user_id
                        viewModel.member_package_plan = gym_owner_name!!

                        viewModel.packageName = gym_owner_name
                        viewModel.monthOryear = gym_owner_mobile
                        viewModel.duration = gym_owner_email

                        binding.goMembersViewModel = viewModel
                    }


                }

            }
        }
    }

    lateinit var dayData : String
    lateinit var monthData : String
    lateinit var package_to_date : String
    override fun selectPackageFromDateButtonClicked() {

        val c = Calendar.getInstance()
        val yeard = c.get(Calendar.YEAR)
        val monthd = c.get(Calendar.MONTH)
        val dayd = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            //lblDate.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
            var month = monthOfYear+1

            if(dayOfMonth.toString().length == 1)
                dayData = "0"+dayOfMonth
            else
                dayData = ""+dayOfMonth

            if(month.toString().length == 1)
                monthData = "0"+month
            else
                monthData = ""+month


            var dateResult = ""+year+"-"+monthData+"-"+dayData


            if(viewModel.monthOryear.equals("m"))
            {
                var noOfdays = 30 * viewModel.duration!!.toInt()
                package_to_date = T.getNextDate(dateResult,noOfdays)
            }
            else if(viewModel.monthOryear.equals("y"))
            {
                var noOfdays = 365 * viewModel.duration!!.toInt()
                package_to_date = T.getNextDate(dateResult,noOfdays)
            }
            viewModel.member_package_from_date = dateResult
            viewModel.package_to_date = package_to_date
            binding.goMembersViewModel = viewModel

        }, yeard, monthd, dayd)

        dpd.show()

    }

    override fun createMemberFabClicked() {

    }




}
