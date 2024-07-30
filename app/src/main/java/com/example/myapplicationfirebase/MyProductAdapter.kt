package com.example.myapplicationfirebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.bottomnavigation.fragments.ProductsFragment
import com.example.myapplicationfirebase.models.Product

open class MyProductAdapter
    (private val context: Context, private var list:ArrayList<Product>, private var fragment: ProductsFragment):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_ui, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder)
        {
            val iv_item_image1 = holder.itemView.findViewById<ImageView>(R.id.iv_item_image)
            GlideLoader(context).loadProductPicture(model.image, iv_item_image1)
          val  tv_item_name1 = holder.itemView.findViewById<TextView>(R.id.tv_item_name)
            tv_item_name1.text = model.title
            val  tv_item_price1 = holder.itemView.findViewById<TextView>(R.id.tv_item_price)
            tv_item_price1.text = model.price
            val iv_delete = holder.itemView.findViewById<ImageView>(R.id.ib_delete_product)
            iv_delete.setOnClickListener()
            {
                 fragment.deleteProduct(model.product_id)
              }
            holder.itemView.setOnClickListener()
            {
                val intent  = Intent (context,ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                context.startActivity(intent)
            }

        }
     }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
    {

    }
}