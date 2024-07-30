package com.example.myapplicationfirebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.Address
import com.example.myapplicationfirebase.utils.BoldTextView

/**
 * An adapter class for AddressList adapter.
 */
// TODO Step 8: Add the parameter to pass the value of address selection so when the user is about to add, edit or, delete the address he should not be able to select the address.
open class AddressList_Adapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_address_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
           val tv_address_full_name =   holder.itemView.findViewById<BoldTextView>(R.id.tv_address_full_name)
        val tv_address_type =   holder.itemView.findViewById<BoldTextView>(R.id.tv_address_type)
        val tv_address_details =   holder.itemView.findViewById<BoldTextView>(R.id.tv_address_details)
        val tv_address_mobile_number =   holder.itemView.findViewById<BoldTextView>(R.id.tv_address_mobile_number)


        if (holder is MyViewHolder) {
          tv_address_full_name.text = model.name
            tv_address_type.text = model.type
            tv_address_details.text = "${model.address}, ${model.zipCode}"
            tv_address_mobile_number.text = model.mobileNumber

            // TODO Step 10: Assign the click event to the address item when user is about to select the address.
            // START
            if (selectAddress) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(context,CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                    context.startActivity(intent)
                }
            }
            // END
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to edit the address details and pass the existing details through intent.
     *
     * @param activity
     * @param position
     */

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)}
