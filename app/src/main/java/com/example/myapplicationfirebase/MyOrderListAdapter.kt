package com.example.myapplicationfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.Orders
import com.example.myapplicationfirebase.utils.BoldTextView

open class MyOrderListAdapter(
    private val context: Context,
    private var list: ArrayList<Orders>
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
                R.layout.item_list_layout,
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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            var iv_item_image = holder.itemView.findViewById<ImageView>(R.id.iv_item_image)

            GlideLoader(context).loadProductPicture(
                model.image,
                iv_item_image
            )
            var tv_item_name = holder.itemView.findViewById<BoldTextView>(R.id.tv_item_name)
            var tv_item_price = holder.itemView.findViewById<BoldTextView>(R.id.tv_item_price)
            var ib_delete_product =
                holder.itemView.findViewById<ImageView>(R.id.ib_delete_product)

            tv_item_name.text = model.title
            tv_item_price.text = "$${model.total_amount}"
            ib_delete_product.visibility = View.GONE
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}