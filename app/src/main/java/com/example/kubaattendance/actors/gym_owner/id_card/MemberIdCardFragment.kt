package com.example.kubaattendance.actors.gym_owner.id_card

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil

import com.example.kubaattendance.R
import com.example.kubaattendance.data.network.responses.gym_owner.id_card.MemberIdCardResponse
import com.example.kubaattendance.databinding.MemberIdCardFragmentBinding
import com.example.kubaattendance.utils.*
import kotlinx.android.synthetic.main.member_id_card_fragment.*
import net.glxn.qrgen.android.QRCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MemberIdCardFragment : Fragment(), IMemberIdCardCallbacks {



    private lateinit var viewModel: MemberIdCardViewModel
    private lateinit var binding: MemberIdCardFragmentBinding

    private var passingData : String? = null

    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater,R.layout.member_id_card_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.memberIdCardViewModelFactory).get(MemberIdCardViewModel::class.java)
        binding.memberIdCardViewModel = viewModel
        viewModel.iBaseListner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()

        arguments!!.let {

            passingData = MemberIdCardFragmentArgs.fromBundle(it).passingData
            var member_id = passingData
            viewModel.getIdCardDetails(member_id!!)
        }


    }
    private val PERMISSIONS_REQUEST_CAMERA = 0
    private fun requestPermission() {

        if(!T.hasPermissions(activity!!,PERMISSIONS))
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CAMERA)

    }

    override fun showProgress() {

        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Getting Members, please wait...")
    }

    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {

        progress_layout.visibility = View.GONE
        var response = data as MemberIdCardResponse
        if(response.success.equals("1"))
        {
            var idCardData = response.member_id_info.get(0)
            viewModel.gymName = idCardData.gymName
            viewModel.gymBranchCode = idCardData.gymBranchCode
            viewModel.memberName = idCardData.memberName
            viewModel.qrCodeContent = idCardData.memberPhone+"@"+idCardData.qrCodeContent
            viewModel.memberAddress = idCardData.memberAddress
            if(idCardData.memberDOJ!!.contains(":"))
            {
                var dateData = idCardData.memberDOJ!!.split(" ")
                viewModel.memberDOJ = T.getFormatedDate(dateData.get(0))+" "+dateData.get(1)
            }
            viewModel.memberPhone = idCardData.memberPhone
            viewModel.memberEmail = idCardData.memberEmail
            binding.memberIdCardViewModel = viewModel

            //set qr image
            var myBitmap = QRCode.from(idCardData.memberPhone+"@"+idCardData.qrCodeContent).bitmap()
            qr_code_img.setImageBitmap(myBitmap)

            //set member image
            if(idCardData.memberImage.equals("null") || idCardData.memberImage.equals("NULL") || idCardData.memberImage.equals("") || idCardData.memberImage == null)
            {

            }
            else
            {
                var path = Constants.MEMBER_IMAGE_PATH+""+idCardData.memberImage
                Log.e(Constants.KUBA_LOGS,"path : "+path)
                T.setImage(member_photo_img,progress_img,activity!!,path)
            }
        }
        else
            activity!!.toast(response.message!!)
    }

    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
    }

    override fun showToast(message: String) {

        activity!!.toast(message)
    }
    lateinit var uri : Uri
    override fun saveIdCardBtnClicked(member_details: String) {

        generateIdCard(member_details,"save")
    }
    override fun shareIdCardBtnClicked(member_details : String) {

        generateIdCard(member_details,"share")

    }
    fun generateIdCard(member_details : String,flag : String)
    {
        main_li.setDrawingCacheEnabled(true)
        main_li.buildDrawingCache()
        val bm = Bitmap.createBitmap(main_li.getDrawingCache())
        main_li.setDrawingCacheEnabled(false) // clear drawing cache
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpg"

        val bytes = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        //store bitmap into sd card
        var dirPath = Environment.getExternalStorageDirectory().absolutePath + "/KuBa/Members_Id"
        var dir = File(dirPath)
        if(!dir.exists())
            dir.mkdirs()

        var file = File(dirPath,member_details+".png")
        try
        {
            var fout = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG,100,fout)
            fout.flush()
            fout.close()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

        if(flag.equals("save"))
            activity!!.toast("Member ID card saved successfully into the sd card")
        else if(flag.equals("share"))
        {
            //last share the saved screenshot
            //var uri = Uri.fromFile(file)
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                uri = FileProvider.getUriForFile(activity!!,activity!!.applicationContext.packageName+".provider",file)
            else
                uri = Uri.fromFile(file)

            Intent().also {
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                it.setAction(Intent.ACTION_SEND)
                it.setType("image/*")
                it.putExtra(Intent.EXTRA_SUBJECT, "")
                it.putExtra(Intent.EXTRA_TEXT, "")
                it.putExtra(Intent.EXTRA_STREAM, uri)
                try
                {
                    activity!!.startActivity(it)
                }
                catch (e : ActivityNotFoundException)
                {
                    activity!!.toast("No App Available to share")
                }
            }
        }

    }
}
