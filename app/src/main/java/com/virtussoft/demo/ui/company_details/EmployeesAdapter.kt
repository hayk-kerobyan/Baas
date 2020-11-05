package com.virtussoft.demo.ui.company_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.virtussoft.demo.R
import com.virtussoft.demo.model.employee.Employee
import kotlinx.android.synthetic.main.item_user.view.*


val differCallback = object : DiffUtil.ItemCallback<Employee>() {
    override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
        return oldItem.firstName == newItem.firstName
                && oldItem.lastName == newItem.lastName
                && oldItem.userId == newItem.userId
                && oldItem.companyId == newItem.companyId
                && oldItem.firstName == newItem.firstName
    }

}

class EmployeesAdapter : PagingDataAdapter<Employee, EmployeesAdapter.EmployeeVH>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeVH {
        return EmployeeVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EmployeeVH, position: Int) {
        getItem(position)?.let {
            holder.itemView.name.text = buildString {
                append(it.id);
                append("  :  ");
                append(it.firstName)
                append(" ")
                append(it.lastName)
            }
        }
    }

    class EmployeeVH(view: View) : RecyclerView.ViewHolder(view)
}