package com.example.kubaattendance.utils.custom_spinner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.databinding.CustomSpinnerFragmentBinding
import com.phelat.navigationresult.navigateUp
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.custom_spinner_fragment.*
import java.lang.Exception

class CustomSpinnerFragment : DialogFragment() {

    private lateinit var viewModel: CustomSpinnerViewModel
    private lateinit var binding: CustomSpinnerFragmentBinding
    var gym_owners =  ArrayList<DOwnerInfo>()

    private var customSpinnerDataHolder : CustomSpinnerDataHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.custom_spinner_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(CustomSpinnerViewModel::class.java)
        binding.customSpinnerViewModel = viewModel

        arguments!!.let {

            customSpinnerDataHolder = CustomSpinnerFragmentArgs.fromBundle(it).customSpinnerDataHolder
            gym_owners = customSpinnerDataHolder!!.data
            if(gym_owners.size > 0)
                initRecyclerView(gym_owners.toQuoteItem())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //search ediext
        search_edt.addTextChangedListener(object : TextWatcher{
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
                    var name = model.gym_owner_name!!.toLowerCase()

                    if(name!!.contains(query))
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
    private fun initRecyclerView(ownerInfoItem: List<CustomSpinnerItem>)
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
    private fun List<DOwnerInfo>.toQuoteItem() : List<CustomSpinnerItem>{
        return this.map {
            CustomSpinnerItem(it,object : ICustomSpinner{
                override fun returnDataToFragment(dOwnerInfo: DOwnerInfo) {

                    var gym_owner_photo = dOwnerInfo.gym_owner_photo ?: "0"
                    var gym_owner_id = dOwnerInfo.gym_owner_id ?: "0"
                    var gym_owners_user_id = dOwnerInfo.gym_owners_user_id ?: "0"
                    var gym_owner_name = dOwnerInfo.gym_owner_name ?: "0"
                    var gym_owner_mobile = dOwnerInfo.gym_owner_mobile ?: "0"
                    var gym_owner_email = dOwnerInfo.gym_owner_email ?: "0"
                    var is_active = dOwnerInfo.is_active ?: "0"

                    navigateUp(855,Bundle().apply {
                        putString("flag","set")
                        putString("gym_owner_photo",gym_owner_photo)
                        putString("gym_owner_id",gym_owner_id)
                        putString("gym_owners_user_id",gym_owners_user_id)
                        putString("gym_owner_name",gym_owner_name)
                        putString("gym_owner_mobile",gym_owner_mobile)
                        putString("gym_owner_email",gym_owner_email)
                        putString("is_active",is_active)
                    })
                }

            })
        }
    }

}
