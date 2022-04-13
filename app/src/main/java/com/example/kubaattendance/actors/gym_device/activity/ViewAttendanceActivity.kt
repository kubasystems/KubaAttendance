package com.example.kubaattendance.actors.gym_device.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_device.GymDeviceListner
import com.example.kubaattendance.actors.gym_device.GymDeviceViewModel
import com.example.kubaattendance.actors.gym_device.TodaysAttendanceItem
import com.example.kubaattendance.data.models.GdTodaysAttendance
import com.example.kubaattendance.data.network.responses.GdTodaysAttendanceResponse
import com.example.kubaattendance.databinding.ActivityViewAttendanceBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_view_attendance.*

class ViewAttendanceActivity : AppCompatActivity(), GymDeviceListner {



    lateinit var binding : ActivityViewAttendanceBinding
    lateinit var gymDeviceViewModel : GymDeviceViewModel
    var attendance_data = ArrayList<GdTodaysAttendance>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_view_attendance)

        binding  = DataBindingUtil.setContentView(this, R.layout.activity_view_attendance)
        gymDeviceViewModel = ViewModelProviders.of(this, MyApplication.gymDeviceViewModelFactory).get(GymDeviceViewModel::class.java)
        binding.gymDeviceViewModel = gymDeviceViewModel
        gymDeviceViewModel.gymDeviceListner = this

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        //in out and total count when app first launch
        if(T.isNetworkAvailable())
            gymDeviceViewModel.getTodaysAttendance()
        else
            toast(Constants.CONNECTION)

    }

    override fun onBackButtonClicked() {

        finish()
    }
    override fun onStarted(message: String) {
        progressMsg_tv.setText(message)
        progress_layout.visibility = View.VISIBLE
    }

    override fun onSuccess(anyResponse: Any) {

        progress_layout.visibility = View.GONE
        if(anyResponse is GdTodaysAttendanceResponse)
        {
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
                toast(message!!)
            }

        }

    }

    override fun onSuccess(anyObject: List<GdTodaysAttendance>) {
        progress_layout.visibility = View.GONE
            //set data to adapter
        initRecyclerView(anyObject.toQuoteItem())
    }
    private fun initRecyclerView(quoteItem: List<TodaysAttendanceItem>) {

        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(quoteItem)
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
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        toast(message)
    }
    override fun showLogoutDialog() {

    }
}
