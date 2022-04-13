package com.example.kubaattendance.actors.gym_owner.pkg

import android.util.Log
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kubaattendance.data.repositories.GymOwnerRepository
import com.example.kubaattendance.util.Coroutines
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.T
import java.io.Serializable

class PkgViewModel(private val gymOwnerRepository: GymOwnerRepository) : ViewModel(),Serializable {

    //create pkg fields
    var operation_flag : String = ""
    var gymName : String = "Select Gym"
    var pkg_name : String? = null
    var pkg_amount : String? = null
    var pkg_duration : String? = null
    var pkg_month_or_year : String? = null
    var pkg_discount : String? = null
    var gym_id : String? = null

    var duration_hint : String = "Enter duration"

    var iPkgs : IPkgs? = null
    var iCreatePackages : ICreatePackages? = null

    var TAG = "PKG_VIEW_MODEL"


    fun getGymDetails()
    {
        if(!T.isNetworkAvailable())
        {
            if(operation_flag.equals("iPkgs"))
                iPkgs!!.showToast(Constants.CONNECTION)
            else if(operation_flag.equals("iCreatePackages"))
                iCreatePackages!!.showToast(Constants.CONNECTION)
            return
        }

        if(operation_flag.equals("iPkgs"))
            iPkgs!!.showProgress("Getting Gyms...")
        else if(operation_flag.equals("iCreatePackages"))
            iCreatePackages!!.showProgress("Getting Gyms...")

        Coroutines.main {

            try {

                var myGymsResponse = gymOwnerRepository.getAllGymOwnerGyms(
                    MyApplication.prefs.getString(Constants.USER_ID,"0")!!)

                myGymsResponse.success?.let {

                    if(operation_flag.equals("iPkgs"))
                        iPkgs!!.onResponse(myGymsResponse)
                    else if(operation_flag.equals("iCreatePackages"))
                        iCreatePackages!!.onResponse(myGymsResponse)
                    return@main
                }

                if(operation_flag.equals("iPkgs"))
                    iPkgs!!.onFailure(myGymsResponse.message!!)
                else if(operation_flag.equals("iCreatePackages"))
                    iCreatePackages!!.onFailure(myGymsResponse.message!!)
            }
            catch (e : Exception)
            {
                if(operation_flag.equals("iPkgs"))
                    iPkgs!!.onFailure(e.message!!)
                else if(operation_flag.equals("iCreatePackages"))
                    iCreatePackages!!.onFailure(e.message!!)
            }
        }
    }
    fun getPackages(gym_id : String) {

        if(!T.isNetworkAvailable())
        {
            iPkgs!!.showToast(Constants.CONNECTION)
            return
        }

        iPkgs!!.showProgress("Getting packages...")
        Coroutines.main {
            try {
                var packagesResponse = gymOwnerRepository.getPackages(gym_id)
                packagesResponse.success?.let {

                    iPkgs!!.onResponse(packagesResponse)
                    return@main
                }
                iPkgs!!.onFailure(packagesResponse.message!!)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun onCreatePackageClicked(view : View)
    {
        if(operation_flag.equals("iPkgs"))
        {
             if(gymName.isNullOrEmpty() || gymName.isNullOrBlank())
             {
                 iCreatePackages!!.showToast("Enter gym name")
                 return
             }
        }
        else if(operation_flag.equals("iCreatePackages"))
        {
            if(gymName.equals("Select Gym"))
            {
                iCreatePackages!!.showToast("Select gym name first")
                return
            }
        }

        if(pkg_name.isNullOrEmpty() || pkg_name.isNullOrBlank())
        {
            iCreatePackages!!.showToast("Enter package name")
            return
        }
        if(pkg_month_or_year.isNullOrEmpty() || pkg_month_or_year.isNullOrBlank())
        {
            iCreatePackages!!.showToast("Select package type")
            return
        }
        if(pkg_duration.isNullOrEmpty() || pkg_duration.isNullOrBlank())
        {
            iCreatePackages!!.showToast("Enter package duration.")
            return
        }

        if(pkg_month_or_year.equals("m"))
        {
            var months = pkg_duration!!.toInt()
            if(months > 12 || months < 1)
            {
                iCreatePackages!!.showToast("Entered Month should be between 1 to 12")
                return
            }

        }
        if(pkg_month_or_year.equals("y"))
        {
            var years = pkg_duration!!.toInt()
            if(years > 5 || years < 1)
            {
                iCreatePackages!!.showToast("Entered Years should be between 1 to 5")
                return
            }

        }
        if(pkg_amount.isNullOrEmpty() || pkg_amount.isNullOrBlank())
        {
            iCreatePackages!!.showToast("Enter package amount.")
            return
        }

        if(pkg_discount.isNullOrEmpty() || pkg_discount.isNullOrBlank())
        {
            iCreatePackages!!.showToast("Enter package discount.")
            return
        }
        iCreatePackages!!.showProgress("Creating package...")
        if(T.isNetworkAvailable())
        {
            Coroutines.main {

                try
                {
                    var commonResponse = gymOwnerRepository.createPackage(
                        MyApplication.prefs.getString(Constants.USER_NAME,"0")!!,
                        gym_id!!,
                        pkg_name!!,
                        pkg_amount!!,
                        pkg_duration!!,
                        pkg_month_or_year!!,
                        pkg_discount!!
                    )
                    commonResponse.success?.let {

                        iCreatePackages!!.onResponse(commonResponse)
                        return@main
                    }
                    iCreatePackages!!.onFailure(commonResponse.message!!)
                }
                catch (e : Exception)
                {
                    iCreatePackages!!.onFailure(e.message!!)
                    e.printStackTrace()
                }
            }
        }
        else
            iCreatePackages!!.showToast(Constants.CONNECTION)
    }
    fun onSelectGymButtonClicked(view : View)
    {

        if(operation_flag.equals("iPkgs"))
            iPkgs!!.selectGymBtnClicked()
        else if(operation_flag.equals("iCreatePackages"))
            iCreatePackages!!.selectGymBtnClicked()

    }
    fun createPkgFabClicked(view : View)
    {
        if(gymName.equals("Select Gym"))
        {
            iPkgs!!.showToast("Select Gym First")
            return
        }
        iPkgs!!.createPkgFabClicked(view)
    }

    fun monthlyRadioClicked(view : View)
    {
        iCreatePackages!!.durationHintChanged("monthly")
    }
    fun yearlyRadioClicked(view : View)
    {
        iCreatePackages!!.durationHintChanged("yearly")
    }

}
