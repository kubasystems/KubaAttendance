package com.example.kubaattendance.actors.gym_owner.my_gyms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.databinding.FragmentMyGymsBinding
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_gyms.*
import kotlinx.android.synthetic.main.fragment_my_gyms.search_edt
import java.lang.Exception

class MyGymsFragment : Fragment(),IBaseListner {



    lateinit var viewModel : MyGymsViewModel
    lateinit var binding : FragmentMyGymsBinding
    var GYMS = ArrayList<DMyGymInfo>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_my_gyms,container,false)
        viewModel = ViewModelProviders.of(this, MyApplication.myGymsViewModelFactory).get(MyGymsViewModel::class.java)
        binding.myGymsViewModel = viewModel
        viewModel.iBaseListner = this

        return  binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search ediext
        search_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var filteredModelList = filter(GYMS,""+s)

                if(filteredModelList.size > 0)
                    initRecyclerView(filteredModelList.toQuoteItem())

            }

        })

        viewModel.getAllGymOwnerGyms()
    }
    fun filter(models : ArrayList<DMyGymInfo>, query : String) : ArrayList<DMyGymInfo>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<DMyGymInfo>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : DMyGymInfo in models)
                {
                    var gym_name = model.gym_name!!.toLowerCase()
                    var gym_branch_id = model.gym_branch_id!!.toLowerCase()
                    var gym_active_status = model.gym_active_status!!.toLowerCase()

                    if(gym_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_branch_id!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_active_status!!.contains(query))
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
    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Getting Gyms, please wait...")
    }

    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE
        var result = data as MyGymsResponse
        if(result.success.equals("1"))
        {
            GYMS = result.gyms as ArrayList<DMyGymInfo>
            search_edt.visibility = View.VISIBLE
            initRecyclerView(GYMS.toQuoteItem())
        }
        else
        {
            search_edt.visibility = View.GONE
            activity!!.toast(result.message!!)
        }

    }
    private fun initRecyclerView(ownerInfoItem: List<MyGymInfoItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(ownerInfoItem)
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
    private fun List<DMyGymInfo>.toQuoteItem() : List<MyGymInfoItem>{
        return this.map {
            MyGymInfoItem(it)
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
