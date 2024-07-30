package com.example.myapplicationfirebase

import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
 import com.example.myapplicationfirebase.databinding.ActivityDashboardBinding


class DashboardActivity11 : BaseActivity() {
    private var doublebackpressedtoexit = false

    private lateinit var binding:ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.app_gradient_color_background))

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



    }

    override fun onBackPressed() {
        if (doublebackpressedtoexit)
        {
            super.onBackPressed()
            return
        }
        doublebackpressedtoexit = true
        ShowErrorSnackbar(getString(R.string.please_click_back_again_to_exit),true)
        Handler().postDelayed({doublebackpressedtoexit =false},2000)
     }
}