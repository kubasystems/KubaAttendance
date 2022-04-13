package com.example.kubaattendance.actors.gym_owner.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
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
import com.example.kubaattendance.data.network.responses.gym_owner.HomeResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.databinding.GoHomeFragmentBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.example.kubaattendance.utils.toast
import com.phelat.navigationresult.BundleFragment
import kotlinx.android.synthetic.main.go_home_fragment.*
import java.io.Serializable

class GoHomeFragment : BundleFragment(), IGoHome {



    private lateinit var viewModel: GoHomeViewModel
    private lateinit var binding: GoHomeFragmentBinding
    var gym_data =  ArrayList<DOwnerInfo>()

    lateinit var mdelayHandler: Handler
    lateinit var mdelayHandlerUserTimer: Handler

    var gymID = "0"
    var progressFlag = "0"

    lateinit var customSpinnerDataHolder : CustomSpinnerDataHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.go_home_fragment, container, false)
        viewModel = ViewModelProviders.of(this, MyApplication.goHomeViewModelFactory).get(GoHomeViewModel::class.java)
        viewModel.iBaseListner = this

        viewModel.inCount = "0"
        viewModel.outCount = "0"
        viewModel.totalCount = "0"

        binding.goHomeFragmentViewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //check default gym is 0
        gymID = MyApplication.prefs.getString(Constants.GYM_ID,"0")!!
        var gymNAME = MyApplication.prefs.getString(Constants.GYM_NAME,"0")

        if(gymID.equals("0") || gymNAME.equals("0"))
            //if 0 get fresh gym detail
            viewModel.getGymDetails()
        else
        {
            //else get home page info
            viewModel.gym_id = gymID!!
            viewModel.gym_name = gymNAME!!
            binding.goHomeFragmentViewModel = viewModel

            progressFlag = "F"
            //get home page info
            viewModel.getHomePageInfo(gymID!!,progressFlag)

        }
    }


    fun startUserTimer() {
        mIsRunningUserTimer = true
        mStatusCheckerUserTimer.run()

    }
    internal var mIsRunningUserTimer: Boolean = false
    internal var mStatusCheckerUserTimer: Runnable = object : Runnable {
        override fun run() {

            if (!mIsRunningUserTimer) {
                return  // stop when told to stop
            }
            //get home page info
            progressFlag = "S"
            gymID = MyApplication.prefs.getString(Constants.GYM_ID,"0")!!
            viewModel.getHomePageInfo(gymID!!,progressFlag)
            mdelayHandlerUserTimer.postDelayed(this, 10000)
        }
    }

    private fun cancelUserHandler() {
        Log.e(Constants.KUBA_LOGS,"Handler destroyed...")
        mIsRunningUserTimer = false
        mdelayHandlerUserTimer.removeCallbacks(mStatusCheckerUserTimer)
    }

    override fun onDetach() {
        super.onDetach()
        MyApplication.editor.putString(Constants.GYM_ID,"0").commit()
        MyApplication.editor.putString(Constants.GYM_NAME,"0").commit()
        if(mIsRunningUserTimer)
            cancelUserHandler()

    }


    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Authenticating, please wait...")
    }

    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun hideProgress() {
        //progress_layout.visibility = View.GONE
    }

    override fun onResponse(result: Any) {


        if(result is MyGymsResponse)
        {
            progress_layout.visibility = View.GONE
            if(result.success.equals("1"))
            {
                gym_data.clear()
                for (i in 0 until result.gyms.size)
                {
                    var gym = result.gyms.get(i)
                    gym_data.add(DOwnerInfo(
                        "",
                        gym.gym_id,
                        "",
                        gym.gym_name,
                        "",
                        ""))


                }
                customSpinnerDataHolder = CustomSpinnerDataHolder(gym_data)
            }
            else
                activity!!.toast(result.message!!)


        }
        else if(result is HomeResponse)
        {
            when(progressFlag)
            {
                "F" -> progress_layout.visibility = View.GONE
                else -> {}
            }

            if(gymID != "0")
            {
                //refresh page every 10 sec
                mdelayHandler = Handler()
                mdelayHandlerUserTimer = Handler()

                if(!mIsRunningUserTimer)
                {
                    Log.e(Constants.KUBA_LOGS,"Handler Started...")
                    //start handler
                    startUserTimer()
                }
            }

            Log.e(Constants.KUBA_LOGS,"HomeResponse : "+result.success)
            if(result.success.equals("1"))
            {
                viewModel.inCount = result.todaysInCount
                viewModel.outCount = result.todaysOutCount
                viewModel.totalCount = result.todaysTotalCount
                binding.goHomeFragmentViewModel = viewModel
            }
        }
        else
            activity!!.toast("Oops ! data not found")
    }
    override fun onSelectGymButtonClicked() {

        try
        {
            if(customSpinnerDataHolder.data.size > 0)
            {
                var action = GoHomeFragmentDirections.customSpinnerData()
                action.setCustomSpinnerDataHolder(customSpinnerDataHolder)
                navigate(action,855)
            }
            else
                activity!!.toast("Oops ! Gym owners not found")
        }
        catch (e : UninitializedPropertyAccessException)
        {
            viewModel.getGymDetails()
        }
        catch (e : UninitializedPropertyAccessException)
        {
            viewModel.getGymDetails()
        }
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

                    //set default gym selected
                    MyApplication.editor.putString(Constants.GYM_ID,gym_owner_id).commit()
                    MyApplication.editor.putString(Constants.GYM_NAME,gym_owner_name).commit()

                    viewModel.gym_id = gym_owner_id
                    viewModel.gym_name = gym_owner_name!!
                    binding.goHomeFragmentViewModel = viewModel

                    //get home page info
                    progressFlag = "F"
                    viewModel.getHomePageInfo(gym_owner_id!!,progressFlag)
                }

            }
        }
    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {

        when(progressFlag)
        {
            "F" -> progress_layout.visibility = View.GONE
            else -> {}
        }
        activity!!.toast(message)

    }
    override fun onGetTodaysAttendance() {

        if(gymID != "0")
        {
            var action = GoHomeFragmentDirections.actionAttendanceFragment()
            action.setPassingData(gymID)
            navigate(action)
        }

    }


}
