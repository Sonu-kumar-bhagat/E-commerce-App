package com.example.myapplicationfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationfirebase.models.Address
import com.example.myapplicationfirebase.utils.MSPButtonText
import com.example.myapplicationfirebase.utils.MSPEditText
import com.example.myapplicationfirebase.utils.MSPRadioButton
import com.google.android.material.textfield.TextInputLayout

class Add_edit_Address : BaseActivity() {

    lateinit var actionbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setactionbar()
        var btn_submit = findViewById<MSPButtonText>(R.id.btn_submit_address)
        btn_submit.setOnClickListener()
        {
            saveAddressToFirestore()
        }
        var rg_type = findViewById<RadioGroup>(R.id.rg_type)
        var til_other_details = findViewById<TextInputLayout>(R.id.til_other_details)
        rg_type.setOnCheckedChangeListener{
                _, checkedId ->
            if (checkedId == R.id.rb_other)
            {
                til_other_details.visibility = View.VISIBLE
            }
            else
            {
                til_other_details.visibility = View.GONE

            }
        }
    }
    private fun setactionbar () {
        actionbar = findViewById(R.id.toolbar_add_edit_address_activity)

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

    private fun validateData(): Boolean {
        var et_full_name = findViewById<MSPEditText>(R.id.et_full_name)
        var et_phone_number= findViewById<MSPEditText>(R.id.et_phone_number)
        var et_address = findViewById<MSPEditText>(R.id.et_address)
        var et_zip_code = findViewById<MSPEditText>(R.id.et_zip_code)
        var rb_other = findViewById<MSPRadioButton>(R.id.rb_other)
        return when {
            TextUtils.isEmpty(et_full_name.text.toString().trim { it <= ' ' }) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim { it <= ' ' }) -> {
                ShowErrorSnackbar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim { it <= ' ' }) -> {
                ShowErrorSnackbar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim { it <= ' ' }) -> {
                ShowErrorSnackbar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            rb_other.isChecked && TextUtils.isEmpty(
                et_zip_code.text.toString().trim { it <= ' ' }) -> {
                ShowErrorSnackbar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun saveAddressToFirestore() {
        var et_full_name = findViewById<MSPEditText>(R.id.et_full_name)
        var et_phone_number= findViewById<MSPEditText>(R.id.et_phone_number)
        var et_address = findViewById<MSPEditText>(R.id.et_address)
        var et_zip_code = findViewById<MSPEditText>(R.id.et_zip_code)
        var rb_other = findViewById<MSPRadioButton>(R.id.rb_other)
        var rb_home = findViewById<MSPRadioButton>(R.id.rb_home)
        var rb_office = findViewById<MSPRadioButton>(R.id.rb_office)

        var et_additional_note = findViewById<MSPEditText>(R.id.et_additional_note)
        var et_other_details = findViewById<MSPEditText>(R.id.et_other_details)
        // Here we get the text from editText and trim the space
        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = et_additional_note.text.toString().trim { it <= ' ' }
        val otherDetails: String = et_other_details.text.toString().trim { it <= ' ' }

        if (validateData()) {

            // Show the progress dialog.
                showprogressbar()
            val addressType: String = when {
                rb_home.isChecked -> {
                    Constants.HOME
                }
                rb_office.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                FirestoreWork().getCurrentUserId(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )

            FirestoreWork().addAddress(this, addressModel)

        }
    }

  fun  addUpdateAddressSuccess()
  {
      hideprogressdialog()
      Toast.makeText(this,"Address Added Successfully",Toast.LENGTH_SHORT).show()
      setResult(RESULT_OK)
      finish()
  }




}