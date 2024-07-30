package com.example.myapplicationfirebase

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplicationfirebase.models.User
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.example.myapplicationfirebase.utils.MSPEditText
import com.example.myapplicationfirebase.utils.MSPRadioButton
import java.io.IOException

class UserProfileActivity : BaseActivity() {
    lateinit var userdetails :User
    lateinit var actionbar:Toolbar
    lateinit var mobilenumber: MSPEditText
    lateinit var rb_male: MSPRadioButton
    lateinit var rb_female:MSPRadioButton
    var   mselectedimagefileuri: Uri? = null
    var  imageurl11:String?=null
    lateinit var ptitle: TextView
    lateinit var profile:ImageView
    lateinit var firstname:MSPEditText
    lateinit var lastname:MSPEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
   val et_firstname = findViewById<MSPEditText>(R.id.et_first_name)
        val et_lastname = findViewById<MSPEditText>(R.id.et_last_name)
        val et_email= findViewById<MSPEditText>(R.id.et_email)
       rb_male = findViewById(R.id.rb_male)
        rb_female= findViewById(R.id.rb_female)
        mobilenumber = findViewById(R.id.et_mobile_number)


        if (intent.hasExtra(Constants.EXTRA_LOGIN_DETAILS))
        {
            userdetails = intent.getParcelableExtra(Constants.EXTRA_LOGIN_DETAILS)!!
        }
        ptitle = findViewById(R.id.tv_title)
        et_firstname.setText(userdetails.firstname)
        et_lastname.setText(userdetails.lastname)

        et_email.setText(userdetails.email)
        et_email.isEnabled = false
        if (userdetails.Profilecompleted == 0)
        {
            et_firstname.isEnabled = false
            et_lastname.isEnabled = false
            ptitle.text = resources.getString(R.string.title_complete_profile)

        }
        else{
            setactionbar ()
           et_firstname.isEnabled = true
           et_lastname.isEnabled = true
             ptitle.setText(R.string.title_edit_profilr)
            profile = findViewById(R.id.iv_user_photo)
            GlideLoader(this@UserProfileActivity).loadUserPicture(userdetails.image,profile)
           if (userdetails.mobile != 0L )
            {
                mobilenumber.setText(userdetails.mobile.toString())
            }
           if (userdetails.gender == Constants.MALE)
            {
               rb_male.isChecked =true
           }
            else
            {
                rb_female.isChecked = true
            }

        }




        val  imageframelayout:FrameLayout = findViewById(R.id.fl_user_image)

                 imageframelayout.setOnClickListener()
             {
                 if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                 {
                   Constants.showImagechooser(this)
                 //  ShowErrorSnackbar("Permission is already granted for Storage",false)
                 }
                 else{

                     ActivityCompat.requestPermissions(
                         this,
                         arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                         Constants.READ_STORAGE_PERMISSIONS_CODE
                     )
                 }
             }
        val btnsubmit : MSPButtonText = findViewById(R.id.btn_submit)
        btnsubmit.setOnClickListener()
        {

             if(validateuserprofiledetails())
             {
                 showprogressbar()
                 if (mselectedimagefileuri != null) {
                     FirestoreWork().storeimagetocloudstorage(this, mselectedimagefileuri,Constants.USER_PROFILE_IMAGE)
                 }
                 else
                 {
                     updateuserdetails()
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
                              profile =findViewById<ImageView>(R.id.iv_user_photo)
                              mselectedimagefileuri = data.data!!
                            GlideLoader(this).loadUserPicture(mselectedimagefileuri!!,profile)
                         }
                    catch (e:IOException)
                    {
                      e.printStackTrace()
                        ShowErrorSnackbar("Image selection failed",true)
                    }


                }
            }
        }
    }

    private fun validateuserprofiledetails():Boolean
    {
            mobilenumber = findViewById<MSPEditText>(R.id.et_mobile_number)
        return when{
            TextUtils.isEmpty(mobilenumber.text.toString().trim { it <= ' ' }) ->
            {
                hideprogressdialog()
                ShowErrorSnackbar("Please Enter Your Mobile Number",true)
               false
            }
            else ->
            {
                true
            }
        }
    }
    fun  onsuccessupdate()
    {
        hideprogressdialog()
        startActivity(Intent(this, DashboardActivity11::class.java))
        finish()
    }
fun updateuserdetails()
{
    val userhashmap = HashMap<String,Any> ()
    val mobileNumber = mobilenumber.text.toString().trim{it <= ' '}

    rb_male = findViewById(R.id.rb_male)

         firstname = findViewById(R.id.et_first_name)
         lastname = findViewById(R.id.et_last_name)
    val firstName = firstname.text.toString().trim{it <= ' '}
    if (firstName != userdetails.firstname)
    {
        userhashmap[Constants.FIRST_NAME] = firstName
    }
    val  lastName = lastname.text.toString().trim{it <= ' '}
    if (lastName != userdetails.firstname)
    {
        userhashmap[Constants.FIRST_NAME] = lastName
    }
    val gender :String = if (rb_male.isChecked)
    {
        Constants.MALE
    } else
    {
        Constants.FEMALE
    }
    if (mobileNumber.isNotEmpty() && mobileNumber != userdetails.mobile.toString())
    {
    userhashmap[Constants.MOBILE] = mobileNumber.toLong()
    }
    if (gender.isNotEmpty() && gender != userdetails.gender) {

        userhashmap[Constants.GENDER] = gender
    }
    if(imageurl11 != null)
    {

        userhashmap[Constants.USER_PROFILE_IMAGE] = imageurl11!!

    }
    userhashmap[Constants.PROFILE_COMPLETED ] =1
    FirestoreWork().updateuserprofiledata(this,userhashmap)

}
fun imageUploadSuccess(imageURL:Uri)
{

    imageurl11 = imageURL.toString()
       updateuserdetails()
}
    private fun setactionbar ()
    {
        actionbar = findViewById(R.id.toolbar_user_profile_activity)

        setSupportActionBar(actionbar)
        val actionBar = supportActionBar
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        }
        actionbar.setNavigationOnClickListener()
        {
            onBackPressed()
        }

    }
}