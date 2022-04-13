package com.example.kubaattendance.actors.gym_owner.members

import android.view.View
import com.example.kubaattendance.actors.gym_owner.members.data_classes.DMemberInfo
import com.example.kubaattendance.utils.IBaseListner

interface IGoMembers : IBaseListner {

    fun showProgress(message : String)
    fun onSelectPackageButtonClicked()
    fun createMemberFabButtonClicked()
    fun selectGymButtonClicked()
    fun menuMembersOptionClicked(view : View,dMemberInfo : DMemberInfo)
    fun onActivateDeactivateGymOwnerBtnClicked(gym_member_user_id : String,is_active : String)
}