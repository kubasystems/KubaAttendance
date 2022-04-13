package com.example.kubaattendance.utils.custom_spinner

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.super_admin.gym_owner.DOwnerInfo
import com.example.kubaattendance.databinding.LayoutCustomSpinnerBinding
import com.xwray.groupie.databinding.BindableItem

class CustomSpinnerItem(private val dOwnerInfo: DOwnerInfo,private val iCustomSpinner: ICustomSpinner) : BindableItem<LayoutCustomSpinnerBinding>(){

    override fun getLayout(): Int = R.layout.layout_custom_spinner

    override fun bind(viewBinding: LayoutCustomSpinnerBinding, position: Int) {
        viewBinding.dOwnerInfo = dOwnerInfo
        viewBinding.itemLi.setOnClickListener {
            iCustomSpinner.returnDataToFragment(dOwnerInfo)
        }
    }
}