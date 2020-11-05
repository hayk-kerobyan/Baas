package com.virtussoft.demo.ui.company_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.virtussoft.demo.R
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.ui.BaseFrag
import com.virtussoft.demo.ui.employee_add.AddEmployeeFrag.Companion.REQUEST_ADD_EMPLOYEE
import com.virtussoft.demo.ui.user_list.UsersListFrag
import com.virtussoft.demo.ui.user_list.UsersListFrag.Companion.KEY_SELECTED_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_details_company.*
import kotlinx.android.synthetic.main.frag_list_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompanyDetailsFrag : BaseFrag() {

    override fun resource() = R.layout.frag_details_company

    private val args: CompanyDetailsFragArgs by navArgs()
    private val viewModel: CompanyDetailsVm by viewModels()
    private lateinit var company: Company

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EmployeesAdapter()
        employeesRV.adapter = adapter
        viewModel.getCompanyById(args.databaseId).observe(viewLifecycleOwner) {
            company = it
            nameTV.text = it.name
            lifecycleScope.launch {
                viewModel.getEmployees(it.id).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
        addFAB.setOnClickListener {
            setFragmentResultListener(REQUEST_ADD_EMPLOYEE) { resultKey, _ ->
                adapter.refresh()
            }
            val direction = CompanyDetailsFragDirections
                .actionCompanyDetailsFragToAddEmployeeFrag(company.databaseId)
            findNavController().navigate(direction)
        }
    }
}