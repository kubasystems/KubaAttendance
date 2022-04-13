package com.example.kubaattendance.actors.super_admin.gym_owner.create

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog

import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.databinding.CreateGymOwnerFragmentBinding
import com.example.kubaattendance.utils.*
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.create_gym_owner_fragment.*

class CreateGymOwnerFragment : Fragment(),IBaseListner {



    companion object {
        fun newInstance() = CreateGymOwnerFragment()
    }

    private lateinit var viewModel: CreateGymOwnerViewModel
    private lateinit var binding: CreateGymOwnerFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.create_gym_owner_fragment,container,false)
        viewModel = ViewModelProviders.of(this,MyApplication.createGymOwnerViewModelFactory).get(CreateGymOwnerViewModel::class.java)
        binding.createGymOwnerViewModel = viewModel
        viewModel.iBaseListner = this
        return binding.root
    }


    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Creating Gym Owner...")
    }

    override fun hideProgress() {

    }

    override fun onResponse(data: Any) {

        progress_layout.visibility = View.GONE
        var response = data as CommonResponse

        if(response.success.equals("1"))
        {
            T.displaySuccessFailureDialog(
                activity!!,
                object : ISweetDialog{
                    override fun onConfirmClickListener() {

                        navigateUp(811,Bundle().apply {
                            putString("refresh_flag", "1")
                        })
                    }

                    override fun onCancelClickListener() {

                        navigateUp(811,Bundle().apply {
                            putString("refresh_flag", "1")
                        })
                    }

                },
                Constants.SA_SUCCESS,
                "Gym Owner",
                response.message!!,
                "Ok",
                "Cancel")
        }
        else
        {
            T.displaySuccessFailureDialog(
                activity!!,
                object : ISweetDialog{
                    override fun onConfirmClickListener() {

                    }

                    override fun onCancelClickListener() {

                    }

                },
                Constants.SA_WARNNING,
                "Gym Owner",
                response.message!!,
                "Ok",
                "Cancel")
        }
    }


    override fun showToast(message: String) {

        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)

    }

}
