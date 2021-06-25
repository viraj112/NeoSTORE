package com.neosoft.neostore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.*
import kotlinx.android.synthetic.main.items_my_cart.view.*
import kotlinx.android.synthetic.main.quantity_dialog.view.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MyCartAdapter(val context: Context, val data: List<Data>) :
    RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {
    //initialize variable
    private val retrofitClientCart: Api = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    private var listData: MutableList<Data> = data as MutableList
    private var productId: String = ""
    private lateinit var token: String
    lateinit var sharedPreferences: SharedPreferences
    private var cartQuantity: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_my_cart, parent, false)
        return MyCartViewHolder(view)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        sharedPreferences = context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString("token", null).toString()

        holder.itemView.txt_title_my_cart.text = data[position].product.name
        holder.itemView.txt_catagory_my_cart.text = "(" + data[position].product.product_category + ")"
        holder.itemView.txt_price_my_cart.text = "\u20B9" + data[position].product.cost.toString()
        Glide.with(context).load(data[position].product.product_images).into(holder.itemView.iv_my_cart)
        productId = data[position].product_id.toString()
        holder.itemView.txt_cart_quantity.text = data[position].quantity.toString()

        holder.itemView.txt_cart_quantity.setOnClickListener {
            val myView = View.inflate(context, R.layout.quantity_dialog, null)
            val myBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
            myBuilder.setView(myView)
            val myDialog = myBuilder.create()
            myDialog.setCanceledOnTouchOutside(false)
            myDialog.show()
            myView.btn_cart_quantity.setOnClickListener {
                if (myView.edt_cart_quantity.text.toString().isEmpty()) {
                    myView.edt_cart_quantity.error = context.getString(R.string.canot_be_empty)
                } else {
                    val quantity = myView.edt_cart_quantity.text.toString().toInt()
                    cartQuantity = quantity
                    holder.itemView.txt_cart_quantity.text = quantity.toString()
                    retrofitClientCart.editCart(token, productId, cartQuantity)
                        .enqueue(object : Callback<EditCartmodel> {
                            override fun onResponse(call: Call<EditCartmodel>, response: Response<EditCartmodel>) {
                                try {
                                    when {
                                        response.code() == Constants.SUCESS_CODE -> {
                                            context.toast(response.body()?.user_msg.toString())

                                        }
                                        response.code() == Constants.NOT_FOUND -> {
                                            context.toast(response.body()?.user_msg.toString())
                                        }
                                        else -> {
                                            context.toast(response.body()?.user_msg.toString())
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(call: Call<EditCartmodel>, t: Throwable) {
                                context.toast(R.string.no_connection)
                            }
                        })

                    myDialog.dismiss()

                }
            }

        }

        //click for delete item from list api call
        holder.itemView.iv_delete_my_cart.setOnClickListener {
            retrofitClientCart.deleteCart(token, data[position].product_id.toString())
                .enqueue(object : Callback<DeleteCartModel> {
                    override fun onResponse(call: Call<DeleteCartModel>, response: Response<DeleteCartModel>) {
                        try {
                            if (response.code() == Constants.SUCESS_CODE) {

                                deleteItem(position)
                                context.toast(response.body()?.user_msg.toString())

                            } else if (response.code() == Constants.NOT_FOUND) {
                                context.toast(response.body()?.user_msg.toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<DeleteCartModel>, t: Throwable) {
                        context.toast(t.message.toString())
                    }
                })
        }
    }

    //delete items from list
    private fun deleteItem(position: Int) {
        listData.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class MyCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}