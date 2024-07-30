package com.example.myapplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.utils.MSPButtonText

class ProductDetailsActivity :  BaseActivity(), View.OnClickListener {
    var productId:String =""
    var productOwnerId:String=""
    lateinit var addtocart:MSPButtonText
    lateinit var btngotocart:MSPButtonText
    private lateinit var mprouctdetails:Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionbar()
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID))
        {
            productId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID))
        {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
         }
        getproductdetails()
        addtocart = findViewById<MSPButtonText>(R.id.btn_add_to_cart)
       btngotocart = findViewById<MSPButtonText>(R.id.btn_go_to_cart)
        if (FirestoreWork().getCurrentUserId() == productOwnerId)
        {
            addtocart.visibility = View.GONE
            btngotocart.visibility= View.GONE
        }
        else{
            addtocart.visibility = View.VISIBLE
            btngotocart.visibility = View.VISIBLE
        }
        addtocart.setOnClickListener(this)
        btngotocart.setOnClickListener(this)
    }
    private fun setupActionbar()
    {
        val actionbar = findViewById<Toolbar>(R.id.toolbar_product_details_activity)
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
    fun getproductdetails()
    {
        showprogressbar()

        FirestoreWork().getproductdetails(this,productId)
    }
    fun ProductDetailsSuccess(product: Product)
    {
        mprouctdetails = product

        val productimage:ImageView = findViewById(R.id.iv_product_detail_image)
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,productimage
        )
        val title:TextView = findViewById(R.id.tv_product_details_title)
        val price = findViewById<TextView>(R.id.tv_product_details_price)
        val description  = findViewById<TextView>(R.id.tv_product_details_description)
        val stockQuantity = findViewById<TextView>(R.id.tv_product_details_stock_quantity)
        title.text = product.title
        price.text = product.price
        description.text = product.description
        stockQuantity.text = product.stack_quantity
          if (FirestoreWork().getCurrentUserId() == product.user_id)
          {
              hideprogressdialog()
          }
        else
          {

            FirestoreWork().checkIfItemexitIncart(this,productId)

          }

    }

    private fun addToCart()
    {
        val  cartitem= CartItem(
            FirestoreWork().getCurrentUserId(), productId, mprouctdetails.title, mprouctdetails.price,
            mprouctdetails.image,Constants.DEFAULT_CART_QUANTITY
        )
         showprogressbar()
        FirestoreWork().addtoCartItems(this, cartitem)

    }
      fun addToCartSucess()
    {
                    hideprogressdialog()
        Toast.makeText(this , resources.getString(R.string.success_message_item_added_to_cart),Toast.LENGTH_SHORT).show()
        addtocart.visibility = View.GONE
        btngotocart.visibility = View.VISIBLE

    }
    override fun onClick(v: View?) {
        if (v!=null)
        {
            when(v.id)
            {
                R.id.btn_add_to_cart ->
                {
                    addToCart()
                }
                R.id.btn_go_to_cart ->
                {
                    startActivity(Intent(this,CartListActivity::class.java))
                }
            }
        }
     }
    fun productExitIncart()
    {
         hideprogressdialog()
        addtocart.visibility = View.GONE
        btngotocart.visibility= View.VISIBLE
        
    }

}