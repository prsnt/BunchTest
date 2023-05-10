package com.bunchapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunchapp.EmployeeAdapter.ViewHolder
import com.bunchapp.databinding.ItemListBinding
import com.bunchapp.model.Employee

class EmployeeAdapter(private val list: List<Employee>, private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    var binding: ItemListBinding? = null

    class ViewHolder(private val binding: ItemListBinding?) : RecyclerView.ViewHolder(binding?.root!!) {

        fun bindData(employee: Employee) {
            binding?.employee = employee
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_list, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }
}