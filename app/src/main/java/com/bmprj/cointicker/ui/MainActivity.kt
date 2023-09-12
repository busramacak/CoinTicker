package com.bmprj.cointicker.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseActivity
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.ActivityMainBinding
import com.bmprj.cointicker.ui.coin.CoinListFragmentDirections
import com.bmprj.cointicker.ui.coin.SearchListAdapter
import com.bmprj.cointicker.utils.loadFromUrl
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)

        navController=navHostFragment.navController

        navHostFragment.navController.addOnDestinationChangedListener{_,nd:NavDestination,_->

            if(nd.id== R.id.registerFragment || nd.id== R.id.loginFragment
                || nd.id == R.id.settingsFragment || nd.id == R.id.coinDetailFragment){
                hideBottomNav()
            }else{
                showBottomNav()
            }
        }
    }

    private fun hideBottomNav(){
        binding.bottomNav.visibility= View.GONE
    }
    private fun showBottomNav(){
        binding.bottomNav.visibility=View.VISIBLE
    }
}