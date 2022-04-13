package com.example.kubaattendance.actors.gym_owner.pkg

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.pkg.data_class.DPkgInfo
import com.example.kubaattendance.actors.gym_owner.pkg.item_classs.PkgInfoItem
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.data.network.responses.gym_owner.MyGymsResponse
import com.example.kubaattendance.data.network.responses.gym_owner.PackagesResponse
import com.example.kubaattendance.databinding.PkgsFragmentBinding
import com.example.kubaattendance.utils.Constants
import com.example.kubaattendance.utils.MyApplication
import com.example.kubaattendance.utils.custom_spinner.CustomSpinnerDataHolder
import com.example.kubaattendance.utils.toast
import com.phelat.navigationresult.BundleFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.pkgs_fragment.*

class PkgsFragment : BundleFragment(), IPkgs {

    private lateinit var viewModel: PkgViewModel
    private lateinit var binding: PkgsFragmentBinding

    var gym_names =  ArrayList<DOwnerInfo>()
    var PACKAGES =  ArrayList<DPkgInfo>()
    var TAG = "PKGS_FRAGMENT : "
    lateinit var customSpinnerDataHolder: CustomSpinnerDataHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding  = DataBindingUtil.inflate(inflater, R.layout.pkgs_fragment, container, false)
        viewModel = ViewModelProviders.of(this,MyApplication.pkgViewModelFactory).get(PkgViewModel::class.java)
        viewModel.iPkgs = this
        viewModel.operation_flag = "iPkgs"
        binding.pkgViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search ediext
        search_edt.visibility = View.GONE
        search_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var filteredModelList = filter(PACKAGES,""+s)

                if(filteredModelList.size > 0)
                    initiRecyclerView(filteredModelList.toQuoteItem())

            }

        })
        if(PACKAGES.size > 0)
            initiRecyclerView(PACKAGES.toQuoteItem())
        else
            viewModel.getGymDetails()
    }
    fun filter(models : ArrayList<DPkgInfo>, query : String) : ArrayList<DPkgInfo>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<DPkgInfo>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : DPkgInfo in models)
                {
                    var name = model.name!!.toLowerCase()
                    var amount = model.amount!!.toLowerCase()
                    var duration = model.duration!!.toLowerCase()
                    var discount = model.discount!!.toLowerCase()

                    if(name!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(amount!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(duration!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(discount!!.contains(query))
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
        progressMsg_tv.setText("Authenticating, please wait...")
    }
    override fun showProgress(message: String) {
        progress_layout.visibility = View.VISIBLE
        progressMsg_tv.setText(message)
    }

    override fun selectGymBtnClicked() {

        if(customSpinnerDataHolder.data.size > 0)
        {
            var action = PkgsFragmentDirections.customSpinnerData()
            action.setCustomSpinnerDataHolder(customSpinnerDataHolder)
            navigate(action,855)
        }
        else
            activity!!.toast("Oops ! Gym owners not found")
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)

        if(requestCode == 888)
        {
            if(bundle != null)
            {
                var refreshFlag = bundle.getString("refresh_flag")
                if(refreshFlag.equals("1"))
                    viewModel.getPackages(viewModel.gym_id!!)
            }
        }
        else if(requestCode == 855)
        {
            if(bundle != null)
            {
                var flag = bundle.getString("flag")
                if(flag.equals("set"))
                {
                    var gym_owner_id = bundle.getString("gym_owner_id")
                    var gym_owner_name = bundle.getString("gym_owner_name")

                    viewModel.gym_id = gym_owner_id
                    viewModel.gymName = gym_owner_name!!
                    binding.pkgViewModel = viewModel

                    viewModel.getPackages(gym_owner_id!!)
                }
            }
        }
    }
    override fun createPkgFabClicked(view : View) {
        val action = PkgsFragmentDirections.actionCreatePackage()
        action.setPkgViewModel(viewModel)
        navigate(action,888)
    }


    override fun hideProgress() {
    }
    override fun onFailure(message: String) {
        progress_layout.visibility = View.GONE
    }

    override fun onResponse(data: Any) {
        progress_layout.visibility = View.GONE

        if(data is PackagesResponse)
        {
            Log.e(Constants.KUBA_LOGS,"data.success : "+data.success)
            if(data.success.equals("1"))
            {
                PACKAGES.clear()
                search_edt.visibility = View.VISIBLE
                recyclerview.visibility = View.VISIBLE
                PACKAGES = data.pkg_info as ArrayList<DPkgInfo>
                initiRecyclerView(PACKAGES.toQuoteItem())
            }
            else
            {
                search_edt.visibility = View.GONE
                recyclerview.visibility = View.GONE
                activity!!.toast(data.message!!)
                PACKAGES.clear()
                initiRecyclerView(PACKAGES.toQuoteItem())

            }


        }
        else if(data is MyGymsResponse)
        {
            if(data.success.equals("1"))
            {
                gym_names.clear()
                for (i in 0 until data.gyms.size)
                {
                    var gym = data.gyms.get(i)
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
                activity!!.toast(data.message!!)
        }
    }

    fun initiRecyclerView(pkgInfoItem: List<PkgInfoItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(pkgInfoItem)
            notifyDataSetChanged()
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }
    private fun List<DPkgInfo>.toQuoteItem() : List<PkgInfoItem>{
        return this.map {
            PkgInfoItem(it)
        }
    }
    override fun showToast(message: String) {

        activity!!.toast(message)
    }


}