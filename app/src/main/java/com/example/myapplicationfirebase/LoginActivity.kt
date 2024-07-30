package com.example.myapplicationfirebase

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.example.myapplicationfirebase.utils.BoldTextView
import com.example.myapplicationfirebase.models.User
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.example.myapplicationfirebase.utils.MSPEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : BaseActivity() {
    lateinit var firstname : MSPEditText
    lateinit var lastname: MSPEditText
    lateinit var email : MSPEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
         val register = findViewById<BoldTextView>(R.id.tv_register)
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
        val tv_forgotPassword : BoldTextView = findViewById(R.id.tv_forgot_password)

        tv_forgotPassword.setOnClickListener()
        {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        register.setOnClickListener()
        {
            val intent = Intent (this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
         }
         val login: MSPButtonText = findViewById(R.id.btn_login)
        login.setOnClickListener(){

            validateregisterdetails()

        }


     }
    private fun validateregisterdetails() :Boolean
    {
        val et_email = findViewById<TextView>(R.id.et_email)
        val et_password = findViewById<TextView>(R.id.et_password)
        return   when {

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




            else ->
            {
                showprogressbar()
                val email:String = et_email.text.toString().trim{it <= ' '}
                val password :String = et_password.text.toString().trim{it <= ' '}
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener()
                    {

                            task ->
                         if (task.isSuccessful)
                        {
                              FirestoreWork().getUserDetails(this)

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

    fun userLoggedInSuccess(user: User)

    {
        hideprogressdialog()
         Log.i("First Name",user.firstname)
        Log.i("Last Name",user.lastname)
       Log.i("email",user.email)
             if (user.Profilecompleted ==0) {
                 val intent = Intent(this, UserProfileActivity::class.java)
                 intent.putExtra(Constants.EXTRA_LOGIN_DETAILS,user)

                  startActivity(intent)
             }
        else
             {
                 startActivity(Intent(this,DashboardActivity11::class.java))

             }
        finish()
    }
}