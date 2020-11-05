package com.virtussoft.demo.ui.user_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.virtussoft.demo.R
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.ui.BaseFrag
import com.virtussoft.demo.utils.ClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_list_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersListFrag : BaseFrag() {

    companion object {
        const val REQUEST_SELECT_USER = "REQUEST_SELECT_USER"
        const val KEY_SELECTED_USER = "KEY_SELECTED_USER"

        const val DEFAULT = 0
        const val SELECTABLE = 1
    }

    private val args: UsersListFragArgs by navArgs()
    private val viewModel: UsersListVm by viewModels()

    override fun resource() = R.layout.frag_list_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UsersAdapter(listener)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.users.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (args.mode == DEFAULT) inflater.inflate(R.menu.user_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (args.mode == DEFAULT) {
            when (item.itemId) {
                R.id.add_user -> {
                    findNavController().navigate(R.id.action_listFrag_to_addUserFrag)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private val listener = object : ClickListener {
        override fun onClick(view: View, pos: Int) {
            when (args.mode) {
                DEFAULT -> {

                }
                SELECTABLE -> onUserSelected(view.tag as User)
                else -> throw IllegalArgumentException("Unhandled mode")
            }
        }
    }

    private fun onUserSelected(user: User) {
        setFragmentResult(
            REQUEST_SELECT_USER,
            Bundle().apply {
                putLong(KEY_SELECTED_USER, user.databaseId)
            }
        )
    }
}