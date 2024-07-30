package com.example.myapplicationfirebase

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.models.User
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.example.myapplicationfirebase.utils.MSPEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException

class AddProductActivity : BaseActivity(),  View.OnClickListener {
    lateinit var actionbar: Toolbar
      var mselectedimagefileuri:Uri? =null
    var mproductimageURL:String =""
    lateinit var profile:ImageView
    lateinit var add_product_image:ImageView
    lateinit var ptitle: MSPEditText
    lateinit var pprice:MSPEditText
    lateinit var pdescription: MSPEditText
    lateinit var pquantity :MSPEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
          add_product_image = findViewById<ImageView>(R.id.iv_add_update_product)
        val btn_submit = findViewById<MSPButtonText>(R.id.btn_submit)
        btn_submit.setOnClickListener(this)
        add_product_image.setOnClickListener(this)
        setactionbar()
    }
    private fun setactionbar () {
        actionbar = findViewById(R.id.toolbar_add_product_activity)

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

    override fun onClick(v: View?) {
        if (v !=null)
        {
            when(v.id)
            {
                R.id.iv_add_update_product ->
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES)==PackageManager.PERMISSION_GRANTED )
                    {
                        Constants.showImagechooser(this)
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,),
                            Constants.READ_STORAGE_PERMISSIONS_CODE
                        )
                    }
                }
                R.id.btn_submit ->
                {
                    if (validateProductdetails())
                    {
                        uploadProductimage()
                     }
                }
            }
        }
     }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Constants.showImagechooser(this)

            ShowErrorSnackbar(" Storage permission is granted",false)

        }
        else
        {
            ShowErrorSnackbar("The Storage permission is denied",true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode== Constants.IMAGE_REQUEST_CODE)
            {
                if (data != null)
                {
                    try
                    {
                        profile =findViewById<ImageView>(R.id.iv_product_image)
                        mselectedimagefileuri = data.data!!
                        GlideLoader(this).loadUserPicture(mselectedimagefileuri!!,profile)
                        add_product_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                    }
                    catch (e: IOException)
                    {
                        e.printStackTrace()
                        ShowErrorSnackbar("Image selection failed",true)
                    }


                }
            }
        }
    }

    private fun uploadProductimage()
    {
        showprogressbar()
        FirestoreWork().storeimagetocloudstorage(this,mselectedimagefileuri,Constants.PRODUCT_IMAGE)
    }
    fun imageUploadSuccess(imageURL:Uri)
    {
        mproductimageURL = imageURL.toString()
        uploadProductdetails()
    }
    fun imageuploadfailed()
    {
        Toast.makeText(this,"top",Toast.LENGTH_SHORT).show()
    }
    private fun uploadProductdetails()
    {
        val username = this.getSharedPreferences(
            Constants.MYSHOPPAL, Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME,"")!!

        val product = Product(
            FirestoreWork().getCurrentUserId(),
            username,
            ptitle.text.toString().trim{it <= ' '},
            pprice.text.toString().trim{it <=' '},
            pdescription.text.toString().trim{it <= ' '},
            pquantity.text.toString().trim{it <= ' '},
            mproductimageURL

        )

        FirestoreWork().uploadproductdetails(this,product)

    }
    fun productuploadsuccess()
    {
        hideprogressdialog()
         finish()
    }
    fun productuploadfailed()
    {
        startActivity(Intent(this,FirestoreWork::class.java))
    }
    private fun validateProductdetails() :Boolean
    {
          ptitle = findViewById(R.id.et_product_title)
          pprice = findViewById(R.id.et_product_price)
          pdescription = findViewById (R.id.et_product_description)
          pquantity = findViewById(R.id.et_product_quantity)
          return   when {
            TextUtils.isEmpty(ptitle.text.toString().trim(){it <=' '}) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_enter_product_title),
                    true
                )
                false
            }
            TextUtils.isEmpty(pprice.text.toString().trim(){it <=' '}) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_enter_product_price),
                    true
                )
                false
            }
            TextUtils.isEmpty(pdescription.text.toString().trim(){it <=' '}) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(pquantity.text.toString().trim(){it <=' '}) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    true
                )
                false
            }
             mselectedimagefileuri == null ->{
                 ShowErrorSnackbar(
                     resources.getString(R.string.err_msg_select_product_image),
                     true
                 )
                 false
             }


            else ->
            {

                true
            }
        }


    }
}