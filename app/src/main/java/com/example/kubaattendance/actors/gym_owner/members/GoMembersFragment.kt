package com.example.kubaattendance.actors.gym_owner.members

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.members.data_classes.DMemberInfo
import com.example.kubaattendance.actors.gym_owner.members.item_classes.MemberInfoItem
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.CommonResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MembersResponse
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.databinding.GoMembersFragmentBinding
import com.example.kubaattendance.utils.*
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.go_members_fragment.*
import kotlinx.android.synthetic.main.not_found_layout.*


class GoMembersFragment : BundleFragment(), IGoMembers {

    private lateinit var viewModel: GoMembersViewModel
    private lateinit var binding: GoMembersFragmentBinding
    var gym_names =  ArrayList<DOwnerInfo>()
    var members =  ArrayList<DMemberInfo>()


    lateinit var customSpinnerDataHolder: CustomSpinnerDataHolder

    var TAG = "GO_MEMBERS_FRAGMENT : "
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Log.e(Constants.KUBA_LOGS,TAG+"onCreateView called\nmembers.size() : "+members.size)
        binding = DataBindingUtil.inflate(inflater, R.layout.go_members_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.goMembersViewModelFactory).get(GoMembersViewModel::class.java)
        viewModel.operation_flag = "iGoMembers"
        binding.goMembersViewModel = viewModel
        viewModel.iGoMembers = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.e(Constants.KUBA_LOGS,TAG+"onViewCreated called called\nmembers.size() : "+members.size)


        //search ediext
        search_edt.visibility = View.GONE
        search_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var filteredModelList = filter(members,""+s)

