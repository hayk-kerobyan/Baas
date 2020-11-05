package com.virtussoft.demo.ui.company_add

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.CompanyRepo
import kotlinx.coroutines.launch

class AddCompanyVm @ViewModelInject constructor(
    private val companyRepo: CompanyRepo
) : ViewModel() {

    internal fun createCompany(company: Company) =
        viewModelScope.launch { companyRepo.createCompany(company) }

}