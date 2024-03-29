package com.neosoft.neostore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.models.OrderDetails
import kotlinx.android.synthetic.main.item_list_order_details.view.*

class OrderDetailsAdapter(val context: Context,val list:List<OrderDetails>) : RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_order_details,parent,false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.itemView.txt_order_title.text = list[position].prod_name
        holder.itemView.txt_order_catagory.text = "("+ list[position].prod_cat_name +")"
        holder.itemView.txt_order_quantity.text = "QTY "+": "+ list[position].quantity.toString()
        Glide.with(context).load(list[position].prod_image).into(holder.itemView.iv_order_details)
        holder.itemView.txt_order_price.text = "₹"+ list[position].total.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}