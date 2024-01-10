package com.ketchupzz.francingsfootwear


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.ketchupzz.francingsfootwear.databinding.ActivityMainBinding


import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState

import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.viewmodel.CartViewModel
import com.ketchupzz.francingsfootwear.viewmodel.ProductViewModel
import com.ketchupzz.francingsfootwear.viewmodel.TransactionViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private val productViewModel by viewModels<ProductViewModel>()
    private val transactionViewModel by viewModels<TransactionViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private lateinit var binding : ActivityMainBinding
    private val loadingDialog = LoadingDialog(this)
    private lateinit var navController : NavController
    private lateinit var badge: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        FirebaseAuth.getInstance().currentUser?.let{
            authViewModel.getCustomerInfo(it.uid)
        }
        badge = BadgeDrawable.create(this)
        observers()
    }
    private fun setUpNav() {
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_container)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_shop,
                R.id.navigation_orders,
                R.id.navigation_account,

            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.navigation_shop -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.navigation_orders -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.navigation_account -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.menu_cart -> {
                    hideBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.checkoutFragment -> {
                    hideBottomNav()
                    invalidateOptionsMenu()
                }
                else -> {
                    hideBottomNav()
                    invalidateOptionsMenu()
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showBottomNav() {
        binding.bottomAppBar.performShow(true)
        binding.bottomAppBar.hideOnScroll = true
    }

    private fun hideBottomNav() {
        binding.bottomAppBar.performHide(true)
        binding.bottomAppBar.hideOnScroll = false
    }

    private fun observers() {
        authViewModel.customer.observe(this) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut().also {
                        finish()
                    }
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting User Profile")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    setUpNav()
                    cartViewModel.getAllMyCart(it.data.id?: "")

                    productViewModel.getAllProducts()
                    transactionViewModel.getAllMyTransactions(it.data.id ?:"")

                }
            }
        }

        cartViewModel.cart.observe(this) {
            when(it) {
                is UiState.FAILED -> {
                }
                is UiState.LOADING -> {
                }
                is UiState.SUCCESS -> {
                    badge.number = it.data.size
                    invalidateOptionsMenu()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId != R.id.menu_cart || currentDestinationId != R.id.checkoutFragment) {
            menuInflater.inflate(R.menu.action_menu, menu)
            val cart = menu.findItem(R.id.menu_cart)
            BadgeUtils.attachBadgeDrawable(badge, binding.toolbar, cart.itemId)
            return true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_cart -> {
                navController.navigate(R.id.menu_cart)
                true
            }
            R.id.menu_messages -> {
                navController.navigate(R.id.menu_messages)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}