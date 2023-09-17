package com.bmprj.cointicker.ui

import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseActivity
import com.bmprj.cointicker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val binding:ActivityMainBinding by lazy { DataBindingUtil.setContentView(this,R.layout.activity_main) }
    private lateinit var navController:NavController

    override fun initView() {
        initNavigation()
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavigationBar,navHostFragment.navController)

        navController=navHostFragment.navController

        navHostFragment.navController.addOnDestinationChangedListener{_,nd:NavDestination,_->
            if(nd.id==R.id.splashFragment){
                window.setFlags(
                    FLAG_FULLSCREEN,
                    FLAG_FULLSCREEN
                )
            }else{
                window.clearFlags(FLAG_FULLSCREEN)
            }
            if(nd.id== R.id.coinListFragment || nd.id== R.id.favCoinsFragment){
                showBottomNav()
            }else{

                hideBottomNav()
            }
        }
    }

    private fun hideBottomNav(){
        binding.bottomNavigationBar.visibility= View.GONE
    }
    private fun showBottomNav(){
        binding.bottomNavigationBar.visibility=View.VISIBLE
    }
}