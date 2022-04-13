package com.example.kubaattendance.actors.gym_owner.pkg.item_classs

import com.example.kubaattendance.R
import com.example.kubaattendance.actors.gym_owner.pkg.data_class.DPkgInfo
import com.example.kubaattendance.databinding.PkgsItemsBinding
import com.xwray.groupie.databinding.BindableItem

class PkgInfoItem(private val dPkgInfo: DPkgInfo) : BindableItem<PkgsItemsBinding>() {
    override fun getLayout(): Int = R.layout.pkgs_items

    override fun bind(viewBinding: PkgsItemsBinding, position: Int) {
        viewBinding.dPkgInfo = dPkgInfo
    }
}