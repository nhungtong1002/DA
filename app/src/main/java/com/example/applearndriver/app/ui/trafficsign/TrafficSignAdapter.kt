package com.example.applearndriver.app.ui.trafficsign

import android.view.ViewGroup
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.data.model.TrafficSigns
import com.example.applearndriver.databinding.ItemTrafficSignLayoutBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.extensions.loadGlideImageFromUrl

class TrafficSignAdapter :
    BaseRecyclerViewAdapter<TrafficSigns, ItemTrafficSignLayoutBinding, TrafficSignAdapter.ViewHolder>(
        TrafficSigns.getDiffUtilCallback()) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<TrafficSigns>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemTrafficSignLayoutBinding::inflate, parent))
    }

    inner class ViewHolder(
        override val binding: ItemTrafficSignLayoutBinding,
    ) : BaseViewHolder<TrafficSigns, ItemTrafficSignLayoutBinding>(binding) {
        override fun onBindData(data: TrafficSigns) {
            binding.apply {
                imageTrafficSign.loadGlideImageFromUrl(
                    root.context,
                    data.imageUrl,
                )
                textTrafficSignTitle.text = data.title
                textTrafficSignDescription.text = data.description.ifBlank { "Không có giải thích!!" }

                root.setOnClickListener {
                    clickItemInterface?.let { function -> function(data) }
                }
            }
        }
    }
}
