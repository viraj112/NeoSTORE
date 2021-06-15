package com.neosoft.neostore.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.ProductDetailsActivity
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClientProduct
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.models.ProductDetailsModel
import com.neosoft.neostore.models.ProductModel
import kotlinx.android.synthetic.main.items_layout_tables.view.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TablesAdapter(val context: Context, val data:List<ProductModel>) :
    RecyclerView.Adapter<TablesAdapter.TablesViewHolder>()  {

     var product_id =1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_layout_tables,parent,false)
        return TablesViewHolder(view)

    }

    override fun onBindViewHolder(holder: TablesViewHolder, position: Int) {
        holder.itemView.txt_title_tables.text = data.get(position).name.toString()
        holder.itemView.txt_sub_title_tables.text = data.get(position).producer.toString()
        holder.itemView.txt_tables_price.text ="Rs"+"."+ data.get(position).cost.toString()
        Glide.with(context).load(data.get(position).product_images).into(holder.itemView.image_view_tables)
        holder.itemView.ratingbar_tables.rating= data.get(position).rating.toInt().toFloat()

        holder.itemView.constraint_item_tables.setOnClickListener{

            product_id=data!!.get(position).id

                val i :Intent= Intent(context,ProductDetailsActivity::class.java)
                i.putExtra("p_id",product_id)
                context.startActivity(i)
            }

    }




    override fun getItemCount(): Int {

        return data.size
    }



    class TablesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {

    }


}