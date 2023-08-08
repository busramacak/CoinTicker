package com.bmprj.cointicker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding
    private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main=this


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener{_,nd:NavDestination,_->

            if(nd.id== R.id.registerFragment || nd.id== R.id.loginFragment){
                hideBottomNav()
            }else{
                showBottomNav()
            }
        }



//        observeLiveData()
    }

    private fun hideBottomNav(){
        binding.bottomNav.visibility= View.GONE

    }
    private fun showBottomNav(){
        binding.bottomNav.visibility=View.VISIBLE

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun observeLiveData(){

    }
}