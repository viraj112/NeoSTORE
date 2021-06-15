package com.neosoft.neostore.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.*
import kotlinx.android.synthetic.main.alert_dialog_custom.*
import kotlinx.android.synthetic.main.alert_dialog_custom.view.*
import kotlinx.android.synthetic.main.fragment_my_cart.view.*
import kotlinx.android.synthetic.main.items_my_cart.view.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MyCartAdapter(val context: Context, val data: List<Data>) :
    RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {
    val retrofitClientCart = RetrofitClientCart.getRetrofitInstance().create(Api::class.java)
    private var listdata: MutableList<Data> = data as MutableList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_my_cart, parent, false)
        return MyCartViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {

        holder.itemView.txt_title_my_cart.text = data.get(position).product.name
        holder.itemView.txt_catagory_my_cart.text = "(" + data.get(position).product.product_category + ")"
        holder.itemView.txt_price_my_cart.text = "\u20B9" + data.get(position).product.cost.toString()
        Glide.with(context).load(data.get(position).product.product_images).into(holder.itemView.iv_my_cart)
        holder.itemView.iv_delete_my_cart.setOnClickListener {

            retrofitClientCart.deleteCart(Constants.TOKEN, data.get(position).product_id.toString())
                .enqueue(
                    object : Callback<DeleteCartModel> {
                        override fun onResponse(
                            call: Call<DeleteCartModel>,
                            response: Response<DeleteCartModel>
                        ) {
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
                    }
                )
        }
    }

    private fun deleteItem(position: Int) {
        listdata.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class MyCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}