package com.neosoft.neostore.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.ProductDetailsActivity
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.SofaListModel
import kotlinx.android.synthetic.main.items_layout_tables.view.*

class SofasAdapter(val context:Context,val data:ArrayList<SofaListModel.SofaModel>): RecyclerView.Adapter<SofasAdapter.MyViewHolder>() {
    private var productId = Constants.PRODUCT_ID


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val view= LayoutInflater.from(context).inflate(R.layout.items_layout_tables,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_title_tables.text = data[position].name
        holder.itemView.txt_sub_title_tables.text = data[position].producer
        holder.itemView.txt_tables_price.text = data[position].cost.toString()
        Glide.with(context).load(data[position].product_images).into(holder.itemView.image_view_tables)
        holder.itemView.ratingbar_tables.rating= data[position].rating.toFloat()
        holder.itemView.constraint_item_tables.setOnClickListener{
            productId= data[position].id
            val i = Intent(context, ProductDetailsActivity::class.java)
            i.putExtra("p_id",productId)
            context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {
    return  data.size
    }

    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

    }
}