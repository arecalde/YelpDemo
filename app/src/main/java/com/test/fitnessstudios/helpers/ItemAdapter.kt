package com.test.fitnessstudios.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter<T, B : ViewDataBinding>(private val items: List<T>, private val lifecycleOwner: LifecycleOwner, private val layout: Int, private val setDatabindingItem: (T, B) -> Unit) : RecyclerView.Adapter<MyViewHolder<T, B>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder<T, B> {
        val itemBinding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return MyViewHolder(itemBinding, lifecycleOwner, setDatabindingItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T, B>, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size
}

class MyViewHolder<T, B: ViewDataBinding>(private val binding: B, private val lifecycleOwner: LifecycleOwner, private val setDatabindingItem: (T, B) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.lifecycleOwner = lifecycleOwner
        setDatabindingItem(item, binding)
        binding.executePendingBindings()
    }
}