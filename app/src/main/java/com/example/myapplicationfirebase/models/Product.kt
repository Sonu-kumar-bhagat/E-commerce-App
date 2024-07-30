package com.example.myapplicationfirebase.models
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
     data class Product (
        val user_id:String = "",
    val user_name :String = "",
    val title:String = "",
    val price:String = "",
    val description:String = "",
    var stack_quantity:String = "",
    val image:String = "",
    var product_id:String = ""


):Parcelable