package com.example.myapplicationfirebase.utils

import android.content.Context
import android.graphics.Typeface
 import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView



   class BoldTextView(context: Context, attrset :AttributeSet ): AppCompatTextView(context,attrset) {

    init {
        applyFont()
    }

    private fun applyFont()
    {
      val boldTypeFace :Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface = boldTypeFace

     }
}