package com.neosoft.neostore.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.*
import com.neosoft.neostore.utilities.LoadingDialog
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.alert_buy_now.view.*
import kotlinx.android.synthetic.main.rating_alert_dialog.view.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NumberFormatException
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {
    //variable initialize
    private val myRetrofit: Api = RetrofitClientProduct.getRetrofitInstance().create(Api::class.java)
    val retrofit: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var title: String
    lateinit var rating: String
    lateinit var description: String
    lateinit var producer: String
    lateinit var cost: String
    lateinit var catGory: String
    lateinit var myProductId: String
    lateinit var sharedPreferences: SharedPreferences
    private var rate: Float = 0.0f
    private lateinit var token: String
    private var quantity: Int = Constants.PRODUCT_ID
    lateinit var loadingDialog: LoadingDialog

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //assign variables
        loadingDialog = LoadingDialog(this)
        initialization()
        getProductDetails()
    }
    //get product details api call
    private fun getProductDetails() {
        val productId = intent.getIntExtra("p_id", -1)
        myRetrofit.getProductDetails(productId.toString())
            .enqueue(object : Callback<ProductDetailsModel> {
                override fun onResponse(call: Call<ProductDetailsModel>, response: Response<ProductDetailsModel>) {
                    try {
                        if (response.code() == Constants.SUCESS_CODE) {
                                val items = response.body()?.data
                                title = items?.name.toString()
                                rating = items?.rating?.toFloat().toString()
                                description = items?.description.toString()
                                producer = items?.producer.toString()
                                cost = "RS ." + " " + items?.cost.toString()
                                catGory = getString(R.string.catagory) + items?.cost
                                myProductId = items?.id.toString()
                                supportActionBar?.title = title
                                getDetails()
                        } else if (response.code() == Constants.NOT_FOUND) {
                            toast(response.message().toString())
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<ProductDetailsModel>, t: Throwable) {
                    toast(getString(R.string.no_connection))
                }
            })
    }
    //get details
    private fun getDetails() {
        txt_product_details_title.text = title
        product_details_ratingbar.rating = rating.toFloat()
        txt_prouct_details_desc.text = description
        txt_product_details_producer.text = producer
        txt_product_details_price.text = cost
        txt_sub_title_tables_product_details_catagory.text = cost
    }
    private fun initialization() {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null).toString()
        btn_product_details_rate.setOnClickListener(this)
        btn_product_details_buy.setOnClickListener(this)
        product_details_iv_share.setOnClickListener(this)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_product_details_rate ->
                //rate product here
                getRating()
            R.id.btn_product_details_buy ->
                //buy product
                buyNowProduct()
            R.id.product_details_iv_share -> {
                val b = BitmapFactory.decodeResource(resources, R.drawable.ic_image_slider_three)
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                val path = MediaStore.Images.Media.insertImage(contentResolver, b, txt_product_details_title.toString() + cost, null)
                val uri = Uri.parse(path)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.putExtra(Intent.EXTRA_TEXT, txt_product_details_title.text.toString() + " " + cost)
                intent.type = "image/*"
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivity(Intent.createChooser(intent, "Share To :"))
            }
            else ->{}
        }
    }
    //buy product popup
    private fun buyNowProduct() {
        val myView = View.inflate(this, R.layout.alert_buy_now, null)
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.setView(myView)
        val myDialog = myBuilder.create()
        myDialog.setCanceledOnTouchOutside(false)
        myDialog.show()
        myView.btn_buynow_submit.setOnClickListener {
            if (myView.edt_buynow_quantity.text?.isEmpty() == true) {
                myView.edt_buynow_quantity.error = getString(R.string.canot_be_empty)
            } else {
                val q: Int = myView.edt_buynow_quantity.text.toString().toInt()
                quantity = q
                //add product  into cart
                loadingDialog.startLoading()
                addToCart()
                myDialog.dismiss()
            }
        }
    }
    //add to cart api call
    private fun addToCart() {
        retrofit.addToCart(myProductId, quantity, token).enqueue(object : Callback<AddToCartModel> {
            override fun onResponse(call: Call<AddToCartModel>, response: Response<AddToCartModel>)
            {
                try {
                    when {
                        response.code() == Constants.SUCESS_CODE -> {
                            val handler = Handler()
                            handler.postDelayed({
                                loadingDialog.isDismiss()
                                toast(response.body()?.user_msg.toString())
                                val i = Intent(this@ProductDetailsActivity,MainActivity::class.java)
                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putString("count",response.body()?.total_carts.toString())
                                editor.apply()
                                startActivity(i)
                            },Constants.DELAY_TIME.toLong())
                        }
                        response.code() == Constants.NOT_FOUND -> {
                           loadingDialog.isDismiss()
                            toast(response.message().toString())
                        }
                        else -> {
                            loadingDialog.isDismiss()
                            toast(response.message().toString())
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<AddToCartModel>, t: Throwable) {
               loadingDialog.isDismiss()
                toast(getString(R.string.no_connection))
            }
        })
    }
    //rating dialog popup
    private fun getRating() {
        val view = View.inflate(this, R.layout.rating_alert_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        view.txt_rating_title.text = title
        view.rating_ratingbar.rating = rating.toFloat()
        view.rating_ratingbar.stepSize = 1.toFloat()
        rate = view.rating_ratingbar.rating
        dialog.show()
        view.btn_rating_ratenow.setOnClickListener {
            rateProduct()
            dialog.dismiss()
        }
    }
    //rating product aoi call
    private fun rateProduct() {
        myRetrofit.productRating(myProductId, rate.roundToInt())
            .enqueue(object : Callback<ProductRatingModel> {
                override fun onResponse(call: Call<ProductRatingModel>, response: Response<ProductRatingModel>)
                {
                    try {
                        if (response.code() == Constants.SUCESS_CODE) {
                            val myItems = response.body()?.data
                            product_details_ratingbar.rating = myItems?.rating?.toFloat()!!
                            toast(response.body()?.user_msg.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<ProductRatingModel>, t: Throwable) {
                    toast(getString(R.string.no_connection))
                }
            })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home ->
            {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}