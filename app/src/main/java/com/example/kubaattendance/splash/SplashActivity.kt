package com.example.kubaattendance.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_device.activity.GdHomeActivity
import com.example.kubaattendance.actors.gym_member.GmHomeActivity
import com.example.kubaattendance.actors.gym_owner.GoHomeActivity
import com.example.kubaattendance.actors.gym_reception_user.ReceptionPersonActivity
import com.example.kubaattendance.actors.super_admin.SaHomeActivity
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var mdelayHandler : Handler
    private var splashDelay : Long = 2000//2 sec

    var TAG = "SPLASH_ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        version_tv.setText("v"+ T.getAppVersion(this))
        //initialise the handler
        mdelayHandler = Handler()
        //navigate with delay
        mdelayHandler.postDelayed(mRunnable,splashDelay)
    }
    internal  val mRunnable : Runnable = Runnable {

        if(!isFinishing)
        {
            val GYM_BRANCH_ID = MyApplication.prefs.getString(Constants.GYM_BRANCH_ID,"0")

            if(GYM_BRANCH_ID.equals("0"))
            {
                val USER_TYPE = MyApplication.prefs.getString(Constants.USER_TYPE,"0")
                if(USER_TYPE.equals("0"))
                {
                    val intent = Intent(this, ModuleChooserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    if(USER_TYPE.equals("super_admin"))
                    {
                        Intent(this,SaHomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    else if(USER_TYPE.equals("gym_owner"))
                    {
                        Intent(this,GoHomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    else if(USER_TYPE.equals("gym_member"))
                    {
                        Intent(this,
                            GmHomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    else if(USER_TYPE.equals("reception_person"))
                    {
                        Intent(this,ReceptionPersonActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                    else if(USER_TYPE.equals("gym_device"))
                    {
                        Intent(this,GdHomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }

                }
            }
            else
            {
                Intent(this,GdHomeActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }


        }
    }


    override fun onDestroy()
    {
        super.onDestroy()
        mdelayHandler.removeCallbacks(mRunnable)

    }
}
