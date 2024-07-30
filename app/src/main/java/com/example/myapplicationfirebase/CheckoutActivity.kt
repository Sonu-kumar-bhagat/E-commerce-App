package com.example.myapplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.Address
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.utils.BoldTextView
import java.util.ArrayList
import com.example.myapplicationfirebase.FirestoreWork
import com.example.myapplicationfirebase.models.Orders
import com.example.myapplicationfirebase.utils.MSPButtonText


class CheckoutActivity : BaseActivity() {
    private var mAddressdetails : Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0

    // A global variable for the Total Amount.
    private var mTotalAmount: Double = 0.0
    // END
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS))
        {
            mAddressdetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)

        }
        if (mAddressdetails !=null)
        {
            val tv_checkout_address_type = findViewById<BoldTextView>(R.id.tv_checkout_address_type)
            val tv_checkout_full_name = findViewById<BoldTextView>(R.id.tv_checkout_full_name)
            val tv_checkout_address = findViewById<BoldTextView>(R.id.tv_checkout_address)
            val tv_checkout_additional_note = findViewById<BoldTextView>(R.id.tv_checkout_additional_note)
            val tv_checkout_other_details = findViewById<BoldTextView>(R.id.tv_checkout_other_details)
            val tv_checkout_mobile_number = findViewById<BoldTextView>(R.id.tv_checkout_mobile_number)

            tv_checkout_address_type.text = mAddressdetails?.type
            tv_checkout_full_name.text = mAddressdetails?.name
            tv_checkout_address.text = "${mAddressdetails!!.address}, ${mAddressdetails!!.zipCode}"
            tv_checkout_additional_note.text = mAddressdetails?.additionalNote

           if (mAddressdetails?.otherDetails!!.toString().isNotEmpty() ){
                tv_checkout_other_details.text = mAddressdetails?.otherDetails
            }
            tv_checkout_mobile_number.text = mAddressdetails?.mobileNumber
        }
        getProductList()

        var placeorder = findViewById<BoldTextView>(R.id.btn_place_order)
        placeorder.setOnClickListener()
        {
            placeAnOrder()
        }

    }

    private fun getProductList() {

        // Show the progress dialog.
                     showprogressbar()
       FirestoreWork().getAllProductsList(this)
    }
   fun  successproductlistfromfirestor(productlist:ArrayList<Product>)
   {
       mProductsList = productlist
       getCartItemsList()
   }
    private fun getCartItemsList() {

        com.example.myapplicationfirebase.FirestoreWork().getcartlist(this)
    }
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        // Hide progress dialog.
        hideprogressdialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stack_quantity
                }
            }
        }

        mCartItemsList = cartList
           val rv_cart_list_items = findViewById<RecyclerView>(R.id.rv_cart_list_items)
       rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = com.example.myapplicationfirebase.CartListAdapter1(
            this@CheckoutActivity,
            mCartItemsList,
            false
        )
        rv_cart_list_items.adapter = cartListAdapter

        // TODO Step 4: Replace the subTotal and totalAmount variables with the global variables.
        // START
        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

             mSubTotal += (price * quantity)
            }
        }
         var tv_checkout_sub_total = findViewById<BoldTextView>(R.id.tv_checkout_sub_total)
        var tv_checkout_shipping_charge = findViewById<BoldTextView>(R.id.tv_checkout_shipping_charge)
        var ll_checkout_place_order = findViewById<LinearLayout>(R.id.ll_checkout_place_order)
        var tv_checkout_total_amount = findViewById<BoldTextView>(R.id.tv_checkout_total_amount)

        tv_checkout_sub_total.text = "₹$mSubTotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        tv_checkout_shipping_charge.text = "₹10.0"

        if (mSubTotal > 0) {
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10.0
            tv_checkout_total_amount.text = "₹$mTotalAmount"
        } else {
            ll_checkout_place_order.visibility = View.GONE
        }

    }

    private fun placeAnOrder() {

        // Show the progress dialog.
               showprogressbar()
        // TODO Step 5: Now prepare the order details based on all the required details.
        // START
        val order = Orders(
            FirestoreWork().getCurrentUserId(),
            mCartItemsList,
            mAddressdetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal.toString(),
            "₹10.0", // The Shipping Charge is fixed as $10 for now in our case.
            mTotalAmount.toString(),
        )
        // END

        // TODO Step 10: Call the function to place the order in the cloud firestore.
        // START
        FirestoreWork().placeOrder(this, order)
        // END
    }
    fun orderPlacedSuccess() {

         FirestoreWork().updateAllDetails(this,mCartItemsList,)
    }

    fun allDetailsUpdatedSuccessfully() {

        // Hide the progress dialog.
          hideprogressdialog()
        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity11::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}