package com.virtussoft.demo.ui.user_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.virtussoft.demo.R
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.utils.ClickListener
import kotlinx.android.synthetic.main.item_user.view.*


val differCallback = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.firstName == newItem.firstName
                && oldItem.lastName == newItem.lastName
    }
}

class UsersAdapter(
    private val listener: ClickListener? = null
) : PagingDataAdapter<User, UsersAdapter.UserVH>(differCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        return UserVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
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

    inner class UserVH(view: View) : RecyclerView.ViewHolder(view) {
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