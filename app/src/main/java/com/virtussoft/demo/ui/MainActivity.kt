package com.virtussoft.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.virtussoft.demo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView.setOnNavigationItemSelectedListener(listener)
    }

    private val listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.user -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_usersListFrag)
                true
            }
            R.id.company -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_companiesListFrag)
                true
            }
            else -> false
        }
    }


}