package com.virtussoft.demo.ui.employee_add

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtussoft.demo.model.company.Company
import com.virtussoft.demo.model.company.dto.CompanyRepo
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.EmployeeRepo
import com.virtussoft.demo.model.user.dto.UserRepo
import com.virtussoft.demo.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEmployeeVm @ViewModelInject constructor(
    userRepo: UserRepo,
    private val companyRepo: CompanyRepo,
    private val employeeRepo: EmployeeRepo
) : ViewModel() {
    val users = userRepo.getUsers()

    private lateinit var company: LiveData<Company>
    private val result = MutableLiveData<Result<Employee>>()

    fun newEmployeeResult() = result as LiveData<Result<Employee>>

    fun getCompanyById(databaseId:Long): LiveData<Company> {
        if(!::company.isInitialized){
            company = companyRepo.getCompanyByDbId(databaseId)
        }
        return company
    }


    fun createEmployee(employee: Employee) =
        viewModelScope.launch(Dispatchers.IO) {
            try{
                result.postValue(Result.Success(employeeRepo.createEmployee(employee)))
            }catch (e:Exception){
                result.postValue(Result.Error(e))
            }
        }
}