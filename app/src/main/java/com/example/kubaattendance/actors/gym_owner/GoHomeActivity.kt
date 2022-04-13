package com.example.kubaattendance.actors.gym_owner

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.kubaattendance.R
import com.phelat.navigationresult.FragmentResultActivity
import kotlinx.android.synthetic.main.activity_go_home.*

class GoHomeActivity : FragmentResultActivity() {


    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_home)

        setSupportActionBar(toolBar)
        toolBar.setTitleTextColor(Color.WHITE)
        
        //setup nav controller
        navController = Navigation.findNavController(this,R.id.go_nav_fragment)
        NavigationUI.setupWithNavController(sa_nav_view,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,sa_drawer_layout)
    }
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this,R.id.go_nav_fragment),
            sa_drawer_layout
        )
    }
    override fun getNavHostFragmentId(): Int = R.id.go_nav_fragment
}
