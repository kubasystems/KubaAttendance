package com.example.kubaattendance.actors.gym_device

import com.example.kubaattendance.R
import com.example.kubaattendance.data.models.GdTodaysAttendance
import com.example.kubaattendance.databinding.ItemTodaysAttendanceBinding
import com.example.kubaattendance.utils.T
import com.xwray.groupie.databinding.BindableItem

class TodaysAttendanceItem(private val gdTodaysAttendance : GdTodaysAttendance)
    : BindableItem<ItemTodaysAttendanceBinding>()
{
    override fun getLayout() = R.layout.item_todays_attendance

    override fun bind(viewBinding: ItemTodaysAttendanceBinding, position: Int) {

        viewBinding.todaysAttendance = gdTodaysAttendance
        var in_timestamp = gdTodaysAttendance.in_timestamp


        if(in_timestamp.isNullOrEmpty())
            viewBinding.totalTimeInGymTv.setText("NA")
        else
        {
            var inTimeData = gdTodaysAttendance.in_timestamp!!.split(" ")
            var outTimeData = gdTodaysAttendance.out_timestamp!!.split(" ")

            var inTimeSeconds = T.timeStampToSeconds(inTimeData.get(1))
            var outTimeSeconds = T.timeStampToSeconds(outTimeData.get(1))

            var timeInGymSec = outTimeSeconds - inTimeSeconds
            var timeFormat = T.secondsToTime(timeInGymSec)

            viewBinding.totalTimeInGymTv.setText(timeFormat.substring(0,5))
            viewBinding.inTimeTv.setText(inTimeData.get(1).substring(0,5))
            viewBinding.outTimeTv.setText(outTimeData.get(1).substring(0,5))
        }

    }
}