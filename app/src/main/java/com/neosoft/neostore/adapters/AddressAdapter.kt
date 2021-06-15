package com.neosoft.neostore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neosoft.neostore.R
import com.neosoft.neostore.models.AddressModel
import kotlinx.android.synthetic.main.lis_items_address.view.*

class AddressAdapter(val context: Context,val data:List<AddressModel>) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.lis_items_address,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     holder.itemView.txt_address_fname.text = data.get(position).fname.toString()
     holder.itemView.txt_item_address.text =data.get(position).address.toString()

    }

    override fun getItemCount(): Int {
        return  data.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}