package com.ketchupzz.francingsfootwear.repository.products

import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.utils.UiState

interface ProductRepository {
    fun getAllProducts(result : (UiState<List<Product>>) -> Unit)
    fun getVariationByProductID(productID: String,result: (UiState<List<Variation>>) -> Unit)
}