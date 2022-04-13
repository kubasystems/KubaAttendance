package com.example.kubaattendance.authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kubaattendance.R
import com.example.kubaattendance.data.models.User
import com.example.kubaattendance.data.network.responses.AuthResponse
import com.example.kubaattendance.data.network.responses.RegisterResponse
import com.example.kubaattendance.databinding.ActivityRegisterBinding
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import com.example.kubaattendance.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),AuthListner {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        val authViewModel = ViewModelProviders.of(this, MyApplication.authViewModelFactory).get(AuthViewModel::class.java)
        binding.authViewModel = authViewModel
        authViewModel.authListner = this

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
    override fun onBackPressed()
    {
        finish()
    }
    override fun onStarted() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Registering, please wait...")

    }

    override fun onSuccess(authResponse: AuthResponse) {

    }

    override fun onSuccess(registerResponse: RegisterResponse) {

        progress_layout.visibility = View.GONE

        when(registerResponse.success)
        {
            "0" -> toast(registerResponse.message!!)
            "1" ->
            {
                toast(registerResponse.message!!)
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            "2" -> toast(registerResponse.message!!)
            "3" -> toast(registerResponse.message!!)
            "4" -> toast(registerResponse.message!!)
            "5" -> toast(registerResponse.message!!)
        }
    }
    override fun onFailure(message: String) {

        progress_layout.visibility = View.GONE
    }
    override fun onDisplayToast(message: String) {
        T.s(root_layout,message)
    }
}
