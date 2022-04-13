package com.example.kubaattendance.actors.gym_owner.members

import com.example.kubaattendance.utils.IBaseListner

interface IGoCreateMember : IBaseListner{

    fun showProgress(message : String)
    fun onSelectPackageButtonClicked()
    fun selectPackageFromDateButtonClicked()
    fun onCreateGymMemberClicked()
    fun createMemberFabClicked()
    fun selectGymButtonClicked()
}