package com.example.kubaattendance.actors.super_admin.gym_owner

import android.content.Context
import com.example.kubaattendance.R
import com.example.kubaattendance.databinding.ItemOwnerInfoBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.IBaseListner
import com.example.kubaattendance.utils.T
import com.xwray.groupie.databinding.BindableItem

class OwnerInfoItem(private val context: Context,private val ownerInfo: DOwnerInfo,private val viewModel : SaGymOwnersFragmentViewModel) : BindableItem<ItemOwnerInfoBinding>() {
    var iBaseListner : IBaseListner? = null
    override fun getLayout(): Int = R.layout.item_owner_info

    override fun bind(viewBinding: ItemOwnerInfoBinding, position: Int) {
        viewBinding.ownerInfo = ownerInfo

        viewBinding.activeDeactiveOwnerBtn.setOnClickListener {
            //activate or deactivate owner

            viewModel.onActivateDeactivateGymOwnerBtnClicked(ownerInfo.gym_owners_user_id!!,ownerInfo.is_active!!)
        }
        if(ownerInfo.gym_owner_photo.isNullOrEmpty() || ownerInfo.gym_owner_photo.isNullOrBlank())
            viewBinding.imagePic.setImageResource(R.drawable.ic_profile_demo)
        else
        {
            T.setImage(
                viewBinding.imagePic,
                viewBinding.progressImg,
                context,
                Constants.MEMBER_IMAGE_PATH+""+ownerInfo.gym_owner_photo!!)

        }


    }
}