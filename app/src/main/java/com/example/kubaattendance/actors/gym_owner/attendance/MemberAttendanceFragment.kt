package com.example.kubaattendance.actors.gym_owner.attendance

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_device.TodaysAttendanceItem
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.models.GdTodaysAttendance
import com.example.kubaattendance.data.network.responses.GdTodaysAttendanceResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.databinding.MemberAttendanceFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.member_attendance_fragment.*
import java.io.Serializable
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MemberAttendanceFragment : BundleFragment(),IMemberAttendance {



    private lateinit var viewModel: MemberAttendanceViewModel
    private lateinit var binding: MemberAttendanceFragmentBinding

    var gym_owners =  ArrayList<DOwnerInfo>()

    lateinit var mdelayHandler: Handler
    lateinit var mdelayHandlerUserTimer: Handler

    var gymID = "0"
    var from = "0"
    var progressFlag = "0"

    var TAG = "Member_Attendance_Fragment : "
    private var passingData : String? = null

    lateinit var customSpinnerDataHolder: CustomSpinnerDataHolder

    var attendance_data = ArrayList<GdTodaysAttendance>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.member_attendance_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.memberAttendanceViewModelFactory).get(MemberAttendanceViewModel::class.java)
        binding.memberAttendanceViewModel = viewModel
        viewModel.iMemberAttendance = this
        return binding.root
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

                var filteredModelList = filter(attendance_data,""+s)

                if(filteredModelList.size > 0)
                    initRecyclerView(filteredModelList.toQuoteItem())

            }

        })

        arguments!!.let {

            passingData = MemberAttendanceFragmentArgs.fromBundle(it).passingData
            if(passingData.equals("0"))
                viewModel.getGymDetails()
            else
            {
                slect_gym_card.visibility = View.GONE
                gymID = passingData!!
                progressFlag = "F"
                var dateData = T.getSystemDateTime()!!.split(" ")
                viewModel.getTodaysAttendance(dateData.get(0),gymID!!,progressFlag)
            }
        }


    }
    fun filter(models : ArrayList<GdTodaysAttendance>, query : String) : ArrayList<GdTodaysAttendance>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<GdTodaysAttendance>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : GdTodaysAttendance in models)
                {
                    var name = model.name!!.toLowerCase()
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
            var dateData = T.getSystemDateTime()!!.split(" ")
            viewModel.getTodaysAttendance(dateData.get(0),gymID!!,progressFlag)
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
        if(mIsRunningUserTimer)
            cancelUserHandler()

    }

    override fun showProgress() {


    }
    override fun onStarted(message: String) {
        progressMsg_tv.setText(message)
        progress_layout.visibility = View.VISIBLE
    }

    override fun hideProgress() {

    }

    override fun onResponse(anyResponse: Any) {


        if(anyResponse is GdTodaysAttendanceResponse)
        {
            when(progressFlag)
            {
                "F" -> progress_layout.visibility = View.GONE
                else -> {}
            }

            if(from.equals("from_home_page"))
            {
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
            }
            var success = anyResponse.success
            Log.e(Constants.KUBA_LOGS,"Member Attendance Response : "+success)
            if(success.equals("1"))
            {

                attendance_data.clear()
                hide_layout.visibility = View.GONE
                hide_card.visibility = View.VISIBLE
                attendance_data = anyResponse.attendance as ArrayList<GdTodaysAttendance>
                initRecyclerView(attendance_data.toQuoteItem())


            }
            else if(success.equals("0"))
            {
                attendance_data.clear()
                initRecyclerView(attendance_data.toQuoteItem())
                hide_layout.visibility = View.VISIBLE
                hide_card.visibility = View.GONE
                text_data.setText(""+anyResponse.message)
            }
            else
            {
                var message = anyResponse.message
                activity!!.toast(message!!)
            }

        }
        else if(anyResponse is MyGymsResponse)
        {
            progress_layout.visibility = View.GONE
            if(anyResponse.success.equals("1"))
            {

                gym_owners.clear()
                for (i in 0 until anyResponse.gyms.size)
                {
                    var gym = anyResponse.gyms.get(i)
                    gym_owners.add(
                        DOwnerInfo(
                            "",
                            gym.gym_id,
                            "",
                            gym.gym_name,
                            "",
                            "")
                    )
                }
                customSpinnerDataHolder = CustomSpinnerDataHolder(gym_owners)
            }
            else
                activity!!.toast(anyResponse.message!!)
        }
    }

    private fun initRecyclerView(quoteItem: List<TodaysAttendanceItem>) {

        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(quoteItem)
            notifyDataSetChanged()
        }
        recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
    private fun List<GdTodaysAttendance>.toQuoteItem() : List<TodaysAttendanceItem>{
        return this.map {
            TodaysAttendanceItem(it)
        }
    }
    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }
    override fun selectGymButtonClicked() {

        if(customSpinnerDataHolder.data.size > 0)
        {

            var action = MemberAttendanceFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolder)
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
                    viewModel.gym_name = gym_owner_name!!
                    binding.memberAttendanceViewModel = viewModel

                    slect_date_cv.visibility = View.VISIBLE
                    // deregisterReceiver()
                    progressFlag = "F"
                    var dateData = T.getSystemDateTime()!!.split(" ")
                    viewModel.getTodaysAttendance(dateData.get(0),gym_owner_id!!,progressFlag)
                }

            }
        }
    }

    lateinit var dayData : String
    lateinit var monthData : String

    override fun selectDateButtonClicked() {
       //
        val c = Calendar.getInstance()
        val yeard = c.get(Calendar.YEAR)
        val monthd = c.get(Calendar.MONTH)
        val dayd = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            //lblDate.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
            var month = monthOfYear+1

            if(dayOfMonth.toString().length == 1)
                dayData = "0"+dayOfMonth
            else
                dayData = ""+dayOfMonth

            if(month.toString().length == 1)
                monthData = "0"+month
            else
                monthData = ""+month


            var selectedDate = ""+year+"-"+monthData+"-"+dayData
            viewModel.select_date = selectedDate
            binding.memberAttendanceViewModel = viewModel
            progressFlag = "F"
            viewModel.getTodaysAttendance(selectedDate,viewModel.gym_id!!,progressFlag)

        }, yeard, monthd, dayd)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

}
