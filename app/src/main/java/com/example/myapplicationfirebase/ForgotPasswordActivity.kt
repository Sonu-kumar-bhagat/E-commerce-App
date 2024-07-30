package com.example.myapplicationfirebase

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.example.myapplicationfirebase.utils.MSPEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    lateinit var actionbar :Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setUpActionbar()
        val email: MSPEditText = findViewById(R.id.et_forgot_email)
            val submit = findViewById<MSPButtonText>(R.id.btn_submit)
        submit.setOnClickListener()
        {
            val forgot_email = email.text.toString().trim {it <= ' '}
            if (forgot_email.isEmpty())
            {
                ShowErrorSnackbar("Please Enter your Email",true)
            }
            else{
                showprogressbar()
                FirebaseAuth.getInstance().sendPasswordResetEmail(forgot_email)
                    .addOnCompleteListener()
                    {
                        task->
                        hideprogressdialog()
                        if(task.isSuccessful)
                        {
                            ShowErrorSnackbar("Confirmation Email Send Successfully",false)
                             finish()
                        }
                        else
                        {
                            ShowErrorSnackbar( task.exception!!.message.toString(),true)

                        }
                    }
            }
        }
    }

    private fun setUpActionbar ()
    {
        actionbar = findViewById(R.id.toolbar_forgot_password_activity)
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
}