package com.virtussoft.demo.ui.employee_add

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.functions.FirebaseFunctions
import com.virtussoft.demo.R
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.net.EmployeeNet
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.ui.BaseFrag
import com.virtussoft.demo.ui.user_list.UsersAdapter
import com.virtussoft.demo.utils.ClickListener
import com.virtussoft.demo.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_list_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEmployeeFrag : BaseFrag() {

    companion object {
        const val REQUEST_ADD_EMPLOYEE = "REQUEST_ADD_EMPLOYEE"
    }

    private val args: AddEmployeeFragArgs by navArgs()
    private val viewModel: AddEmployeeVm by viewModels()
    private lateinit var company: Company

    override fun resource() = R.layout.frag_list_user

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UsersAdapter(listener)
        recyclerView.adapter = adapter
        viewModel.getCompanyById(args.companyId).observe(viewLifecycleOwner) {
            company = it
        }
        viewModel.newEmployeeResult().observe(viewLifecycleOwner){
            when (it) {
                is Result.Success -> {
                    setFragmentResult(REQUEST_ADD_EMPLOYEE, null)
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.users.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private val listener = object : ClickListener {
        override fun onClick(view: View, pos: Int) {
            createEmployee(view.tag as User)
        }
    }

    private fun createEmployee(user: User) {
        val employee = Employee(
            userId = user.id,
            companyId = company.id,
            firstName = user.firstName,
            lastName = user.lastName,
            avatarUrl = user.avatarUrl,
            companyName = company.name
        )
        viewModel.createEmployee(employee)
    }
}