package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.repository.products.ProductRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel  @Inject constructor(val productRepository: ProductRepository) : ViewModel(){
    private val _products = MutableLiveData<UiState<List<Product>>>()
    val products : LiveData<UiState<List<Product>>> get() = _products


    private val _variations = MutableLiveData<UiState<List<Variation>>>()
    val variations : LiveData<UiState<List<Variation>>> get() = _variations

    fun getAllProducts() {
        productRepository.getAllProducts {
            _products.value = it
        }
    }
    fun getVariationByProductID(productID : String) {
        productRepository.getVariationByProductID(productID) {
            _variations.value = it
        }
    }
}