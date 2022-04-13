package com.example.kubaattendance.actors.super_admin

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.kubaattendance.R
import com.phelat.navigationresult.FragmentResultActivity
import kotlinx.android.synthetic.main.activity_sa_home.*

class SaHomeActivity : FragmentResultActivity(){



    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sa_home)

        setSupportActionBar(toolBar)
        toolBar.setTitleTextColor(Color.WHITE);
        //setup nav controller
        navController = Navigation.findNavController(this,R.id.sa_nav_fragment)
        NavigationUI.setupWithNavController(sa_nav_view,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,sa_drawer_layout)

    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this,R.id.sa_nav_fragment),
            sa_drawer_layout
        )
    }
    override fun getNavHostFragmentId(): Int = R.id.sa_nav_fragment

}
