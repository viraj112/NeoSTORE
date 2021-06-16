package com.neosoft.neostore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.neosoft.neostore.R
import com.neosoft.neostore.api.Api
import com.neosoft.neostore.api.RetrofitClient
import com.neosoft.neostore.api.RetrofitClientCart
import com.neosoft.neostore.constants.Constants
import com.neosoft.neostore.database.AddressDatabase
import com.neosoft.neostore.database.AddressEntity

import kotlinx.android.synthetic.main.lis_items_address.view.*

class AddressAdapter(
    val context: Context,
    var allAddress: List<AddressEntity>,
    private val onItemClick: OnItemClick
) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {


    private var listdata: MutableList<AddressEntity> = allAddress as MutableList<AddressEntity>
    lateinit var add: String
    val database = Room.databaseBuilder(
        context,
        AddressDatabase::class.java,
        "addressdatabase"
    )
        .allowMainThreadQueries()
        .build()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.lis_items_address, parent, false)
        return MyViewHolder(view)
    }

    private var checkdRadioButton: CompoundButton? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.txt_address_fname.text = allAddress.get(position).name
        holder.itemView.txt_item_address.text = allAddress.get(position).address
        holder.itemView.iv_delete.setOnClickListener {
            //var addressEntity :AddressEntity
            val id: Int = allAddress.get(position).id
            database.addressDao().deleteAddress(
                AddressEntity(
                    id = id,
                    name = allAddress.get(position).name,
                    address = allAddress.get(position).address
                )
            )
            deleteItem(position)

        }

        holder.itemView.radiobtn_address.setOnClickListener {
            add = allAddress.get(position).address
            onItemClick.onClick(add)
        }

        holder.itemView.radiobtn_address.setOnCheckedChangeListener(chekckedChangeListner)
        if (holder.itemView.radiobtn_address.isChecked) checkdRadioButton =
            holder.itemView.radiobtn_address
    }

    private val chekckedChangeListner =
        CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            checkdRadioButton?.apply {
                setChecked(!isChecked)

            }
            checkdRadioButton = compoundButton.apply {
                setChecked(isChecked)
                checkdRadioButton = radiobtn_address


            }
        }

    private fun deleteItem(position: Int) {
        listdata.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listdata.size
    }


    interface OnItemClick {
        fun onClick(myadd: String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}