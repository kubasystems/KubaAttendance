package com.example.kubaattendance.actors.gym_owner.members.item_classes

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.members.GoMembersViewModel
import com.example.kubaattendance.actors.gym_owner.members.data_classes.DMemberInfo
import com.example.kubaattendance.databinding.MemberInfoItemBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.T
import com.xwray.groupie.databinding.BindableItem
import java.io.Serializable

class MemberInfoItem (private val context: Context,private val dMemberInfo: DMemberInfo,private val goMembersViewModel : GoMembersViewModel) : BindableItem<MemberInfoItemBinding>(){
    override fun getLayout(): Int = R.layout.member_info_item

    override fun bind(viewBinding: MemberInfoItemBinding, position: Int) {
        viewBinding.dMemberInfo = dMemberInfo

        if(dMemberInfo.pkg_from_date!!.contains("-"))
            viewBinding.fromDate.setText("Start : "+T.getFormatedDate(dMemberInfo.pkg_from_date!!))
        else
            viewBinding.fromDate.setText("Start : "+dMemberInfo.pkg_from_date!!)

        if(dMemberInfo.pkg_to_date!!.contains("-"))
            viewBinding.toDate.setText("End : "+T.getFormatedDate(dMemberInfo.pkg_to_date!!))
        else
            viewBinding.toDate.setText("End : "+dMemberInfo.pkg_to_date!!)

        viewBinding.menuMemberImg.setOnClickListener {
            goMembersViewModel.menuMembersOptionClicked(it,dMemberInfo)
        }

        if(dMemberInfo.gym_member_photo.isNullOrBlank() || dMemberInfo.gym_member_photo.isNullOrEmpty())
            viewBinding.imagePic.setImageResource(R.drawable.ic_profile_demo)
        else
        {
            T.setImage(
                viewBinding.imagePic,
                viewBinding.progressImg,
                context,
                Constants.MEMBER_IMAGE_PATH+""+dMemberInfo.gym_member_photo!!)

        }
    }
}