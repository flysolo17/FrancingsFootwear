package com.ketchupzz.francingsfootwear.views.nav.shop.widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentSelectedProductBottomSheetBinding
import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Size
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.models.transaction.Items
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.formatPrice
import com.ketchupzz.francingsfootwear.utils.generateRandomNumber
import com.ketchupzz.francingsfootwear.viewmodel.AuthViewModel
import com.ketchupzz.francingsfootwear.viewmodel.CartViewModel
import java.util.Date
import kotlin.properties.Delegates


class SelectedProductBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentSelectedProductBottomSheetBinding
    private lateinit var loadingDialog: LoadingDialog
    private val args by navArgs<SelectedProductBottomSheetArgs>()

    private lateinit var product: Product
    private lateinit var variation : Variation
    private lateinit var size: Size
    private var isCheckOut by Delegates.notNull<Boolean>()
    private var quantity = 1
    private var customer : Customer ? = null
    private val authViewModel by activityViewModels<AuthViewModel>()
    private val cartViewModel by activityViewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = args.product
        variation = args.variation
        size = args.size
        isCheckOut = args.isCheckOut
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectedProductBottomSheetBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        Glide.with(binding.root.context)
            .load(variation.image)
            .error(R.drawable.product)
            .into(binding.imageSelectedVariation)
        if (isCheckOut) {
            binding.buttonAddTocart.visibility = View.GONE
            binding.buttonBuyNow.visibility = View.VISIBLE
        } else {
            binding.buttonBuyNow.visibility = View.GONE
            binding.buttonAddTocart.visibility = View.VISIBLE
        }
        binding.textProductName.text = product.name
        binding.textProductVariation.text = variation.name
        binding.textSizeName.text = "Size : ${size.size}"
        updatePriceAndQuantity(quantity)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        binding.buttonAddTocart.setOnClickListener {
            if (customer === null) {
                Toast.makeText(view.context,"no user!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val cart = Cart(
                id = generateRandomNumber(14),
                customerID = customer?.id ?: "",
                productID = product.id ?: "",
                variationID = variation.id,
                size = size.size,
                quantity = quantity)
            addToCart(cart)
        }
        binding.buttonBuyNow.setOnClickListener {
            if (customer === null) {
                Toast.makeText(view.context,"no user!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val items = Items(productID = product.id ?: "",variationID = variation.id, name = product.name ?: "", image = variation.image,variation = variation.name,size = size.size,price = size.price,quantity = quantity)
            val directions = SelectedProductBottomSheetDirections.actionSelectedProductBottomSheetToCheckoutFragment(
                arrayOf(items)
            )
            findNavController().navigate(directions)
        }
        binding.buttonAdd.setOnClickListener {
            if (size.stock > quantity) {
                quantity += 1
                updatePriceAndQuantity(quantity)
                return@setOnClickListener
            }
            Toast.makeText(view.context,"You reach the max quantity",Toast.LENGTH_SHORT).show()
        }
        binding.buttonMinus.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                updatePriceAndQuantity(quantity)
                return@setOnClickListener
            }
            Toast.makeText(view.context,"You reach the minimum quantity",Toast.LENGTH_SHORT).show()
        }
    }
    private fun updatePriceAndQuantity(quantity : Int) {
        binding.textQuantity.text = quantity.toString()
        binding.textPrice.text = formatPrice(size.price * quantity)
    }

    private fun observers() {
        authViewModel.customer.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                    dismiss()
                }

                is UiState.LOADING -> {
                    loadingDialog.showDialog("Getting user...")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    customer = it.data
                }
            }
        }
    }
    private fun addToCart(cart: Cart) {
        cartViewModel.cartRepository.addToCart(cart) {
            when(it) {
                is UiState.FAILED -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> {
                    loadingDialog.showDialog("Adding to your cart..")
                }
                is UiState.SUCCESS -> {
                    loadingDialog.closeDialog()
                    Toast.makeText(binding.root.context,it.data,Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
    }
}