package com.virtussoft.demo.ui.company_list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.virtussoft.demo.R
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.ui.BaseFrag
import com.virtussoft.demo.ui.user_list.UsersListFrag
import com.virtussoft.demo.utils.ClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_list_company.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompaniesListFrag : BaseFrag() {

    override fun resource() = R.layout.frag_list_company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CompaniesAdapter(listener)
        recyclerView.adapter = adapter
        val model: CompaniesListVm by viewModels()
        lifecycleScope.launch {
            model.companies.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.company_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_company -> {
                findNavController().navigate(R.id.action_companiesListFrag_to_addCompanyFrag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val listener = object : ClickListener {
        override fun onClick(view: View, pos: Int) {
            val company = view.tag as Company
            val direction = CompaniesListFragDirections
                .actionCompaniesListFragToCompanyDetailsFrag(company.databaseId)
            findNavController().navigate(direction)
        }
    }
}