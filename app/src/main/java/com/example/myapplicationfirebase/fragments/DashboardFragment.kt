package com.example.bottomnavigation.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplicationfirebase.CartListActivity
import com.example.myapplicationfirebase.Constants
import com.example.myapplicationfirebase.DashboardItemListAdapter
import com.example.myapplicationfirebase.FirestoreWork
import com.example.myapplicationfirebase.ProductDetailsActivity
import com.example.myapplicationfirebase.R
import com.example.myapplicationfirebase.SettingActivity
import com.example.myapplicationfirebase.databinding.FragmentDashboardBinding
import com.example.myapplicationfirebase.fragments.BaseFragment
import com.example.myapplicationfirebase.models.Product

class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

         /*dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id)
        {
              R.id.action_setting ->
              {
                  startActivity(Intent(activity, SettingActivity::class.java))
                  return true
              }
            R.id.action_cart ->
            {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList:ArrayList<Product>)
    {
        hideprogressdialog()
        for (i in dashboardItemsList)
        {
            if (dashboardItemsList.size > 0)
           {
                _binding!!.rvDashboardItems.visibility = View.VISIBLE
                _binding!!.textDashboard.visibility = View.GONE
                val adapter = DashboardItemListAdapter(requireActivity(), dashboardItemsList)

                _binding!!.rvDashboardItems.adapter = adapter
               _binding!!.rvDashboardItems.layoutManager = GridLayoutManager(activity,2)
                _binding!!.rvDashboardItems.setHasFixedSize(true)

                   adapter.setOnClickListener(object:DashboardItemListAdapter.OnClickListener
                   {
                     override  fun onClick(position:Int, product:Product)
                     {
                         val intent  = Intent (context, ProductDetailsActivity::class.java)
                         intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
                         intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,product.user_id)
                         startActivity(intent)
                     }
                   })

            }
            else{
                _binding!!.rvDashboardItems.visibility = View.GONE
                _binding!!.textDashboard.visibility = View.VISIBLE

            }
         }

    }
    fun getdashboarditemlist()
    {
        showprogressdialog("Please Wait")
        FirestoreWork().getDashboardItemList(this@DashboardFragment)
    }

    override fun onResume() {
        super.onResume()
        getdashboarditemlist()

    }
}