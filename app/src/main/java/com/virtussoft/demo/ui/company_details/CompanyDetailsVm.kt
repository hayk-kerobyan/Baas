package com.virtussoft.demo.ui.company_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.CompanyRepo
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.EmployeeRepo
import kotlinx.coroutines.launch

class CompanyDetailsVm @ViewModelInject constructor(
    private val companyRepo: CompanyRepo,
    private val employeeRepo: EmployeeRepo
) : ViewModel() {

    private lateinit var company: LiveData<Company>

    fun getCompanyById(databaseId:Long): LiveData<Company>{
        if(!::company.isInitialized){
            company = companyRepo.getCompanyByDbId(databaseId)
        }
        return company
    }

    fun getEmployees(companyId:String) = employeeRepo.getEmployees(companyId)
}