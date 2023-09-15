package com.bmprj.cointicker.ui.coin

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val adapter by lazy { CoinListAdapter() }
    private val adapter1 by lazy { SearchListAdapter() }
    private val viewModel by viewModels<CoinListViewModel>()

    private val navigationHeaderView: View by lazy { binding.navigationView.getHeaderView(0) }
    private val photo: ShapeableImageView by lazy { navigationHeaderView.findViewById(R.id.shapeableImageView) }
    private val name: TextView by lazy { navigationHeaderView.findViewById(R.id.drawer_baslik_title) }
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
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
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
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener {
            viewModel.getDataFromDatabase(it.toString())
        }
    }

    @SuppressLint("InflateParams")
    private fun logOut() {

        val viewv = layoutInflater.inflate(R.layout.logout_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

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

    private fun initAdapter() {
        binding.coinListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.coinListRecyclerView.adapter = adapter

        adapter.setOnClickListener { onCoinItemClicked(it) }

        binding.searchRecy.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchRecy.adapter = adapter1

        adapter1.setOnClickListener { item ->
            onEntityItemClicked(item)
        }
    }


    private fun initLiveDataObservers() {


        viewModel.coins.handleState(
            onLoading = {
                binding.progress.visibility = View.VISIBLE
            },
            onSucces = {
                binding.progress.visibility = View.GONE
                adapter.updateList(it.asList())
                viewModel.insertCoins(ArrayList<CoinMarketItem>().apply {
                    addAll(it.asList())
                })
            },
            onError = {
                binding.progress.visibility = View.GONE
                Toast.makeText(requireContext(), getString(R.string.failmsg9), Toast.LENGTH_SHORT)
                    .show()
            }
        )

        viewModel.logOut.handleState(
            onSucces = {
                val action = CoinListFragmentDirections.actionCoinListFragmentToLoginFragment()
                findNavController.navigate(action)
            },
            onError = {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
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
                Log.e("userInfoError", it.message.toString())
            }
        )

        lifecycleScope.launch {
            viewModel.filteredCoins.collect { entity ->
                adapter1.updateList(entity)
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