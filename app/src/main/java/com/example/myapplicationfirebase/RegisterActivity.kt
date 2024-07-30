package com.example.myapplicationfirebase

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.models.User
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity :  BaseActivity() {
    lateinit var actionbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setUpActionbar ()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }

        else
        {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN

            )
        }
              val login : BoldTextView = findViewById(R.id.tv_login)
        login.setOnClickListener()
        {
           startActivity(Intent(this@RegisterActivity,  LoginActivity::class.java))
        }

        val registerbtn = findViewById<MSPButtonText>(R.id.btn_register)

        registerbtn.setOnClickListener()
        {
            validateregisterdetails()
        }
    }
private fun setUpActionbar ()
{
    actionbar = findViewById(R.id.toolbar_register_activity)
    setSupportActionBar(actionbar)
    if (actionbar!=null)
    {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
     }
    actionbar.setNavigationOnClickListener()
    {
        onBackPressed()
    }
}

    private fun validateregisterdetails() :Boolean
    {
        val et_first_name = findViewById<TextView>(R.id.et_first_name)
        val et_last_name = findViewById<TextView>(R.id.et_last_name)
        val et_email = findViewById<TextView>(R.id.et_email)
       val et_password = findViewById<TextView>(R.id.et_password)
        val  confirmpassword = findViewById<TextView>(R.id.et_confirm_password)
       val cb_term_and_condition = findViewById<AppCompatCheckBox>(R.id.cb_terms_and_condition)
      return   when {
          TextUtils.isEmpty(et_first_name.text.toString().trim(){it <=' '}) -> {
                ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_enter_first_name),
                  true
              )
              false
          }
          TextUtils.isEmpty(et_last_name.text.toString().trim(){it <=' '}) -> {
               ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_enter_last_name),
                  true
              )
              false
          }
          TextUtils.isEmpty(et_email.text.toString().trim(){it <=' '}) -> {
               ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_enter_email),
                  true
              )
              false
          }

          TextUtils.isEmpty(et_password.text.toString().trim(){it <=' '}) -> {
               ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_enter_password),
                  true
              )
              false
          }
          TextUtils.isEmpty(confirmpassword.text.toString().trim(){it <=' '}) -> {
               ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_enter_confirm_password),
                  true
              )
              false
          }
          et_password.text.toString().trim{it <= ' '} != confirmpassword.text.toString().trim{it <= ' '} ->
          {
               ShowErrorSnackbar(
                  resources.getString(R.string.error_msg_password_and_confirm_password_mismatch),
                  true
              )
              false
          }
           !cb_term_and_condition.isChecked ->
           {
                ShowErrorSnackbar(
                   resources.getString(R.string.i_agree_to_the_terms_and_condition),
                   true
               )
               false
           }
          else ->
          {
              val email:String = et_email.text.toString().trim{it <= ' '}
              val password :String =  et_password.text.toString().trim{it <= ' '}
              val firstname :String = et_first_name.text.toString().trim{it <= ' '}
              val lastname :String = et_last_name.text.toString().trim{it <= ' '}

              showprogressbar()
              FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener()
                      {  task ->

                          if (task.isSuccessful)
                          {
                              val firebaseuser:FirebaseUser = task.result!!.user!!
                              val user =User(firebaseuser.uid, firstname, lastname,email
                                  )
                              FirestoreWork().registerUser(this,user)

                                  val intent = Intent(this, LoginActivity::class.java)
                              intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                              intent.putExtra("user_id",firebaseuser.uid)
                              intent.putExtra("email_id",email)
                              startActivity(intent)

                               finish()
                          } else
                          {
                              hideprogressdialog()
                              ShowErrorSnackbar(
                                  task.exception!!.message.toString(),
                                  true)
                          }



                      }





              true
          }
      }


    }

}