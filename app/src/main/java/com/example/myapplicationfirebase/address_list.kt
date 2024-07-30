package com.example.myapplicationfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationfirebase.models.Address
import com.example.myapplicationfirebase.utils.BoldTextView

class address_list : BaseActivity() {
    lateinit var actionbar: Toolbar
    private var mselectedaddress:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setactionbar()
        getaddresslist()

        val setadd = findViewById<BoldTextView>(R.id.tv_add_address)

        setadd.setOnClickListener()
        {
          var intent =  Intent(this,Add_edit_Address::class.java)
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS))
        {
            mselectedaddress = intent.getBooleanExtra("pyq",false)
        }

        if (mselectedaddress)
        {
            var tv_title = findViewById<BoldTextView>(R.id.tv_title)
            tv_title.text = "Select Address"
        }

     }
    private fun setactionbar () {
        actionbar = findViewById(R.id.toolbar_address_list_activity)

        setSupportActionBar(actionbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        }
        actionbar.setNavigationOnClickListener()
        {
            onBackPressed()
        }
    }
    fun successAddressListFromFirestore(addresslist:ArrayList<Address>) {
        hideprogressdialog()
        if (addresslist.size > 0) {
              var rv_address_list = findViewById<RecyclerView>(R.id.rv_address_list)
            var tv_no_address_found = findViewById<BoldTextView>(R.id.tv_no_address_found)
            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE

            rv_address_list.layoutManager = LinearLayoutManager(this)
            rv_address_list.setHasFixedSize(true)

            // TODO Step 9: Pass the address selection value.
            // START
            val addressAdapter =
                AddressList_Adapter(this, addresslist,true)
            // END
            rv_address_list.adapter = addressAdapter

        }
        else
        {
            var rv_address_list = findViewById<RecyclerView>(R.id.rv_address_list)
            var tv_no_address_found = findViewById<BoldTextView>(R.id.tv_no_address_found)
            rv_address_list.visibility = View.GONE
            tv_no_address_found.visibility = View.VISIBLE
        }
    }
    private fun getaddresslist()
    {
        showprogressbar()
        FirestoreWork().getAddressesList(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            getaddresslist()
        }
    }
}