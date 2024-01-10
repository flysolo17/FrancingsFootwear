package com.ketchupzz.francingsfootwear.views.nav.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.databinding.FragmentViewProductBinding
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.utils.LoadingDialog
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.getEffectiveProductPrice
import com.ketchupzz.francingsfootwear.viewmodel.ProductViewModel
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.SizesAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.VariationAdapter
import com.ketchupzz.francingsfootwear.views.nav.shop.adapters.VariationClickListener

class ViewProductFragment : Fragment(),VariationClickListener {

    private lateinit var binding : FragmentViewProductBinding
    private lateinit var loadingDialog: LoadingDialog
    private val args by navArgs<ViewProductFragmentArgs>()
    private var variations : List<Variation> = listOf()
    private lateinit var variationAdapter: VariationAdapter
    private lateinit var sizesAdapter: SizesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        variations = args.variations.toList()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProductBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(binding.root.context)
        Glide.with(binding.root.context)
            .load(args.product.image)
            .error(R.drawable.product)
            .into(binding.imageProduct)
        binding.textName.text = args.product.name
        binding.textCategory.text = args.product.category
        binding.textDescription.text = args.product.description
        binding.textPrice.text = getEffectiveProductPrice(variations)
        variationAdapter = VariationAdapter(binding.root.context,variations,this)
        sizesAdapter = SizesAdapter(binding.root.context, listOf())
        binding.recyclerviewVariations.apply {
            layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)
            adapter = variationAdapter
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBuyNow.setOnClickListener {
            val selectedVar : Int = variationAdapter.getSelectedVariation()
            val selectedSize  = sizesAdapter.getSelectedSize()
            if (selectedVar == -1) {
                Toast.makeText(view.context,"Please Select Variation",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedSize == -1 ) {
                Toast.makeText(view.context,"Please Select Size",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val directions = ViewProductFragmentDirections.actionViewProductFragmentToSelectedProductBottomSheet(args.product,
                variations[selectedVar],
                variations[selectedVar].sizes[selectedSize],
                true)
            findNavController().navigate(directions)
        }

        binding.buttonAddToCart.setOnClickListener {
        val selectedVar : Int = variationAdapter.getSelectedVariation()
        val selectedSize  = sizesAdapter.getSelectedSize()
            if (selectedVar == -1) {
                Toast.makeText(view.context,"Please Select Variation",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedSize == -1 ) {
                Toast.makeText(view.context,"Please Select Size",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val directions = ViewProductFragmentDirections.actionViewProductFragmentToSelectedProductBottomSheet(args.product,
                variations[selectedVar],
                variations[selectedVar].sizes[selectedSize],
                false)
            findNavController().navigate(directions)
        }
    }

    override fun onVariationClicked(variation: Variation) {
        sizesAdapter = SizesAdapter(binding.root.context,variation.sizes)
        binding.recyclerviewSizes.apply {
            layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)
            adapter = sizesAdapter
        }
    }

}