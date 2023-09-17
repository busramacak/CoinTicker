package com.bmprj.cointicker.ui.coin

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.domain.coin.asList
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.utils.setUpDialog
import com.bmprj.cointicker.utils.toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val coinListAdapter by lazy { CoinListAdapter() }
    private val searchListAdapter by lazy { SearchListAdapter() }
    private val viewModel by viewModels<CoinListViewModel>()

    private val navigationHeaderView: View by lazy { binding.navigationView.getHeaderView(0) }
    private val photo: ShapeableImageView by lazy { navigationHeaderView.findViewById(R.id.drawerImageView) }
    private val name: TextView by lazy { navigationHeaderView.findViewById(R.id.drawerTitle) }
    private val findNavController by lazy { findNavController() }

    override fun initView(view: View) {
        initDrawer()
        initSearchView()
        initAdapter()
        initLiveDataObservers()
        viewModel.getUserInfo()
        viewModel.getData()
        initBackPress()
    }


    private fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.logOut -> {
                    logOut()
                }

                R.id.settings -> {
                    val action = CoinListFragmentDirections.actionCoinListFragmentToSettingsFragment()
                    findNavController.navigate(action)
                }
            }
            binding.drawer.closeDrawer(GravityCompat.START)

            true
        }
    }

    private fun initBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                } else {
                    val homeIntent = Intent(Intent.ACTION_MAIN)
                    homeIntent.addCategory(Intent.CATEGORY_HOME)
                    startActivity(homeIntent)
                }
            }
        })
    }

    private fun initSearchView() {
        with(binding){
            searchView.setupWithSearchBar(binding.searchBar)
            searchView.editText.addTextChangedListener {
                viewModel.getDataFromDatabase(it.toString())
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun logOut() {

        val viewv = layoutInflater.inflate(R.layout.logout_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val logOutButton = viewv.findViewById<MaterialButton>(R.id.pozitiveButton)
            val cancelButton = viewv.findViewById<MaterialButton>(R.id.negativeButton)

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

    private fun initAdapter() {
        with(binding){
            coinListRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            coinListRecyclerView.adapter = coinListAdapter
            searchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            searchRecyclerView.adapter = searchListAdapter
        }
        coinListAdapter.setOnClickListener { onCoinItemClicked(it) }
        searchListAdapter.setOnClickListener { onEntityItemClicked(it) }
    }


    private fun initLiveDataObservers() {
        viewModel.coins.handleState(
            onLoading = {
                binding.progresBar.visibility = View.VISIBLE
            },
            onSucces = {
                binding.progresBar.visibility = View.GONE
                coinListAdapter.updateList(it.asList())
                viewModel.insertCoins(ArrayList<CoinMarketItem>().apply {
                    addAll(it.asList())
                })
            },
            onError = {
                binding.progresBar.visibility = View.GONE
                toast(it.message)
            }
        )

        viewModel.logOut.handleState(
            onSucces = {
                toast(R.string.logOutSuccess)
                val action = CoinListFragmentDirections.actionCoinListFragmentToLoginFragment()
                findNavController.navigate(action)
            },
            onError = {
                toast(it.message)

            }
        )

        viewModel.userInfo.handleState(
            onLoading = {
                photo.setImageResource(R.drawable.progres)
            },
            onSucces = {
                photo.loadFromUrl(it.toString())
                name.text = viewModel.firebaseUser?.displayName
            },
            onError = {
                photo.setImageResource(R.drawable.error)
                name.text = ""
                toast(it.message)
            }
        )

        lifecycleScope.launch {
            viewModel.filteredCoins.collect { entity ->
                searchListAdapter.updateList(entity)
            }
        }
    }

    private fun onCoinItemClicked(item: CoinMarketItem) {
        val transition = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id, Constants.COLLECTION_COINS)
        findNavController.navigate(transition)
    }

    private fun onEntityItemClicked(item: Entity) {
        val transition = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id, Constants.COLLECTION_COINS)
        findNavController.navigate(transition)
    }
}