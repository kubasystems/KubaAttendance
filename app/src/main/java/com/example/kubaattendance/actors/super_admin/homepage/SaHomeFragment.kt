package com.example.kubaattendance.actors.super_admin.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.super_admin.HomePageResponse
import com.example.kubaattendance.databinding.SaHomeFragmentsBinding
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.toast
import kotlinx.android.synthetic.main.sa_home_fragments.*


class SaHomeFragment : Fragment(),SaHomeCallbacks
{



    lateinit var navController: NavController
    lateinit var viewModel : SaHomeFragmentViewModel
    lateinit var binding : SaHomeFragmentsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding  = DataBindingUtil.inflate(inflater,R.layout.sa_home_fragments,container,false)
        viewModel = ViewModelProviders.of(this,MyApplication.saHomeFragmentViewModelFactory).get(SaHomeFragmentViewModel::class.java)
        binding.saHomeFragmentViewModel = viewModel
        viewModel.iSaHomeCallbacks = this

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        viewModel.getHomePageInfo()
    }
    override fun totalGymsViewClicked() {

        navController!!.navigate(R.id.sa_gyms_fragment)
    }

    override fun totalGymOwnerViewClicked() {

        navController!!.navigate(R.id.sa_gym_owner_fragment)
    }

    override fun totalGymMembersViewClicked() {

        activity!!.toast("Total Gym Members")
    }
    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Loading, please wait...")
    }

    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {

        progress_layout.visibility = View.GONE
        var homePageResponse = data as HomePageResponse

        if(homePageResponse.success.equals("1"))
        {
            viewModel.totalGymsCount = homePageResponse.totalGyms
            viewModel.totalGymOwnerCount = homePageResponse.totalGymOwners
            viewModel.totalGymMembersCount = homePageResponse.totalGymMembers
            binding.saHomeFragmentViewModel = viewModel
        }
        else
            activity!!.toast(homePageResponse.message!!)

    }
    override fun showToast(message: String) {

        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }



}