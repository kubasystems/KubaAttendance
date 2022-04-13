package com.example.kubaattendance.actors.super_admin.gyms.create

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
import com.example.kubaattendance.data.network.responses.super_admin.GymOwnersResponse
import com.example.kubaattendance.databinding.CreateGymFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.create_gym_fragment.*
import java.io.Serializable

class CreateGymFragment : BundleFragment(), ICreateGym {



    private lateinit var viewModel: CreateGymViewModel
    lateinit var binding : CreateGymFragmentBinding

    lateinit var customSpinnerDataHolder : CustomSpinnerDataHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.create_gym_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.createGymViewModelFactory).get(CreateGymViewModel::class.java)
        binding.createGymViewModel = viewModel
        viewModel.iBaseListner = this
        viewModel.iCreateGym = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //fetch gym owners
        viewModel.getAllGymOwners()
    }

    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Creating Gym, please wait...")
    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun hideProgress() {

    }


    override fun onResponse(result: Any) {
        progress_layout.visibility = View.GONE

        if(result is CommonResponse)
        {
            if(result.success.equals("1"))
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog {
                        override fun onConfirmClickListener() {

                            navigateUp(811,Bundle().apply {
                                putString("refresh_flag","1")
                            })
                        }

                        override fun onCancelClickListener() {
                            navigateUp(811,Bundle().apply {
                                putString("refresh_flag","1")
                            })
                        }

                    },
                    Constants.SA_SUCCESS,
                    "Success",
                    result.message!!,
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
                    result.message!!,
                    "Ok",
                    "Cancel")
            }
        }
        else if(result is GymOwnersResponse)
        {

            if(result.success.equals("1"))
            {
                customSpinnerDataHolder = CustomSpinnerDataHolder(result.gym_details as ArrayList<DOwnerInfo>)
                //gym_owners = result.gym_details
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
                    result.message!!,
                    "Ok",
                    "Cancel")
            }
        }

    }

    override fun showToast(message: String) {

        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }
    override fun onSelectGymOwnerButtonClicked() {

        try
        {
            if(customSpinnerDataHolder.data.size > 0)
            {

                var action = CreateGymFragmentDirections.customSpinnerData()
                action.setCustomSpinnerDataHolder(customSpinnerDataHolder)
                navigate(action,855)

            }
        }
        catch (ee : UninitializedPropertyAccessException)
        {
            activity!!.toast("Oops ! Gym owners not found")
        }
        catch (e : Exception)
        {
            activity!!.toast(""+e)
        }


    }

    var TAG = "CREATE_GYM_FRAGMENT : "
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
                    //var gym_owner_mobile = bundle.getString("gym_owner_mobile")
                    //var gym_owner_email = bundle.getString("gym_owner_email")
                    //var is_active = bundle.getString("is_active")

                    viewModel.gym_owner_id = gym_owner_id
                    viewModel.gym_owner_user_id = gym_owners_user_id
                    viewModel.ownerNames = gym_owner_name!!
                    binding.createGymViewModel = viewModel
                }

            }
        }
    }
}
