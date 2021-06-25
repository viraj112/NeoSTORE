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
    fun getProductDetails(@Query("product_id")product_id:String):Call<ProductDetailsModel>

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

    @FormUrlEncoded
    @POST(ApiEndPoints.ORDER)
    fun placeOrder(@Header("access_token")access_token:String,@Field("address")address:String):Call<PlaceOrderModel>

    @GET(ApiEndPoints.GET_USER_DATA)
    fun fetchAccountDetails(@Header("access_token")access_token:String):Call<FetchAccountModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.CHANGE_PASSWORD)
    fun changePassword(@Header("access_token")access_token:String,@Field("old_password")old_password:String,@Field("password")password:String,@Field("confirm_password")confirm_password:String):Call<ChangePasswordModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.EDIT_PROFILE)
    fun editProfile(@Header("access_token")access_token: String, @Field("email")email: String, @Field("dob")dob: String
                    , @Field("phone_no")phone_no: String, @Field("profile_pic")profile_pic:String,@Field("first_name")first_name: String,@Field("last_name")last_name: String):Call<EditProfileModel>

    @GET(ApiEndPoints.ORDER_LIST)
    fun getOrderList(@Header("access_token")access_token:String):Call<OrderListModel>

    @GET(ApiEndPoints.ORDER_DETAILS)
    fun getOrderDetails(@Header("access_token")access_token:String,@Query("order_id")order_id:Int):Call<OrderDetailsModel>

    @FormUrlEncoded
    @POST(ApiEndPoints.EDIT_CART)
    fun editCart(@Header("access_token")access_token:String,@Field("product_id")product_id:String,@Field("quantity")quantity:Int):Call<EditCartmodel>

}

