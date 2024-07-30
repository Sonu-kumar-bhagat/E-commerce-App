package com.example.myapplicationfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import org.checkerframework.checker.units.qual.C

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
             val sharedpref = getSharedPreferences(Constants.MYSHOPPAL, Context.MODE_PRIVATE)
             val username = sharedpref.getString(Constants.LOGGED_IN_USERNAME,"")



        val userid =  findViewById<TextView>(R.id.user_name)
        val emailid = findViewById<TextView>(R.id.email_id)
        val logoutbtn  :Button = findViewById(R.id.logout)
        userid.text =  username
        //emailid.text = email_id

        logoutbtn.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }
}