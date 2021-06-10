package com.neosoft.neostore.api

import com.neosoft.neostore.constants.ApiEndPoints
import com.neosoft.neostore.models.*
import retrofit2.Call
import retrofit2.http.*

interface Api {


    @FormUrlEncoded
    @POST(ApiEndPoints.REGISTER_KEY)
    fun register(@Field("first_name")first_name:String,@Field("last_name")last_name:String,@Field("email")email:String,@Field("password")password:String,@Field("confirm_password")confirm_password:String,@Field("gender")gender:String,@Field("phone_no")phone_no:String):Call<RegisterationModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.LOGIN_KEY)
    fun doLogin(@Field("email")email:String,@Field("password")password:String):Call<LoginModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.FORGOT_PASSWORD_KEY)
    fun forgotPassword(@Field("email")email:String):Call<ForgotPModel>


    @GET(ApiEndPoints.PRODUCT_LIST)
    fun getProductList(@Query("product_category_id")product_category_id:Int,@Query("limit")limit:Int,@Query("page")page:Int):Call<ProductList>

    @GET(ApiEndPoints.PRODUCT_DETAILS)
    fun getProductDetails(@Query("product_id")product_id:Int):Call<ProductDetailsModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.PRODUCT_RATING)
    fun productRating(@Field("product_id")product_id:String,@Field("rating")rating:Int):Call<ProductRatingModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.ADD_TO_CART)
    fun addToCart(@Field("product_id") product_id:String, @Field("quantity") quantity: Int, @Header("access_token") access_token:String):Call<AddToCartModel>


    @GET(ApiEndPoints.CART)
    fun getCartList(@Header("access_token")access_token:String):Call<MyCartListModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.DELETE_CART)
    fun deleteCart(@Header("access_token")access_token:String,@Field("product_id")product_id:String):Call<DeleteCartModel>

}

