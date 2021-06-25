package com.neosoft.neostore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.OrdersDetailsActivity
import com.neosoft.neostore.models.OrderModel
import kotlinx.android.synthetic.main.item_list_order.view.*

class MyordersAdapter(val context: Context,val list:List<OrderModel>) : RecyclerView.Adapter<MyordersAdapter.MyViewHolder>() {
    private var orderIid=1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_order, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_order_id.text = context.getString(R.string.orderid)+":"+ list[position].id.toString()
        holder.itemView.txt_ordered_date.text =context.getString(R.string.ordered_date)+":"+ list[position].created
        holder.itemView.txt_order_price.text = "\u20B9"+ list[position].cost.toString()

        holder.itemView.orders_layout.setOnClickListener {
            orderIid = list[position].id
            val i = Intent(context,OrdersDetailsActivity::class.java)
            i.putExtra("id",orderIid)
            context.startActivity(i)

        }
    }

    override fun getItemCount():Int {
        return list.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
