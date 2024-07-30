package com.example.bottomnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationfirebase.FirestoreWork
import com.example.myapplicationfirebase.MyOrderListAdapter
import com.example.myapplicationfirebase.databinding.FragmentOrdersBinding
import com.example.myapplicationfirebase.fragments.BaseFragment
import com.example.myapplicationfirebase.models.Orders


class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textNotifications
        /*notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun populateOrdersListInUI(ordersList: ArrayList<Orders>) {

        // Hide the progress dialog.
         hideprogressdialog()

        // TODO Step 11: Populate the orders list in the UI.
        // STAR
        if (ordersList.size > 0) {
             _binding!!.rvMyOrderItems.visibility = View.VISIBLE
            _binding!!.tvNoOrdersFound.visibility = View.GONE

            _binding!!.rvMyOrderItems.layoutManager = LinearLayoutManager(activity)
            _binding!!.rvMyOrderItems.setHasFixedSize(true)

            val myOrdersAdapter = MyOrderListAdapter(requireActivity(), ordersList)
           _binding!!.rvMyOrderItems.adapter = myOrdersAdapter
        } else {
            _binding!!.rvMyOrderItems.visibility = View.GONE
            _binding!!.tvNoOrdersFound.visibility = View.VISIBLE
        }
        // END
    }
    private fun getMyOrdersList() {
        // Show the progress dialog.
              showprogressdialog("Please Wait")
        FirestoreWork().getMyOrdersList(this@OrdersFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
}