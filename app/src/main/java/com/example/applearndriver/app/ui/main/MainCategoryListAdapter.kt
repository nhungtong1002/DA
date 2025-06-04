package com.example.applearndriver.app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.data.model.MainCategoryModel
import com.example.applearndriver.databinding.ItemMainScreenCategoryLayoutBinding
import com.example.applearndriver.utils.OnClickItem

class MainCategoryListAdapter :
    BaseRecyclerViewAdapter<MainCategoryModel, ItemMainScreenCategoryLayoutBinding,
            MainCategoryListAdapter.ViewHolder>(MainCategoryModel.getDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMainScreenCategoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(
        override val binding: ItemMainScreenCategoryLayoutBinding,
    ) : BaseViewHolder<MainCategoryModel, ItemMainScreenCategoryLayoutBinding>(binding) {
        override fun onBindData(data: MainCategoryModel) {
            binding.apply {
                imageCategory.setImageResource(data.resourceID)
                textCategory.text = data.title

                root.setOnClickListener {
                    clickItemInterface?.let { function -> function(data) }
                }
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<MainCategoryModel>) {
        this.clickItemInterface = onClickItem
    }
}
