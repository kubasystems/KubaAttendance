package com.example.kubaattendance.logout

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.example.kubaattendance.R
import com.example.kubaattendance.authentication.other_user.OtherUserLoginActivity
import com.example.kubaattendance.databinding.LogoutFragmentBinding
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.toast

class LogoutFragment : DialogFragment(), ILogoutCallBacks
{


    companion object {
        fun newInstance() = LogoutFragment()
    }
    private lateinit var viewModel: LogoutViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding : LogoutFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.logout_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.logoutViewModelFactory).get(LogoutViewModel::class.java)
        binding.logoutViewModel = viewModel
        viewModel.iLogoutCallBacks = this
        return binding.root
    }


    override fun onLogoutBtnClicked() {
        dismiss()
        MyApplication.editor.clear().commit()
        Intent(activity,OtherUserLoginActivity::class.java).also{
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(it)
            activity!!.finish()
        }
        activity!!.toast("Logout Successfully")

    }

    override fun onCancelBtnClicked() {
        dismiss()
    }

}
