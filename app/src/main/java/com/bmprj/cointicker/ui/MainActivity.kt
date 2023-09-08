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

    private val viewModel by viewModels<MainViewModel>()
    private val adapter1 by lazy { SearchListAdapter() }

    private lateinit var navController:NavController

    private val navigationHeaderView : View by lazy { binding.navigationView.getHeaderView(0) }
    private val photo :ShapeableImageView by lazy { navigationHeaderView.findViewById(R.id.shapeableImageView) }
    private val name  : TextView by lazy { navigationHeaderView.findViewById(R.id.drawer_baslik_title) }

    override fun initView() {
        initNavigation()
        initDrawer()
        initSearchView()
        initAdapter()
        initLiveDataObservers()
        viewModel.getUserInfo()

    }

    override fun onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun logOut(){

        val viewv = layoutInflater.inflate(R.layout.logout_dialog,null)
        val dialog = AlertDialog.Builder(this)
            .setView(viewv)
            .setCancelable(false)
            .create()

        dialog.setOnShowListener {
            val logOutButton = viewv.findViewById<MaterialButton>(R.id.poz)
            val cancelButton = viewv.findViewById<MaterialButton>(R.id.neg)

            logOutButton.setOnClickListener {
                viewModel.logOut()

                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
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

                if(nd.id==R.id.coinListFragment){
                    binding.toolbar.title=getString(R.string.coinScreenTitle)
                    binding.searchBar.visibility=View.VISIBLE
                    binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else if(nd.id == R.id.favCoinsFragment){
                    binding.toolbar.title=getString(R.string.favScreenTitle)
                    binding.searchBar.visibility=View.GONE
                    binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                showBottomNav()
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { item->

            when(item.itemId){
                R.id.logOut->{ logOut() }
                R.id.settings->{
                    navController.navigate(R.id.action_coinListFragment_to_settingsFragment)
                }
            }

            binding.drawer.closeDrawer(GravityCompat.START)

            true
        }


    }

    private fun initDrawer(){
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initSearchView(){
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener {
            viewModel.getDataFromDatabase(it.toString())
        }
    }

    private fun initAdapter(){
        binding.searchRecy.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.searchRecy.adapter=adapter1

        adapter1.setOnClickListener { item ->
            onEntityItemClicked(item)
        }
    }

    private fun initLiveDataObservers(){

        lifecycleScope.launch {
            viewModel.logOut.handleState(
                onSucces = {
                    navController.navigate(R.id.action_coinListFragment_to_loginFragment)
                },
                onError = {
                    Toast.makeText(this@MainActivity,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
            )
        }

        lifecycleScope.launch {
            viewModel.filteredCoins.collect{entity ->
                adapter1.updateList(entity)
            }
        }

        lifecycleScope.launch {
            viewModel.userInfo.handleState(
                onLoading = {
                    photo.setImageResource(R.drawable.progres)
                },
                onSucces = {
                    photo.loadFromUrl(it.toString())
                    name.text=viewModel.currentUser?.displayName
                },
                onError = {
                    photo.setImageResource(R.drawable.error)
                    name.text=""
                    Log.e("userInfoError",it.message.toString())
                }
            )
        }
    }

    private fun hideBottomNav(){
        binding.bottomNav.visibility= View.GONE
        binding.toolbar.visibility=View.GONE
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    private fun showBottomNav(){
        binding.bottomNav.visibility=View.VISIBLE
        binding.toolbar.visibility=View.VISIBLE
    }
    private fun onEntityItemClicked(item: Entity) {
        hideKeyboard()

        navController.navigate(CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id))
        binding.searchView.visibility= View.GONE
        binding.searchBar.visibility= View.GONE
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}