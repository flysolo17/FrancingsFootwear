package com.ketchupzz.francingsfootwear.repository.products

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.utils.UiState
const val PRODUCT_COLLECTION = "products";
const val VARIATION_SUB_COLLECTION = "variations"
class ProductRepositoryImpl(private val firestore: FirebaseFirestore) : ProductRepository {
    override fun getAllProducts(result: (UiState<List<Product>>) -> Unit) {
        firestore.collection(PRODUCT_COLLECTION)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Product::class.java)))
                }
            }

    }

    override fun getVariationByProductID(
        productID: String,
        result: (UiState<List<Variation>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(PRODUCT_COLLECTION)
            .document(productID)
            .collection(VARIATION_SUB_COLLECTION)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Variation::class.java)))
                }
            }
    }
}