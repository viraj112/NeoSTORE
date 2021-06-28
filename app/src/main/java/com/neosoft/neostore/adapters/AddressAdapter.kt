package com.neosoft.neostore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.neosoft.neostore.R
import com.neosoft.neostore.database.AddressDatabase
import com.neosoft.neostore.database.AddressEntity

import kotlinx.android.synthetic.main.lis_items_address.view.*

class AddressAdapter(val context: Context, var allAddress: List<AddressEntity>, private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {
    private var listData: MutableList<AddressEntity> = allAddress as MutableList<AddressEntity>
    lateinit var add: String
    val database = Room.databaseBuilder(context, AddressDatabase::class.java, "addressee's")
        .allowMainThreadQueries()
        .build()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lis_items_address, parent, false)
        return MyViewHolder(view)
    }
    private var checkedRadioButton: CompoundButton? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_address_fname.text = allAddress[position].name
        holder.itemView.txt_item_address.text = allAddress[position].address
        holder.itemView.iv_delete.setOnClickListener {
            val id: Int = allAddress[position].id
            database.addressDao().deleteAddress(AddressEntity(id = id, name = allAddress[position].name, address = allAddress[position].address))
            deleteItem(position)
        }
        holder.itemView.radiobtn_address.setOnClickListener {
            add = allAddress[position].address
            onItemClick.onClick(add)
        }
        holder.itemView.radiobtn_address.setOnCheckedChangeListener(checkedChangeListener)
        if (holder.itemView.radiobtn_address.isChecked) checkedRadioButton =
            holder.itemView.radiobtn_address
    }
    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            checkedRadioButton?.apply {
                setChecked(!isChecked)
            }
            checkedRadioButton = compoundButton.apply {
                setChecked(isChecked)
                checkedRadioButton = radiobtn_address
            }
        }
    private fun deleteItem(position: Int) {
        listData.removeAt(position)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return listData.size
    }
    interface OnItemClick {
        fun onClick(myAddress: String)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}