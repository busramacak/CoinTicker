package com.bmprj.cointicker.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.ActivityMainBinding
import com.bmprj.cointicker.ui.coin.CoinListFragmentDirections
import com.bmprj.cointicker.ui.coin.SearchListAdapter
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.bmprj.cointicker.utils.loadFromUrl
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding
    private lateinit var navController:NavController
    private val viewModel by viewModels<MainViewModel>()
    private val adapter1 = SearchListAdapter(onItemClicked = {item -> onEntityItemClicked(item)},arrayListOf())

    private lateinit var photo :ShapeableImageView
    private lateinit var name  : TextView


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CoinTicker)
        super.onCreate(savedInstanceState)


        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main=this

        val view =  binding.navigationView.getHeaderView(0)
        photo = view.findViewById(R.id.shapeableImageView)
        name = view.findViewById(R.id.drawer_baslik_title)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)

        navController=navHostFragment.navController



        viewModel.getUserInfo()


        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()




        binding.navigationView.setNavigationItemSelectedListener { item->
            val id= item.itemId

            when(id){
                R.id.logOut->{
                    logOut()
                    println("logoutt")
                }
                R.id.settings->{
                    navController.navigate(R.id.action_coinListFragment_to_settingsFragment)
                    println("settinnnngs")
                }
            }

            binding.drawer.closeDrawer(GravityCompat.START)

            true
        }

        binding.searchRecy.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.searchRecy.adapter=adapter1

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                viewModel.getDataFromDatabase(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })


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

        observeLiveData()
    }


    private fun observeLiveData(){

        viewModel.logOut.observe(this){logout->
            when(logout){
                is UiState.Loading -> {

                }
                is UiState.Success -> {

                    navController.navigate(R.id.action_coinListFragment_to_loginFragment)

                }
                is UiState.Error -> {

                }
            }
        }
        viewModel.filteredCoins.observe(this){entity->
            entity?.let {
                adapter1.updateList(entity)
            }
        }

        viewModel.userInfo.observe(this){resource->
            when(resource){
                is Resource.Failure ->{
                    photo.setImageResource(R.drawable.error)
                    Log.e("userInfoError",resource.exception.message.toString())
                }
                is Resource.Success -> {
                    photo.loadFromUrl(resource.result.toString())
                    name.text=viewModel.currentUser?.displayName
                }
                Resource.loading -> {
                    photo.setImageResource(R.drawable.progres)

                }
            }

        }
    }


    fun logOut(){

        val viewv = layoutInflater.inflate(R.layout.logout_dialog,null)
        val dialog = AlertDialog.Builder(this)
            .setView(viewv)
            .setCancelable(false)
            .create()

        dialog.setOnShowListener {
            val btnp = viewv.findViewById<MaterialButton>(R.id.poz)
            val btnn = viewv.findViewById<MaterialButton>(R.id.neg)

            btnp.setOnClickListener {
                viewModel.logOut()

                dialog.dismiss()
            }
            btnn.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()

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

    private fun onEntityItemClicked(item: Entity) {
        navController.navigate(CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id))
        binding.searchView.visibility= View.GONE
        binding.searchBar.visibility= View.GONE
    }
}