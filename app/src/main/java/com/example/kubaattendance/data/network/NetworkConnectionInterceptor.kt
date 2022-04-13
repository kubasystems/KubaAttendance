package com.example.mvvmkotlinretrofitroomkodeindatabinding.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.kubaattendance.util.NoInternetException
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException(Constants.CONNECTION)
        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}