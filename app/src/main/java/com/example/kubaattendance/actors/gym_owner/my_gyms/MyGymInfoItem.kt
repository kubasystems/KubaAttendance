package com.example.kubaattendance.actors.gym_owner.my_gyms

import com.example.kubaattendance.R
import com.example.kubaattendance.databinding.MyGymInfoitemLayoutBinding
import com.xwray.groupie.databinding.BindableItem

class MyGymInfoItem(private  val myGymInfo: DMyGymInfo)  : BindableItem<MyGymInfoitemLayoutBinding>(){
    override fun getLayout(): Int = R.layout.my_gym_infoitem_layout
    override fun bind(viewBinding: MyGymInfoitemLayoutBinding, position: Int) {
        viewBinding.dMyGymInfo = myGymInfo
    }
}