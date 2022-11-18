package com.neosoft.neostore.models

data class SofaListModel ( val `data`: ArrayList<SofaModel>,
                           val status: Int){
    data class SofaModel( val id: Int,
                          val product_category_id: Int,
                          val name: String,
                          val producer: String,
                          val description: String,
                          val cost: Int,
                          val rating: Int,
                          val view_count: Int,
                          val created: String,
                          val modified: String,
                          val product_images: String)

}