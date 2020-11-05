package com.virtussoft.demo.ui.company_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.virtussoft.demo.model.company.dto.CompanyRepo

class CompaniesListVm @ViewModelInject constructor(
    companyRepo: CompanyRepo
) : ViewModel() {
    val companies = companyRepo.getCompanies()
}