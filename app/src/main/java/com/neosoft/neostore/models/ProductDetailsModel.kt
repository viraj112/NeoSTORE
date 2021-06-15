package com.neosoft.neostore.models

data class ProductDetailsModel(
    val `data`: ProductDetailsList,
    val `product_images`: List<ProductImages>,
    val status: Int
)