package com.neosoft.neostore.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.alert_buy_now.*
import kotlinx.android.synthetic.main.alert_buy_now.view.*
import kotlinx.android.synthetic.main.rating_alert_dialog.view.*
import org.jetbrains.anko.toast
import org.xml.sax.Parser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NumberFormatException
import kotlin.math.roundToInt

class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {

    //variable initialize
    val my_retrofit = RetrofitClientProduct.getRetrofitInstance().create(Api::class.java)
    val retrofit = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    lateinit var title: String
    lateinit var rating: String
    lateinit var description: String
    lateinit var producer: String
    lateinit var cost: String
    lateinit var catagory: String
    lateinit var my_product_id :String
    lateinit var sharedPreferences: SharedPreferences
    var rate :Float = 0.0f
    lateinit var token :String
     var quantity:Int = 1

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //dassign variables
        initialization()

        getProductDetails()
    }

    //get product details api call
    private fun getProductDetails()
    {
        var product_id = intent.getIntExtra("p_id", -1)
        my_retrofit.getProductDetails(product_id.toString()).enqueue(object : Callback<ProductDetailsModel> {
            override fun onResponse(call: Call<ProductDetailsModel>, response: Response<ProductDetailsModel>)
            {
                try
                {
                    if (response.code() == Constants.SUCESS_CODE)
                    {
                            val items = response.body()?.data
                            //val images:List<ProductImages> = response.body()?.product_images!!
                            title = items?.name.toString()
                            rating = items?.rating?.toFloat().toString()
                            description = items?.description.toString()
                            producer = items?.producer.toString()
                            cost = "RS ." + " " + items?.cost.toString()
                            catagory = getString(R.string.catagory) + items?.cost
                            my_product_id = items?.id.toString()
                            supportActionBar?.title = title
                            getDetails()
                    } else if (response.code() == Constants.NOT_FOUND)
                    {

                        toast(response.message().toString())
                    }

                } catch (e: NumberFormatException)
                {
                    e.printStackTrace()
                } }

            override fun onFailure(call: Call<ProductDetailsModel>, t: Throwable)
            {
                toast(t.message.toString())
            }
        })
    }

    private fun getDetails()
    {
        txt_product_details_title.text = title
        product_details_ratingbar.rating = rating?.toFloat()!!
        txt_prouct_details_desc.text = description
        txt_product_details_producer.text = producer
        txt_product_details_price.text = cost
        txt_sub_title_tables_product_details_catagory.text = cost
    }

    private fun initialization()
    {
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token",null).toString()
        btn_product_details_rate.setOnClickListener(this)
        btn_product_details_buy.setOnClickListener(this)
        product_details_iv_share.setOnClickListener(this)
    }

    override fun onClick(view: View)
    {
        when (view.id) {
            R.id.btn_product_details_rate ->
            {
                //rate product here
                getRating()

            }
            R.id.btn_product_details_buy ->
            {
                //buy product
                buyNowProduct()
            }
            R.id.product_details_iv_share ->
            {
                val b= BitmapFactory.decodeResource(resources,R.drawable.ic_image_slider_three)
                val intent:Intent = Intent()
                intent.action= Intent.ACTION_SEND
                val path = MediaStore.Images.Media.insertImage(contentResolver,b,txt_product_details_title.toString()+cost,null)
                val uri = Uri.parse(path)
                intent.putExtra(Intent.EXTRA_STREAM,uri)
                intent.putExtra(Intent.EXTRA_TEXT,txt_product_details_title.text.toString()+" "+cost)
                intent.type="image/*"
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(intent,"Share To :"))
            }
            else ->{
                //donothing
            }
        }
    }

    //buy product popup
    private fun buyNowProduct()
    {
        val myview = View.inflate(this, R.layout.alert_buy_now, null)
        val mybuilder = AlertDialog.Builder(this)
        mybuilder.setView(myview)
        val mydialog = mybuilder.create()

        mydialog.setCanceledOnTouchOutside(false)
        mydialog.show()
        myview.btn_buynow_submit.setOnClickListener{
            val q:Int =myview.edt_buynow_quantity.text.toString().toInt()
                quantity = q
                //add product  into cart
                addToCart()
                mydialog.dismiss()
        }

    }

    //add to cart api call
    private fun addToCart()
    {

        retrofit.addToCart(my_product_id,quantity,token).enqueue(object :Callback<AddToCartModel>{
            override fun onResponse(call: Call<AddToCartModel>, response: Response<AddToCartModel>)
            {
                try
                {
                  if (response.code() == Constants.SUCESS_CODE)
                  {
                    toast(response.body()?.user_msg.toString())
                  }else if (response.code() ==Constants.NOT_FOUND)
                  {
                    toast(response.message().toString())
                  }else
                  {
                      toast(response.message().toString())
                  }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<AddToCartModel>, t: Throwable)
            {
                toast(t.message.toString())
            }
        })
    }

    //rating dialog popup
    private fun getRating()
    {
        val view = View.inflate(this, R.layout.rating_alert_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        view.txt_rating_title.text = title
        view.rating_ratingbar.rating = rating.toFloat()
        view.rating_ratingbar.stepSize = 1.toFloat()
        rate=view.rating_ratingbar.rating
        dialog.show()
        view.btn_rating_ratenow.setOnClickListener{
                rateProduct()
            dialog.dismiss()
        }
    }

    //rating product aoi call
    private fun rateProduct()
    {
        my_retrofit.productRating(my_product_id, rate.roundToInt()).enqueue(object :Callback<ProductRatingModel>{
            override fun onResponse(call: Call<ProductRatingModel>, response: Response<ProductRatingModel>)
            {
                try
                {
                    if (response.code()==Constants.SUCESS_CODE)
                    {
                        val myitems = response.body()?.data
                        product_details_ratingbar.rating = myitems?.rating?.toFloat()!!
                        toast(response.body()?.user_msg.toString())
                    }
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ProductRatingModel>, t: Throwable)
            {
                toast(t.message.toString())
            }

        })
    }

    override fun onBackPressed()
    {
        finish()
    }
}