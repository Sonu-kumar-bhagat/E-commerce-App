package com.example.myapplicationfirebase.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.myapplicationfirebase.R

class MSPButtonText(context: Context, attrs:AttributeSet):AppCompatButton(context,attrs) {
    init
    {
        ApplyFont()
    }
    private  fun ApplyFont()
    {
        val boldtypeface : Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        typeface = boldtypeface
        setBackgroundResource(R.drawable.button_background)
      }
}