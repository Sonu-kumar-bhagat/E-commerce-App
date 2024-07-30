package com.example.myapplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.models.User
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : BaseActivity() {
     lateinit var actionbar:Toolbar
     lateinit var muserdetails :User
     lateinit var profileimage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setactionbar()
        val logoutbtn:MSPButtonText = findViewById (R.id.btn_logout)
        val edit :BoldTextView = findViewById(R.id.tv_edit)
        logoutbtn.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@SettingActivity,LoginActivity::class.java)
            intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK  or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        edit.setOnClickListener()
        {
            val intent = Intent(this@SettingActivity,UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_LOGIN_DETAILS,muserdetails)
            startActivity(intent)
        }
        val address = findViewById<LinearLayout>(R.id.ll_address)
        address.setOnClickListener()
        {
            startActivity(Intent(this,address_list::class.java))
        }

     }
private fun setactionbar () {
    actionbar = findViewById(R.id.toolbar_settings_activity)

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

    private fun getuserdetails ()
    {
        showprogressbar()
         FirestoreWork().getUserDetails(this)
    }
    fun userdetailssuccess(user: User)
    {
        muserdetails =user

        hideprogressdialog()
          profileimage = findViewById(R.id.iv_user_photo)
        GlideLoader(this@SettingActivity).loadUserPicture(user.image,profileimage)
        val name = findViewById<BoldTextView>(R.id.tv_name)
        val gender = findViewById<BoldTextView>(R.id.tv_gender)
        val email = findViewById<BoldTextView>(R.id.tv_email)
        val mobile = findViewById<BoldTextView>(R.id.tv_mobile_number)
        name.text = "${user.firstname} ${user.lastname}"
        gender.text  = user.gender
        email.text = user.email
        mobile.text  = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getuserdetails()
    }

}