package com.neosoft.neostore.models

import android.service.autofill.UserData

data class AccountData (
    val `user_data`: AccountUserDetails,
    val `product_categories`: List<AccountProductCatagories>,
    val  total_carts :String,
    val total_orders :String
        )