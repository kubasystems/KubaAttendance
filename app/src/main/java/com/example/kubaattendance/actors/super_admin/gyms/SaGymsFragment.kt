package com.example.kubaattendance.actors.super_admin.gyms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.super_admin.GymsResponse
import com.example.kubaattendance.databinding.SaGymsFragmentBinding
import com.example.kubaattendance.utils.*
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.not_found_layout.*
import kotlinx.android.synthetic.main.sa_gyms_fragment.*

class SaGymsFragment : BundleFragment(), IGyms
{
    lateinit var viewModel : SaGymsFragmentViewModel
    lateinit var binding : SaGymsFragmentBinding

    var gym_details = ArrayList<DGymInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding  = DataBindingUtil.inflate(inflater,R.layout.sa_gyms_fragment,container,false)
        viewModel = ViewModelProviders.of(this,MyApplication.saGymDeviceViewModelFactory).get(SaGymsFragmentViewModel::class.java)
        binding.saGymsFragmentViewModel = viewModel
        viewModel.iBaseListner = this

        return  binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_edt.visibility = View.GONE
        search_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var filteredModelList = filter(gym_details,""+s)

                if(filteredModelList.size > 0)
                    initRecyclerView(filteredModelList.toQuoteItem())

            }

        })

        if(gym_details.size > 0)
        {
            search_edt.visibility = View.VISIBLE
            initRecyclerView(gym_details.toQuoteItem())
        }
        else
            viewModel.getAllGyms()
    }
    fun filter(models : ArrayList<DGymInfo>, query : String) : ArrayList<DGymInfo>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<DGymInfo>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : DGymInfo in models)
                {
                    var gym_name = model.gym_name!!.toLowerCase()
                    var gym_branch_id = model.gym_branch_id!!.toLowerCase()
                    var gym_mobile = model.gym_mobile!!.toLowerCase()
                    var gym_email = model.gym_email!!.toLowerCase()
                    var gym_owner_name = model.gym_owner_name!!.toLowerCase()
                    var device_id = model.device_id!!.toLowerCase()

                    if(gym_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_branch_id!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_mobile!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_email!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_owner_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(device_id!!.contains(query))
                        filterd_gym_owners.add(model)
                }
            }
            catch (e : Exception)
            {
                e.printStackTrace()

            }
        }
        return filterd_gym_owners
    }
    override fun createGymFabClicked() {

        navigate(SaGymsFragmentDirections.actionMainFragmentToCreateGymFragment(),811)
    }

    override fun onDeviceIdBtnClicked(dGymInfo : DGymInfo) {

        var action = SaGymsFragmentDirections.actionUpdateGymDeviceIdFragment()
        var updateDeviceIdDataPasser = UpdateDeviceIdDataPasser(dGymInfo)
        action.setDGymInfo(updateDeviceIdDataPasser)
        navigate(action,811)
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)
        if(requestCode == 811)
        {
            if(bundle != null)
            {
                var refresh_flag = bundle.getString("refresh_flag")
                if(refresh_flag.equals("1"))
                    viewModel.getAllGyms()
            }
        }

    }
    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Getting Gyms, please wait...")
    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }

    override fun hideProgress() {

    }
    override fun onFailure(message: String) {

        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE
        if(data is GymsResponse)
        {
            gym_details.clear()
            if(data.success.equals("1"))
            {

                search_edt.visibility = View.VISIBLE
                gym_details = data.gym_details as ArrayList<DGymInfo>
                initRecyclerView(gym_details.toQuoteItem())
            }
            else
            {
                search_edt.visibility = View.GONE
                hide_layout.visibility = View.VISIBLE
                image_data.setImageResource(R.drawable.ic_gym_nf)
                text_data.text = data.message
                initRecyclerView(gym_details.toQuoteItem())
            }
        }
        else if(data is CommonResponse)
            activity!!.toast(data.message!!)


    }
    override fun showToast(message: String) {

        activity!!.toast(message)
    }

    private fun initRecyclerView(ownerInfoItem: List<GymInfoItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(ownerInfoItem)
            notifyDataSetChanged()
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
    private fun List<DGymInfo>.toQuoteItem() : List<GymInfoItem>{
        return this.map {
            GymInfoItem(activity!!,it,viewModel)
        }
    }
    override fun activeDeactiveGym(gym_id: String, active_deactive_status: String) {

        Log.e(Constants.KUBA_LOGS,"active_deactive_status : "+active_deactive_status)
        when(active_deactive_status)
        {
            "1"-> showOperationDialog("deactivate",gym_id,"0")
            "0"-> showOperationDialog("activate",gym_id,"1")
            else->{

            }
        }
    }
    lateinit var dialogTitle : String
    lateinit var dialogMessage : String

    fun showOperationDialog(flag : String,gym_id : String,is_active : String)
    {
        when(flag)
        {
            "activate"->{
                dialogTitle = "Activate Gym"
                dialogMessage = "Do you want to Activate Gym?"
            }
            "deactivate"->{
                dialogTitle = "Deactivate Gym"
                dialogMessage = "Do you want to Deactivate Gym?"
            }
            else->{

            }
        }
        T.displaySuccessFailureDialog(
            activity!!,
            object : ISweetDialog{
                override fun onConfirmClickListener() {
                    when(flag)
                    {
                        "activate"->{
                            viewModel.activeDeactiveGym(gym_id,is_active)
                        }
                        "deactivate"->{
                            viewModel.activeDeactiveGym(gym_id,is_active)
                        }
                        else->{

                        }
                    }
                }

                override fun onCancelClickListener() {

                }

            },
            Constants.SA_NORMAL,
            dialogTitle,
            dialogMessage,
            "Ok",
            "Cancel")

    }


}