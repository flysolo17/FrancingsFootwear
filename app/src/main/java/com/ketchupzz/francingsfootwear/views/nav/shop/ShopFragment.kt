package com.ketchupzz.francingsfootwear.views.nav.shop

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselLayoutManager
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentShopBinding
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.viewmodel.ProductViewModel
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.ImageAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.ProductAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.ProductAdapterClickListener


class ShopFragment : Fragment() ,ProductAdapterClickListener {

    private lateinit var binding : FragmentShopBinding
    private lateinit var loadingDialog: LoadingDialog
    private val productViewModel by activityViewModels<ProductViewModel>()
    private lateinit var productsAdapter : ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        binding.recycler.apply {
            adapter = ImageAdapter(view.context, listOf(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3))
        }


    }
    private fun observers() {
        productViewModel.products.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting All Products")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    productsAdapter = ProductAdapter(binding.root.context,it.data,this)
                    binding.recyclerviewProducts.apply {
                        layoutManager = GridLayoutManager(binding.root.context,2)
                        adapter = productsAdapter
                    }
                    implementSearch(productsAdapter)
                }
            }
        }
    }

    private fun implementSearch(adapter: ProductAdapter) {
        val handler = Handler(Looper.getMainLooper())
        var searchHandler: Runnable? = null
        binding.inputProductSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // If the inputProductSearch has focus, hide the featured products
                binding.layoutFeaturedProducts.visibility = View.GONE
            } else {
                // If the inputProductSearch loses focus, show the featured products
                binding.layoutFeaturedProducts.visibility = View.VISIBLE
            }
        }
        binding.inputProductSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                searchHandler?.let { handler.removeCallbacks(it) }
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                val query = editable.toString().trim()

                searchHandler?.let { handler.removeCallbacks(it) }

                searchHandler = Runnable {
                    adapter.filter(query)

                }


                handler.postDelayed(searchHandler!!, 500)
            }
        })
    }



    override fun onClick(product: Product, variations: List<Variation>) {
        val directions = ShopFragmentDirections.actionNavigationShopToViewProductFragment(product,variations.toTypedArray())
        findNavController().navigate(directions)
    }
}