package com.example.myapplicationfirebase

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
private lateinit var mProgressDialog : Dialog
private var doublebackpressedtoexit = false
   fun ShowErrorSnackbar (message:String, errorMessage:Boolean)
   {
       val snackBar = Snackbar.make(findViewById(android.R.id.content), message,Snackbar.LENGTH_LONG)
       val snackbarview = snackBar.view
       if (errorMessage)
       {
           snackBar.setBackgroundTint( getResources().getColor(R.color.colorSnackBarError))
       }
       else
       {
           snackBar.setBackgroundTint(getColor(R.color.colorSnackBarSuccess))

       }
       snackBar.show()
   }
    fun showprogressbar()
    {
          mProgressDialog = Dialog(this)
         mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }
    fun hideprogressdialog()
    {
        mProgressDialog.dismiss()
    }

    fun doubleBackToexit()
    {
        if (doublebackpressedtoexit)
        {
            super.onBackPressed()
            return
        }
        doublebackpressedtoexit = true
        ShowErrorSnackbar(getString(R.string.please_click_back_again_to_exit),true)
        Handler().postDelayed({doublebackpressedtoexit =false},2000)
    }
}