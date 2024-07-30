package com.example.myapplicationfirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.Product

open class DashboardItemListAdapter(private val context: Context, private var list:ArrayList<Product>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var onClickListener:OnClickListener? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is  MyViewHolder)
        {
            val iv_item_image1 = holder.itemView.findViewById<ImageView>(R.id.iv_dashboard_item_image)
            GlideLoader(context).loadProductPicture(model.image, iv_item_image1)
            val  tv_item_name1 = holder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_title )
            tv_item_name1.text = model.title
            val  tv_item_price1 = holder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_price)
            tv_item_price1.text = model.price
            holder.itemView.setOnClickListener()
            {
                if(onClickListener != null)
                {
                    onClickListener!!.onClick(position,model)
                }
            }

        }
     }
    fun setOnClickListener(onClickListener: OnClickListener)
    {
        this.onClickListener = onClickListener
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
    {

    }
    interface OnClickListener
    {
        fun onClick(position:Int,product:Product)
    }
}