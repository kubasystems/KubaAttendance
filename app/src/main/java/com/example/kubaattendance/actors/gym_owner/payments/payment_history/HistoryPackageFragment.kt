package com.example.kubaattendance.actors.gym_owner.payments.payment_history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.payments.GoMembPaymentViewModel
import com.example.kubaattendance.actors.gym_owner.payments.MemberPackages
import com.example.kubaattendance.databinding.FragmentHistoryPackageBinding
import com.example.kubaattendance.utils.MyApplication
import com.phelat.navigationresult.navigateUp
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_history_package.*


class HistoryPackageFragment : DialogFragment() {

    lateinit var binding : FragmentHistoryPackageBinding
    private lateinit var viewModel: GoMembPaymentViewModel
    private var packageHistoryDataHolder : PackageHistoryDataHolder? = null
    var packageHistoryData = ArrayList<MemberPackages>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history_package, container, false)
        viewModel = ViewModelProviders.of(this, MyApplication.goMembPaymentViewModelFactory).get(GoMembPaymentViewModel::class.java)
        binding.goMembPaymentViewModel = viewModel

        arguments!!.let {

            packageHistoryDataHolder = HistoryPackageFragmentArgs.fromBundle(it).packageHistoryDataHolder
            packageHistoryData = packageHistoryDataHolder!!.data
            if(packageHistoryData.size > 0)
                initRecyclerView(packageHistoryData.toQuoteItem())
        }
        return binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //search ediext
        search_edt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var filteredModelList = filter(packageHistoryData,""+s)

                if(filteredModelList.size > 0)
                    initRecyclerView(filteredModelList.toQuoteItem())

            }

        })
    }
    fun filter(models : ArrayList<MemberPackages>, query : String) : ArrayList<MemberPackages>
    {
        var query = query.toLowerCase()
        var filterd_gym_owners =  ArrayList<MemberPackages>()
        filterd_gym_owners.clear()

        if(query.length == 0)
            filterd_gym_owners.addAll(models)
        else
        {
            try
            {
                for(model : MemberPackages in models)
                {
                    var packageName = model.pkg_name!!.toLowerCase()
                    var packageAmount = model.pkg_amount!!.toLowerCase()
                    var packageFromDate = model.pkg_from_date!!.toLowerCase()
                    var packageToDate = model.pkg_to_date!!.toLowerCase()

                    if(packageName!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(packageAmount!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(packageFromDate!!.contains(query))
                        filterd_gym_owners.add(model)
                    else if(packageToDate!!.contains(query))
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
    private fun initRecyclerView(ownerInfoItem: List<PackageHistoryItem>)
    {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(ownerInfoItem)
            notifyDataSetChanged()
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }
    private fun List<MemberPackages>.toQuoteItem() : List<PackageHistoryItem>{
        return this.map {
            PackageHistoryItem(it,object : IPackageHistory{
                override fun returnResult(MemberPackages: MemberPackages) {

                    var package_id = MemberPackages.package_id ?: "0"
                    var pkg_name = MemberPackages.pkg_name ?: "0"
                    var pkg_amount = MemberPackages.pkg_amount ?: "0"
                    var pkg_from_date = MemberPackages.pkg_from_date ?: "0"
                    var pkg_to_date = MemberPackages.pkg_to_date ?: "0"

                    navigateUp(856,Bundle().apply {
                        putString("flag","set")
                        putString("package_id",package_id)
                        putString("pkg_name",pkg_name)
                        putString("pkg_amount",pkg_amount)
                        putString("pkg_from_date",pkg_from_date)
                        putString("pkg_to_date",pkg_to_date)
                    })
                }

            })
        }
    }



}
