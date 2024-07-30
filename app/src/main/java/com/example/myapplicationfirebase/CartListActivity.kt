package com.example.myapplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.utils.MSPButtonText
import org.checkerframework.checker.units.qual.C
import java.util.ArrayList

class CartListActivity : BaseActivity() {
    lateinit var productlist1: ArrayList<Product>
    lateinit var mCartListItems: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionbar()

        var btn_checkout = findViewById<MSPButtonText>(R.id.btn_checkout)
        btn_checkout.setOnClickListener()
        {
            val intent = Intent(this,address_list::class.java)
            intent.putExtra("pyq",true)
            startActivity(intent)
        }
    }
    private fun setupActionbar()
    {
        val actionbar = findViewById<Toolbar>(R.id.toolbar_cart_list_activity)
        setSupportActionBar(actionbar)
        val actionbar1 = supportActionBar
        if (actionbar1!= null)
        {
            actionbar1.setDisplayHomeAsUpEnabled(true)
            actionbar1.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }
        actionbar.setNavigationOnClickListener()
        {
            onBackPressed()
        }
    }

    fun getcartitemlist() {
        showprogressbar()
        FirestoreWork().getcartlist(this)

    }
    override fun onResume() {
        super.onResume()
        getproductlist()
    }
    fun successcartitemlist(cartList:ArrayList<CartItem>)
    {
          hideprogressdialog()
        for (product in productlist1) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {

                    cart.stock_quantity = product.stack_quantity

                    if (product.stack_quantity.toInt() == 0) {
                        cart.cart_quantity = product.stack_quantity
                    }
                }
            }
        }
        mCartListItems = cartList

        if (mCartListItems.size>0) {
            val rv_cart_items_list = findViewById<RecyclerView>(R.id.rv_cart_items_list)
            rv_cart_items_list.visibility = View.VISIBLE
            val ll_checkout = findViewById<LinearLayout>(R.id.ll_checkout)
            ll_checkout.visibility = View.VISIBLE
            val rv_no_cart_item_found = findViewById<BoldTextView>(R.id.tv_no_cart_item_found)
            rv_no_cart_item_found.visibility = View.GONE
           rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

              val cartListAdapter = CartListAdapter1(this@CartListActivity, mCartListItems,true)
           rv_cart_items_list.adapter = cartListAdapter

       var subTotal: Double = 0.0

           for (item in mCartListItems) {

               val availableQuantity = item.stock_quantity.toInt()

               if (availableQuantity > 0) {
                   val price = item.price.toDouble()
                   val quantity = item.cart_quantity.toInt()

                   subTotal += (price * quantity)
               }
           }
            val tv_sub_total = findViewById<BoldTextView>(R.id.tv_sub_total)
           tv_sub_total.text = "₹$subTotal"
           // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
           val tv_shipping_charge = findViewById<BoldTextView>(R.id.tv_shipping_charge)

           tv_shipping_charge.text = "₹10.0"

           if (subTotal > 0) {
               ll_checkout.visibility = View.VISIBLE

               val total = subTotal + 10
               val tv_total_amount = findViewById<BoldTextView>(R.id.tv_total_amount)

               tv_total_amount.text = "₹$total"
           } else {
               ll_checkout.visibility = View.GONE
           }

        }
        else {
            val rv_cart_items_list = findViewById<RecyclerView>(R.id.rv_cart_items_list)
            val ll_checkout = findViewById<LinearLayout>(R.id.ll_checkout)
            val rv_no_cart_item_found = findViewById<BoldTextView>(R.id.tv_no_cart_item_found)

            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            rv_no_cart_item_found.visibility = View.VISIBLE
        }

    }



    fun successproductlistfromfirestor(productlist: ArrayList<Product>)
    {
        hideprogressdialog()
        productlist1 = productlist
        getcartitemlist()

    }
    fun getproductlist() {
        showprogressbar()
        FirestoreWork().getAllProductsList(this)

    }
    fun itemUpdateSuccess()
    {
        hideprogressdialog()
        getcartitemlist()
    }

    fun itemRemovedSuccess()
    {
        hideprogressdialog()

    }
}