package com.example.kubaattendance.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.text.ParseException


class T
{

    companion object
    {
        fun getAppVersion(context: Context) : String
        {
            var version = ""
            try
            {
                var pInfo = context.packageManager.getPackageInfo(context.packageName,0)
                version = pInfo.versionName
            }
            catch (e : Exception)
            {
                version = "NA"
            }
            return version
        }
        fun getFormatedDate(input_date : String) : String
        {
            var date = ""
            if(input_date.contains(" "))
            {
                var dateData = input_date.split(" ")
                date = dateData.get(0)
            }
            else
                date = input_date

            var dateData = date.split("-")
            var month = "0"
            when(dateData.get(1))
            {
                "01"-> month = "Jan"
                "02"-> month = "Feb"
                "03"-> month = "Mar"
                "04"-> month = "Apr"
                "05"-> month = "May"
                "06"-> month = "Jun"
                "07"-> month = "Jul"
                "08"-> month = "Aug"
                "09"-> month = "Sep"
                "10"-> month = "Oct"
                "11"-> month = "Nov"
                "12"-> month = "Dec"
            }
            return dateData.get(2)+" "+month+" "+dateData.get(0)
        }
        //check internet connection
        fun isNetworkAvailable(): Boolean
        {
            val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
        //snack bar
        fun s(view : View, message : String)
        {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val view = snackbar.view
            val params = view.layoutParams as CoordinatorLayout.LayoutParams
            params.gravity = Gravity.TOP
            view.layoutParams = params
            snackbar.show()
        }

        fun getSystemDateTime(): String? {
            var systemTime: String? = null
            try {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                systemTime = df.format(Calendar.getInstance().getTime())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return systemTime
        }

        fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
            if (context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }
        fun t(message : String)
        {
            Toast.makeText(MyApplication.context,message,Toast.LENGTH_LONG).show()
        }

        fun  getDeviceId() : String
        {
           return  Settings.Secure.getString(MyApplication.context.getContentResolver(), Settings.Secure.ANDROID_ID + "")
        }

        fun getNextDate(oldDate : String,noofDays : Int) : String
        {
            //var oldDAte = "2020-03-04"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            try
            {
                cal.time = dateFormat.parse(oldDate)
            }
            catch (e : ParseException)
            {
                Log.e(Constants.KUBA_LOGS,"getNetDate : "+ e)
                e.printStackTrace()
            }
            cal.add(Calendar.DAY_OF_MONTH,noofDays)
            var newDate = dateFormat.format(cal.time)
            return newDate
        }

        fun setImage(imageView : ImageView, progress_img : ProgressBar, context : Context, img_path : String)
        {
            Glide.with(context)
                .load(img_path)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progress_img.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progress_img.visibility = View.GONE
                        return false
                    }

                })
                .into(imageView);
        }
        var dialogType : Int = 0
        fun displaySuccessFailureDialog(context: Context,
                                        iSweetDialog: ISweetDialog,
                                        dialogTypeFlag : String,
                                        title : String,
                                        message : String,
                                        confirmText : String,
                                        cancelText : String)
        {
            if(dialogTypeFlag.equals(Constants.SA_SUCCESS))
                dialogType = SweetAlertDialog.SUCCESS_TYPE
            else if(dialogTypeFlag.equals(Constants.SA_WARNNING))
                dialogType = SweetAlertDialog.WARNING_TYPE
            else if(dialogTypeFlag.equals(Constants.SA_NORMAL))
                dialogType = SweetAlertDialog.NORMAL_TYPE
            else if(dialogTypeFlag.equals(Constants.SA_ERROR))
                dialogType = SweetAlertDialog.ERROR_TYPE

            SweetAlertDialog(context, dialogType)
                .setTitleText(title)
                .setConfirmText(confirmText)
                .setCancelText(cancelText)
                .setContentText(message)
                .setConfirmClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    iSweetDialog.onConfirmClickListener()
                }
                .setCancelClickListener { sDialog ->
                    sDialog.dismissWithAnimation()
                    iSweetDialog.onCancelClickListener()
                }
                .show()
        }
        fun timeStampToSeconds(timeStamp : String) : Long
        {
            var timeData = timeStamp.split(":")

            var h = timeData.get(0).toLong()
            var m = timeData.get(1).toLong()
            var s = timeData.get(2).toLong()

            var hs = h * 60 * 60
            var ms = m * 60

            return hs + ms + s
        }
        fun secondsToTime(seconds : Long) : String
        {
            var s = seconds % 60
            var h = seconds / 60
            var m = h % 60

            h = h / 60

            var hh  = if (h.toString().length > 1) h else "0"+h
            var mm  = if (m.toString().length > 1) m else "0"+m
            var ss  = if (s.toString().length > 1) s else "0"+s

            return hh.toString() + ":" + mm.toString() + ":" + ss.toString()
        }



    }
}