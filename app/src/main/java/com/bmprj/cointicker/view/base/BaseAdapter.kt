package com.bmprj.cointicker.view.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<DB:ViewDataBinding,T:Any>(open var list:ArrayList<T>)
    :RecyclerView.Adapter<BaseViewHolder<DB>>(){

    @get:LayoutRes
    abstract val layoutId:Int
    abstract fun bind(binding: DB,item:T)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> {
        val binder = DataBindingUtil.inflate<DB>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return BaseViewHolder(binder)
    }

    override fun getItemCount():Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
        bind(holder.binder,list[position])
    }

    fun updateList(newList:ArrayList<T>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


}