package com.example.kubaattendance.actors.super_admin.gyms

import android.content.Context
import com.example.kubaattendance.R
import com.example.kubaattendance.databinding.ItemGymInfoBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.T
import com.xwray.groupie.databinding.BindableItem

class GymInfoItem(private val context: Context,private val dGymInfo: DGymInfo,private val viewModel : SaGymsFragmentViewModel) : BindableItem<ItemGymInfoBinding>() {
    override fun getLayout(): Int = R.layout.item_gym_info
    override fun bind(viewBinding: ItemGymInfoBinding, position: Int) {
        viewBinding.gymInfo = dGymInfo


        viewBinding.activeDeactiveGymBtn.setOnClickListener {
            viewModel.activeDeactiveGymVM(dGymInfo.gym_id!!,dGymInfo.gym_active_status!!)
        }
        viewBinding.deviceIdTv.setOnClickListener {
            viewModel.onDeviceIdBtnClicked(dGymInfo)
        }
        if(dGymInfo.gym_photo.isNullOrEmpty() || dGymInfo.gym_photo.isNullOrBlank())
            viewBinding.imagePic.setImageResource(R.drawable.ic_gyms_default)
        else
        {
            T.setImage(
                viewBinding.imagePic,
                viewBinding.progressImg,
                context,
                Constants.MEMBER_IMAGE_PATH+""+dGymInfo.gym_photo!!)

        }

    }
}