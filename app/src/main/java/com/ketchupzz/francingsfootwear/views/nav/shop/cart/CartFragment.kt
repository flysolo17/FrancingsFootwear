package com.ketchupzz.francingsfootwear.views.nav.shop.cart

import android.content.ClipData.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentCartBinding
import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Size
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.models.transaction.Items
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.computeItemSubtotal
import com.ketchupzz.francingsfootwear.utils.toPHP
import com.ketchupzz.francingsfootwear.viewmodel.CartViewModel
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.CartAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.CartClickListener


class CartFragment : Fragment(),CartClickListener {
    private lateinit var binding : FragmentCartBinding
    private lateinit var loadingDialog: LoadingDialog
    private val cartViewModel by activityViewModels<CartViewModel>()
    private val selectedCart : MutableList<Items> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        binding.buttonCheckout.setOnClickListener {
            if (selectedCart.isEmpty()) {
                Toast.makeText(view.context,"Please select something in your cart",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val directions = CartFragmentDirections.actionMenuCartToCheckoutFragment(selectedCart.toTypedArray())
            findNavController().navigate(directions)
        }
    }

    private fun observers() {
        cartViewModel.cart.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting cart..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    it.data.forEach { cart ->
                        selectedCart.find { items ->
                            cart.productID == items.productID &&
                                    cart.variationID == items.variationID &&
                                    cart.size == items.size
                        }?.quantity = cart.quantity
                    }
                    updateTotal(selectedCart)
                    binding.recyclerViewCart.apply {
                        layoutManager = LinearLayoutManager(binding.root.context)
                        adapter = CartAdapter(binding.root.context,it.data,this@CartFragment)
                        addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                    }
                }
            }
        }
    }
    private fun updateTotal(items : List<Items>) {
        binding.textTotal.text = computeItemSubtotal(items).toPHP()
    }

    override fun checkBoxIsClick(isChecked: Boolean,product: Product,variation: Variation, cart: Cart, size: Size) {
        val item  = Items( cart.productID, cart.variationID, name = product.name ?: "", image = variation.image, variation = variation.name, size.size, size.price, cart.quantity)
        if (isChecked) {
            selectedCart.add(item)
        } else {
            selectedCart.remove(item)
        }
        updateTotal(selectedCart)
    }

    override fun onResume() {
        super.onResume()
        selectedCart.clear()
        updateTotal(selectedCart)
    }

}