                if(filteredModelList.size > 0)
                    initiRecyclerView(filteredModelList.toQuoteItem())

            }
        })
        if(members.size > 0)
            initiRecyclerView(members.toQuoteItem())
        else
            viewModel.getGymDetails()
    }

    fun filter(models : ArrayList<DMemberInfo>, query : String) : ArrayList<DMemberInfo>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<DMemberInfo>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : DMemberInfo in models)
                {
                    var gym_member_name = model.gym_member_name!!.toLowerCase()
                    var pkg_name = model.pkg_name!!.toLowerCase()
                    var pkg_from_date = model.pkg_from_date!!.toLowerCase()
                    var pkg_to_date = model.pkg_to_date!!.toLowerCase()
                    var gym_member_mobile = model.gym_member_mobile!!.toLowerCase()
                    var gym_member_email = model.gym_member_email!!.toLowerCase()

                    if(gym_member_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(pkg_name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(pkg_from_date!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(pkg_to_date!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_member_mobile!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(gym_member_email!!.contains(query))
                        filterd_gym_owners.add(model)
                }
            }
            catch (e : Exception)
            {
                e.printStackTrace()

            }
        }
        return filterd_gym_owners
    }
    override fun showProgress() {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText("Getting Members, please wait...")
    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }
    override fun hideProgress() {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(result: Any)
    {
        progress_layout.visibility = View.GONE

        if(result is MembersResponse)
        {
            members.clear()
            if(result.success.equals("1"))
            {
                search_edt.visibility = View.VISIBLE
                recyclerview.visibility = View.VISIBLE
                hide_layout.visibility = View.GONE
                members = result.members as ArrayList<DMemberInfo>
                initiRecyclerView(members.toQuoteItem())
            }
            else
            {
                search_edt.visibility = View.GONE
                initiRecyclerView(members.toQuoteItem())
                recyclerview.visibility = View.GONE
                hide_layout.visibility = View.VISIBLE
                text_data.setText(result.message!!)
                image_data.setImageResource(R.drawable.ic_member_nf)
            }
        }
        else if(result is MyGymsResponse)
        {
            if(result.success.equals("1"))
            {

                gym_names.clear()
                for (i in 0 until result.gyms.size)
                {
                    var gym = result.gyms.get(i)
                    gym_names.add(DOwnerInfo(
                        "",
                        gym.gym_id,
                        "",
                        gym.gym_name,
                        "",
                        ""))
                }

                customSpinnerDataHolder = CustomSpinnerDataHolder(gym_names)
            }
            else
                activity!!.toast(result.message!!)
        }
        else if(result is CommonResponse)
        {
            if(result.success.equals("1"))
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog{
                        override fun onConfirmClickListener() {
                            viewModel.getMembers(viewModel.gym_id!!)
                        }

                        override fun onCancelClickListener() {
                            viewModel.getMembers(viewModel.gym_id!!)
                        }

                    },
                    Constants.SA_SUCCESS,
                    "Success",
                    result.message!!,
                    "Ok",
                    "Cancel")
            }
            else
            {
                T.displaySuccessFailureDialog(
                    activity!!,
                    object : ISweetDialog{
                        override fun onConfirmClickListener() {

                        }

                        override fun onCancelClickListener() {

                        }

                    },
                    Constants.SA_ERROR,
                    "Fail",
                    result.message!!,
                    "Ok",
                    "Cancel")
            }
        }



    }
    fun initiRecyclerView(memberInfoItem: List<MemberInfoItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(memberInfoItem)
            notifyDataSetChanged()
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }
    private fun List<DMemberInfo>.toQuoteItem() : List<MemberInfoItem>{
        return this.map {
            MemberInfoItem(activity!!,it,viewModel)
        }
    }

    override fun showToast(message: String) {
        activity!!.toast(message)
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
        activity!!.toast(message)
    }
    override fun selectGymButtonClicked() {

        if(customSpinnerDataHolder.data.size > 0)
        {
            var action = GoMembersFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolder)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gyms not found")
    }
    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)

        if(requestCode == 855)
        {
            if(bundle != null)
            {
                var flag = bundle.getString("flag")

                if(flag.equals("set"))
                {
                    var gym_owner_id = bundle.getString("gym_owner_id")
                    var gym_owner_name = bundle.getString("gym_owner_name")

                    viewModel.gym_id = gym_owner_id
                    viewModel.gym_name = gym_owner_name!!
                    binding.goMembersViewModel = viewModel

                    viewModel.getMembers(gym_owner_id!!)

                }

            }
        }
        else if(requestCode == 856)
        {
            if(bundle != null)
            {
                var status = bundle.getString("refresh_flag")
                if(status.equals("1"))
                    viewModel.getMembers(bundle.getString("gym_id")!!)
            }
        }
    }

    override fun createMemberFabButtonClicked() {

        var action = GoMembersFragmentDirections.actionCreateGymMember()
        action.setPassingData("create_member#"+viewModel.gym_id+"#"+viewModel.gym_name)
        navigate(action,856)
    }
    override fun onActivateDeactivateGymOwnerBtnClicked(gym_member_user_id: String, is_active: String) {

        when(is_active)
        {
            "1"-> showOperationDialog("deactivate",gym_member_user_id,"0")
            "0"-> showOperationDialog("activate",gym_member_user_id,"1")
            else->{

            }
        }
    }
    lateinit var dialogTitle : String
    lateinit var dialogMessage : String

    fun showOperationDialog(flag : String,gym_member_user_id : String,is_active : String)
    {
        when(flag)
        {
            "activate"->{
                dialogTitle = "Activate Member"
                dialogMessage = "Do you want to Activate this Member?"
            }
            "deactivate"->{
                dialogTitle = "Deactivate Member"
                dialogMessage = "Do you want to Deactivate this Member?"
            }
            else->{

            }
        }
        T.displaySuccessFailureDialog(
            activity!!,
            object : ISweetDialog {
                override fun onConfirmClickListener() {
                    when(flag)
                    {
                        "activate"->{
                            viewModel.activateDeactivateGymOwner(gym_member_user_id,is_active)
                        }
                        "deactivate"->{
                            viewModel.activateDeactivateGymOwner(gym_member_user_id,is_active)
                        }
                        else->{

                        }
                    }
                }

                override fun onCancelClickListener() {

                }

            },
            Constants.SA_NORMAL,
            dialogTitle,
            dialogMessage,
            "Yes",
            "No")

    }
    override fun menuMembersOptionClicked(view : View,dMemberInfo : DMemberInfo) {

        var popup = PopupMenu(activity!!,view)
        popup.menuInflater.inflate(R.menu.members_menu_item,popup.menu)

        popup.setOnMenuItemClickListener {


            when(it.title)
            {
                "Edit Info"->{
                    var action = GoMembersFragmentDirections.actionCreateGymMember()
                    action.setPassingData(
                                "edit_info#"
                                +viewModel.gym_id+"#"
                                +viewModel.gym_name+"#"
                                +dMemberInfo.gym_member_user_id+"#"
                                +dMemberInfo.gym_member_name+"#"
                                +dMemberInfo.gym_member_mobile+"#"
                                +dMemberInfo.gym_member_email+"#"
                                +dMemberInfo.gym_member_address
                    )
                    navigate(action)
                }
                "Assign New Package"->{

                    var action = GoMembersFragmentDirections.actionCreateGymMember()
                    action.setPassingData(
                        "assign_new_pkg#"
                                +viewModel.gym_id+"#"
                                +viewModel.gym_name+"#"
                                +dMemberInfo.gym_member_id+"#"
                                +dMemberInfo.gym_member_user_id+"#"
                                +dMemberInfo.gym_member_name
                    )
                    navigate(action)
                }
                "View ID Card"->{

                    var action = GoMembersFragmentDirections.actionViewMemberIdCard()
                    action.setPassingData(dMemberInfo.gym_member_id)
                    navigate(action)


                }
                "Activate / Deactivate"->{
                    viewModel.onActivateDeactivateGymOwnerBtnClicked(dMemberInfo.gym_member_user_id!!,dMemberInfo.gym_member_is_active!!)
                }
                else->{

                }
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }
    override fun onSelectPackageButtonClicked() {

    }



}
