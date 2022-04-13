package com.example.kubaattendance.actors.super_admin.gym_owner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.kubaattendance.data.network.responses.super_admin.GymOwnersResponse
import com.example.kubaattendance.databinding.SaGymOwnersFragmentsBinding
import com.example.kubaattendance.utils.*
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.not_found_layout.*
import kotlinx.android.synthetic.main.sa_gym_owners_fragments.*

class SaGymOwnersFragment : BundleFragment(), IGymOwner
{
    lateinit var viewModel : SaGymOwnersFragmentViewModel
    lateinit var binding : SaGymOwnersFragmentsBinding

    var gym_owners = ArrayList<DOwnerInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.sa_gym_owners_fragments,container,false)
        viewModel = ViewModelProviders.of(this,MyApplication.saGymOwnersFragmentViewModelFactory).get(SaGymOwnersFragmentViewModel::class.java)
        binding.saGymOwnersFragmentViewModel = viewModel
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

                var filteredModelList = filter(gym_owners,""+s)

                if(filteredModelList.size > 0)
                    initRecyclerView(filteredModelList.toQuoteItem())

            }

        })

        if(gym_owners.size > 0)
            initRecyclerView(gym_owners.toQuoteItem())
        else
            viewModel.getAllGymOwners()
    }
    fun filter(models : ArrayList<DOwnerInfo>, query : String) : ArrayList<DOwnerInfo>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<DOwnerInfo>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : DOwnerInfo in models)
                {
                    var gym_owner_name = model.gym_owner_name!!.toLowerCase()
                    var gym_owner_mobile = model.gym_owner_mobile!!.toLowerCase()
                    var gym_owner_email = model.gym_owner_email!!.toLowerCase()
                    var is_active = model.is_active!!.toLowerCase()

                    if(gym_owner_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_owner_mobile!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_owner_email!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(is_active!!.contains(query))
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
    override fun createGymOwnerFabClicked() {
        navigate(SaGymOwnersFragmentDirections.actionMainFragmentToCreateGymOwnerFragment(),811)
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)

        if(requestCode == 811)
        {
            if(bundle != null)
            {
                var refresh_flag = bundle.getString("refresh_flag")
                if(refresh_flag.equals("1"))
                    viewModel.getAllGymOwners()
            }
        }
    }
    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Getting Gym Owners, please wait...")
    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun hideProgress() {

    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE

        if(data is GymOwnersResponse)
        {
            gym_owners.clear()
                if(data.success.equals("1"))
                {
                    search_edt.visibility = View.VISIBLE
                    gym_owners = data.gym_details as ArrayList<DOwnerInfo>
                    initRecyclerView(gym_owners.toQuoteItem())
                }
                else
                {
                    search_edt.visibility = View.GONE
                    hide_layout.visibility = View.VISIBLE
                    image_data.setImageResource(R.drawable.ic_gym_owner)
                    text_data.text = data.message
                    initRecyclerView(gym_owners.toQuoteItem())
                }

        }
        else if(data is CommonResponse)
            activity!!.toast(data.message!!)
    }
    private fun initRecyclerView(ownerInfoItem: List<OwnerInfoItem>)
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
    private fun List<DOwnerInfo>.toQuoteItem() : List<OwnerInfoItem>{
        return this.map {
            OwnerInfoItem(activity!!,it,viewModel)
        }
    }
    override fun showToast(message: String) {

        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
    }

    override fun onActivateDeactivateGymOwnerBtnClicked(gym_owners_user_id: String, is_active: String) {

        when(is_active)
        {
            "1"-> showOperationDialog("deactivate",gym_owners_user_id,"0")
            "0"-> showOperationDialog("activate",gym_owners_user_id,"1")
            else->{

            }
        }
    }



    lateinit var dialogTitle : String
    lateinit var dialogMessage : String

    fun showOperationDialog(flag : String,gym_owners_user_id : String,is_active : String)
    {
        when(flag)
        {
            "activate"->{
                dialogTitle = "Activate Owner"
                dialogMessage = "Do you want to Activate this owner?"
            }
            "deactivate"->{
                dialogTitle = "Deactivate Owner"
                dialogMessage = "Do you want to Deactivate this owner?"
            }
            else->{

            }
        }
        T.displaySuccessFailureDialog(
            activity!!,
            object : ISweetDialog {
                override fun onConfirmClickListener() {
                    when(flag)
                    {
                        "activate"->{
                            viewModel.activateDeactivateGymOwner(gym_owners_user_id,is_active)
                        }
                        "deactivate"->{
                            viewModel.activateDeactivateGymOwner(gym_owners_user_id,is_active)
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