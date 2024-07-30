package com.example.bottomnavigation.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationfirebase.AddProductActivity
import com.example.myapplicationfirebase.FirestoreWork
import com.example.myapplicationfirebase.GlideLoader
import com.example.myapplicationfirebase.MyProductAdapter
import com.example.myapplicationfirebase.R
import com.example.myapplicationfirebase.SettingActivity
import com.example.myapplicationfirebase.databinding.FragmentProductsBinding
import com.example.myapplicationfirebase.fragments.BaseFragment
import com.example.myapplicationfirebase.models.Product

class ProductsFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

         /*homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id)
        {
            R.id.action_add_product ->
            {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successProductListFromFirestore(productList:ArrayList<Product>)
    {
        hideprogressdialog()
      if (productList.size>0)
      {
         _binding!!.rvDashboardItems.visibility =View.VISIBLE
          _binding?.tvNoDashboardItemsFound!!.visibility = View.GONE
         // _binding!!.rvDashboardItems.LayoutManager =LinearLayoutManager(activity)
          _binding!!.rvDashboardItems.setHasFixedSize(true)
          val adapterproducts = MyProductAdapter(requireActivity(), productList,this)
          _binding!!.rvDashboardItems.adapter = adapterproducts

      }else
      {
          _binding!!.rvDashboardItems.visibility =View.GONE
          _binding?.tvNoDashboardItemsFound!!.visibility = View.VISIBLE


      }
    }
    private fun getProductListFromFirestore()
    {
       showprogressdialog("Please Wait")
        FirestoreWork().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
            getProductListFromFirestore()
    }
    fun deleteProduct (productId:String)
    {
                showalertdialogtodeleteproduct(productId)
    }

 fun productDeleteSuccess()
    {
     hideprogressdialog()
        Toast.makeText(requireActivity(),"Product delete successfully",Toast.LENGTH_SHORT).show()
        getProductListFromFirestore()
 }
    private fun showalertdialogtodeleteproduct(productId:String)
    {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes))
        {
            dialogInterface,_->
            dialogInterface.dismiss()
            showprogressdialog("Please Wait")
            FirestoreWork().deleteproduct(this,productId)
        }
        builder.setNegativeButton(resources.getString(R.string.no))
        {
            dialogInterface,_->
            dialogInterface.dismiss()

        }
        val alertDialog:AlertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCancelable(false)
    }


}