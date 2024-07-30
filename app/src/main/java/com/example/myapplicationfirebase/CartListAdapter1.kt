package com.example.myapplicationfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.utils.BoldTextView
import java.util.ArrayList

open class CartListAdapter1 (
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private var updatecartitems:Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartListAdapter1.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size

     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is CartListAdapter1.MyViewHolder) {
            val iv_cart_item_image =
                holder.itemView.findViewById<ImageView>(R.id.iv_cart_item_image)

            com.example.myapplicationfirebase.GlideLoader(context)
                .loadProductPicture(model.image, iv_cart_item_image)
            val tv_cart_item_title =
                holder.itemView.findViewById<BoldTextView>(R.id.tv_cart_item_title)

            tv_cart_item_title.text = model.title
            val tv_cart_item_price =
                holder.itemView.findViewById<BoldTextView>(R.id.tv_cart_item_price)

            tv_cart_item_price.text = "₹${model.price}"
            val tv_cart_quantity = holder.itemView.findViewById<BoldTextView>(R.id.tv_cart_quantity)

            tv_cart_quantity.text = model.cart_quantity
            val ib_remove_cart_item =
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item)
            val ib_add_cart_item = holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item)
            var ib_delete_cart_item = holder.itemView.findViewById<ImageView>(R.id.ib_delete_cart_item)

            if (model.cart_quantity == "0") {
                ib_remove_cart_item.visibility = View.GONE
                ib_add_cart_item.visibility = View.GONE
                  if (updatecartitems)
                  {
                      ib_delete_cart_item.visibility = View.VISIBLE

                  }
                else{
                      ib_delete_cart_item.visibility = View.GONE

                  }
                tv_cart_quantity.text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                if(updatecartitems) {
                    ib_remove_cart_item.visibility = View.VISIBLE
                    ib_add_cart_item.visibility = View.VISIBLE
                    ib_delete_cart_item.visibility = View.VISIBLE
                }
                else
                {
                    ib_remove_cart_item.visibility = View.GONE
                    ib_add_cart_item.visibility = View.GONE
                    ib_delete_cart_item.visibility = View.GONE
                }
                tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorsecondarytext
                    )
                )
            }

            // TODO Step 1: Assign the click event to the ib_remove_cart_item.
           /* ib_remove_cart_item.setOnClickListener {

                // TODO Step 6: Call the update or remove function of firestore class based on the cart quantity.
                // START
                if (model.cart_quantity == "1") {
                    FirestoreWork().removeItemFromCart(context, model.id)
                } else {

                    val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                    // Show the progress dialog.

                    if (context is CartListActivity) {
                        context.showprogressbar()
                    }

                    FirestoreWork().updateMyCart(context, model.id, itemHashMap)
                }
                // END
            }*/
            // END

            // TODO Step 7: Assign the click event to the ib_add_cart_item.
            // START
            /*  val ib_add_cart_item = holder.itemView.findViewById<BoldTextView>(R.id.ib_add_cart_item)

        ib_add_cart_item.setOnClickListener {

            // TODO Step 8: Call the update function of firestore class based on the cart quantity.
            // START
            val cartQuantity: Int = model.cart_quantity.toInt()

            if (cartQuantity < model.stock_quantity.toInt()) {

                val itemHashMap = HashMap<String, Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                // Show the progress dialog.
                if (context is CartListActivity) {
                    context.showprogressbar()
                }

                FirestoreWork().updateMyCart(context, model.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    context.ShowErrorSnackbar(
                        context.resources.getString(
                            R.string.msg_for_available_stock,
                            model.stock_quantity
                        ),
                        true
                    )
                }
            }
            // END
        }
        // END

        var ib_delete_cart_item = holder.itemView.findViewById<ImageView>(R.id.ib_delete_cart_item)

        ib_delete_cart_item.setOnClickListener {

            when (context) {
                is CartListActivity -> {
                    context.showprogressbar()
                }
            }

            FirestoreWork().removeItemFromCart(context, model.id)
        }*/
        }
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}