package com.virtussoft.demo.ui.user_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtussoft.demo.model.employee.Employee
import com.virtussoft.demo.model.employee.dto.EmployeeRepo
import com.virtussoft.demo.model.user.dto.UserRepo
import kotlinx.coroutines.launch

class UsersListVm @ViewModelInject constructor(
    userRepo: UserRepo
) : ViewModel() {
    val users = userRepo.getUsers()
}