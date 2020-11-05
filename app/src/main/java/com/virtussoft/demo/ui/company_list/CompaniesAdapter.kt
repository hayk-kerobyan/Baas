package com.virtussoft.demo.ui.company_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.virtussoft.demo.R
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.utils.ClickListener
import kotlinx.android.synthetic.main.item_company.view.*


val differCallback = object : DiffUtil.ItemCallback<Company>() {
    override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
        return oldItem.name == newItem.name
    }

}

class CompaniesAdapter(
    private val listener:ClickListener? = null
) : PagingDataAdapter<Company, CompaniesAdapter.CompanyVH>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyVH {
        return CompanyVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CompanyVH, position: Int) {
        getItem(position)?.let {
            holder.itemView.name.text = buildString {
                append(it.id);
                append("  :  ");
                append(it.name)
            }
        }
    }

    inner class CompanyVH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            listener?.let { listener ->
                itemView.setOnClickListener {
                    val pos = absoluteAdapterPosition
                    view.tag = getItem(pos)
                    listener.onClick(view, pos)
                }
            }
        }
    }
}