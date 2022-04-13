package com.example.kubaattendance.actors.gym_owner.pkg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.databinding.CreatePackageFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.create_package_fragment.*

class CreatePkgFragment : BundleFragment(),ICreatePackages {



    private lateinit var viewModel: PkgViewModel
    private lateinit var binding: CreatePackageFragmentBinding
    var gym_names =  ArrayList<DOwnerInfo>()
    private var viewModelBundle : PkgViewModel? = null
    lateinit var customSpinnerDataHolderGyms: CustomSpinnerDataHolder

    var TAG = "CreatePkgFragment : "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.create_package_fragment, container, false)
        viewModel = ViewModelProviders.of(this, MyApplication.pkgViewModelFactory).get(PkgViewModel::class.java)
        binding.pkgViewModel = viewModel
        viewModel.iCreatePackages = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try
        {
            arguments!!.let {
                viewModelBundle = CreatePkgFragmentArgs.fromBundle(it).pkgViewModel!!

                viewModel.gym_id = viewModelBundle!!.gym_id
                viewModel.gymName = viewModelBundle!!.gymName
                viewModel.duration_hint = "Select Package months*"
                viewModel.pkg_month_or_year = "m"
                binding.pkgViewModel = viewModel
            }
            gym_name_til.visibility = View.VISIBLE
            gym_name_et.keyListener = null
            select_gym_cv.visibility = View.GONE
        }
        catch (e : KotlinNullPointerException)
        {

            gym_name_til.visibility = View.GONE
            select_gym_cv.visibility = View.VISIBLE
            viewModel.operation_flag = "iCreatePackages"
            viewModel.duration_hint = "Select Package months*"
            viewModel.pkg_month_or_year = "m"
            viewModel.iCreatePackages = this
            binding.pkgViewModel = viewModel
            viewModel.getGymDetails()
        }
        catch (e : UninitializedPropertyAccessException)
        {

            gym_name_til.visibility = View.GONE
            select_gym_cv.visibility = View.VISIBLE
            viewModel.operation_flag = "iCreatePackages"
            viewModel.duration_hint = "Select Package months*"
            viewModel.pkg_month_or_year = "m"
            viewModel.iCreatePackages = this
            binding.pkgViewModel = viewModel
            viewModel.getGymDetails()
        }
    }

    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Creating Package, please wait...")
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
                            navigateUp(888, Bundle().apply {
                                putString("refresh_flag", "1")
                            })
                        }

                        override fun onCancelClickListener() {
                            navigateUp(888, Bundle().apply {
                                putString("refresh_flag", "1")
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
        else if(data is MyGymsResponse)
        {
            gym_names.clear()
            if(data.success.equals("1"))
            {
                for (i in 0 until data.gyms.size)
                {
                    var gym = data.gyms.get(i)
                    gym_names.add(
                        DOwnerInfo(
                            "",
                            gym.gym_id,
                            "",
                            gym.gym_name,
                            "",
                            "")
                    )
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
        progress_layout.visibility = View.GONE
        activity!!.toast(message)

    }
    override fun showProgress(message: String) {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun durationHintChanged(package_type: String) {

        if (package_type.equals("monthly"))
        {
            viewModel.duration_hint = "Enter Package Months*"
            viewModel.pkg_month_or_year = "m"
            binding.pkgViewModel = viewModel
        }
        else if (package_type.equals("yearly"))
        {
            viewModel.duration_hint = "Enter Package Years*"
            viewModel.pkg_month_or_year = "y"
            binding.pkgViewModel = viewModel
        }
    }

    override fun selectGymBtnClicked() {

        if(customSpinnerDataHolderGyms.data.size > 0)
        {
            var action = CreatePkgFragmentDirections.customSpinnerData()
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
                    var gym_owner_id = bundle.getString("gym_owner_id")
                    var gym_owner_name = bundle.getString("gym_owner_name")

                    viewModel.gym_id = gym_owner_id
                    viewModel.gymName = gym_owner_name!!
                    binding.pkgViewModel = viewModel

                }

            }
        }
    }


}
