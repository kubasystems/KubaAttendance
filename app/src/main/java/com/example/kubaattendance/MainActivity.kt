package com.example.kubaattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kubaattendance.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import net.glxn.qrgen.android.QRCode
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    private fun createBarcode() {

        //barcode_img
        try
        {
            var myBitmap = QRCode.from("8806283610").bitmap()
            barcode_img.setImageBitmap(myBitmap)

        }
        catch (e : Exception)
        {
            Log.e(Constants.KUBA_LOGS,""+e)
        }
    }

    fun generateBarcode(view: View) {
        createBarcode()
    }
}
