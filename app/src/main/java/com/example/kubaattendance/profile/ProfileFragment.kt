package com.example.kubaattendance.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.profile.ProfileResponse
import com.example.kubaattendance.databinding.ProfileFragmentBinding
import com.example.kubaattendance.utils.*
import kotlinx.android.synthetic.main.profile_fragment.*
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileFragment : Fragment(),IProfile {



    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: ProfileFragmentBinding

    var IMG_REQUEST = 777

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater,R.layout.profile_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.profileViewModelFactory).get(ProfileViewModel::class.java)
        binding.profileViewModel = viewModel
        viewModel.iProfile = this

        requestReadPhoneStatePermission()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile()

    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE
    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }
    override fun displayProfile(result: Any) {
        progress_layout.visibility = View.GONE

        if (result is ProfileResponse)
        {
            if(result.success.equals("1"))
            {
                var profileInfo = result.profile.get(0)
                viewModel.name = profileInfo.name
                viewModel.mobile = profileInfo.mobile
                viewModel.email = profileInfo.email
                viewModel.address = profileInfo.address
                viewModel.registered_at = T.getFormatedDate(profileInfo.created_at!!)
                binding.profileViewModel = viewModel
                //set profile image
                if(profileInfo.photo != null)
                    T.setImage(profile_image,progress_img,activity!!,Constants.MEMBER_IMAGE_PATH+""+profileInfo.photo)

            }

        }
        else if(result is CommonResponse)
            activity!!.toast(result.message!!)
    }

    override fun updateProfile(result: Any) {
        progress_layout.visibility = View.GONE

        var result = result as CommonResponse
        activity!!.toast(result.message!!)
    }
    override fun selectProfileImage() {

        val packageList = arrayListOf<String>()

        packageList.add("Gallery")
        packageList.add("Camera")

        // setup the alert builder
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Select Media")
        builder.setItems(packageList.toTypedArray()) { dialog, which ->
            dialog.dismiss()
            when(packageList.get(which))
            {
                "Gallery" -> selectImageFromGallery()
                "Camera" -> selectImageFromCamera()
            }
        }
        val dialog = builder.create()
        dialog.show()

    }
    fun selectImageFromGallery()
    {
        Intent().also {
            it.setType("image/*")
            it.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(it,IMG_REQUEST)
        }
    }
    private val PERMISSIONS_REQUEST_CAMERA = 0
    private fun requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CAMERA
            )
        } else {
            // CAMERA permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CAMERA
            )
        }
    }
    var CAMERA_REQUEST : Int = 1888
    fun selectImageFromCamera()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            startActivityForResult(it,CAMERA_REQUEST)
        }
    }
    lateinit var bitmap : Bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMG_REQUEST && data != null)
        {
            var path = data.data
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver,path)
                profile_image.setImageBitmap(bitmap)
                var image_code = imageToString()
                viewModel.updateProfileImage(image_code)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
        else if (requestCode == CAMERA_REQUEST)
        {
            try
            {
                bitmap = data!!.getExtras()!!.get("data") as Bitmap
                profile_image.setImageBitmap(bitmap)
                var image_code = imageToString()
                viewModel.updateProfileImage(image_code)
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
    }
    fun imageToString() : String
    {
        var byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        var imgByte = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgByte,Base64.DEFAULT)
    }


}


