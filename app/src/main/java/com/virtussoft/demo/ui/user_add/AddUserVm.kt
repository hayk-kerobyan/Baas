package com.virtussoft.demo.ui.user_add

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.model.user.dto.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddUserVm @ViewModelInject constructor(
    val userRepo: UserRepo
) : ViewModel() {

    internal fun createUser(user: User, avatar: Uri?) =
        viewModelScope.launch(Dispatchers.IO) { userRepo.createUser(user, avatar) }

}