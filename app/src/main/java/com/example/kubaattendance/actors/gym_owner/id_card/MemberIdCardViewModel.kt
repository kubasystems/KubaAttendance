package com.example.kubaattendance.actors.gym_owner.id_card

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import java.lang.Exception

class MemberIdCardViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel() {

    var iBaseListner : IMemberIdCardCallbacks? = null

    var gymName : String? = null
    var gymBranchCode : String? = null
    var memberImage : String? = null
    var memberName : String? = null
    var qrCodeBitmap : String? = null
    var qrCodeContent : String? = null
    var memberAddress : String? = null
    var memberDOJ : String? = null
    var memberPhone : String? = null
    var memberEmail : String? = null

    fun getIdCardDetails(member_id : String)
    {
        if(!T.isNetworkAvailable())
        {
            iBaseListner!!.showToast(Constants.CONNECTION)
            return
        }
        iBaseListner!!.showProgress()
        try
        {
            Coroutines.main {

                var memberIdCardResponse = gymOwnerRepository.getIdCardDetails(
                    MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                    member_id
                )
                memberIdCardResponse.success?.let {
                    iBaseListner!!.onResponse(memberIdCardResponse)
                    return@main
                }
            }
        }
        catch (e : Exception)
        {
            iBaseListner!!.onFailure(e.message!!)
        }
    }
    fun saveIdCardBtnClicked(view : View)
    {
        iBaseListner!!.saveIdCardBtnClicked(memberName!!.replace(" ","_")+"_"+memberPhone)
    }
    fun shareIdCardBtnClicked(view : View)
    {
        iBaseListner!!.shareIdCardBtnClicked(memberName!!.replace(" ","_")+"_"+memberPhone)
    }
}
