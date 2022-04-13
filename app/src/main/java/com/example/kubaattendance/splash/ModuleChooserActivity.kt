package com.example.kubaattendance.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.authentication.AuthViewModel
import com.example.kubaattendance.authentication.LoginActivity
import com.example.kubaattendance.authentication.other_user.OtherUserLoginActivity
import com.example.kubaattendance.databinding.ActivityModuleChooserBinding
import com.example.kubaattendance.utils.MyApplication

class ModuleChooserActivity : AppCompatActivity(), IModuleChooser {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding : ActivityModuleChooserBinding = DataBindingUtil.setContentView(this,R.layout.activity_module_chooser)
        val authViewModel = ViewModelProviders.of(this, MyApplication.authViewModelFactory).get(
            AuthViewModel::class.java)
        binding.authViewModel = authViewModel
        authViewModel.iModuleChooser = this
    }
    override fun onBackButtonClicked() {
        finish()
    }

    override fun onGymDeviceLoginBtnClicked() {

        Intent(this,LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    override fun onUsersLoginBtnClicked() {
        Intent(this,OtherUserLoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
