package com.example.kubaattendance.actors.super_admin.gyms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.super_admin.gyms.create.CreateGymViewModel
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.databinding.FragmentUpdateGymDeviceBinding
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.toast
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.fragment_update_gym_device.*

class UpdateGymDeviceFragment : Fragment(),IBaseListner {


    lateinit var binding : FragmentUpdateGymDeviceBinding
    private lateinit var viewModel: CreateGymViewModel

    var dGymInfo : UpdateDeviceIdDataPasser? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_update_gym_device, container, false)
        viewModel = ViewModelProviders.of(this, MyApplication.createGymViewModelFactory).get(CreateGymViewModel::class.java)
        binding.createGymViewModel = viewModel
        viewModel.iBaseListner = this

        arguments!!.let {

            dGymInfo = UpdateGymDeviceFragmentArgs.fromBundle(it).dGymInfo
            viewModel.gymID = dGymInfo!!.dGymInfo1!!.gym_id
            viewModel.gymDeviceID = dGymInfo!!.dGymInfo1!!.device_id
            viewModel.gymName = dGymInfo!!.dGymInfo1!!.gym_name
            binding.createGymViewModel = viewModel


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gym_name_et.keyListener = null
    }
    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Updating gym device id...")
    }

    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE
        var response = data as CommonResponse
        if(response.success.equals("1"))
        {
            activity!!.toast(response.message!!)
            navigateUp(811,Bundle().also {
                it.putString("refresh_flag","1")
            })
        }
        else
            activity!!.toast(response.message!!)
    }

    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }
}
