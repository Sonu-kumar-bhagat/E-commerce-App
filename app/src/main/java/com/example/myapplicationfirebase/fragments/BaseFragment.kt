package com.example.myapplicationfirebase.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplicationfirebase.R


open class BaseFragment : Fragment() {
    private lateinit var mprogressbar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    fun showprogressdialog(text:String)
    {
        mprogressbar= Dialog(requireActivity())
        mprogressbar.setContentView(R.layout.dialog_progress)
       val textm   = mprogressbar.findViewById<TextView>(R.id.tv_progress_text)
        textm.text = text
        mprogressbar.setCancelable(false)
        mprogressbar.setCanceledOnTouchOutside(false)
        mprogressbar.show()
     }
    fun hideprogressdialog()
    {
        mprogressbar.dismiss()
    }
 }