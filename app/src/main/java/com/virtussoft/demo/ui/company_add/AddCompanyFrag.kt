package com.virtussoft.demo.ui.company_add

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.virtussoft.demo.R
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.ui.BaseFrag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_add_company.*

@AndroidEntryPoint
class AddCompanyFrag : BaseFrag() {

    private val viewModel: AddCompanyVm by viewModels()

    override fun resource() = R.layout.frag_add_company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.company_add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirm -> {
                createCompany()
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createCompany() {
        val company = Company(
            id = "",
            databaseId = -1,
            name = nameET.text.toString()
        )
        viewModel.createCompany(company)
    }
}