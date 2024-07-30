package com.example.myapplicationfirebase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.bottomnavigation.fragments.DashboardFragment
import com.example.bottomnavigation.fragments.OrdersFragment
import com.example.bottomnavigation.fragments.ProductsFragment
import com.example.myapplicationfirebase.models.Address
import com.example.myapplicationfirebase.models.CartItem
import com.example.myapplicationfirebase.models.Orders
import com.example.myapplicationfirebase.models.Product
import com.example.myapplicationfirebase.models.User
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.currentCoroutineContext

class FirestoreWork {

    private val mfirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mfirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.ShowErrorSnackbar(
                    "Your Details are valid Registration Successfull",
                    false
                )
                activity.hideprogressdialog()
            }
            .addOnFailureListener { e ->
                activity.hideprogressdialog()

            }
    }

    fun getCurrentUserId(): String {
        val currentuser = FirebaseAuth.getInstance().currentUser
        var currentuserId = ""
        if (currentuser != null) {
            currentuserId = currentuser.uid
        }
        return currentuserId

    }

    fun getUserDetails(activity: Activity) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject<User>(User::class.java)!!
                val sharedPrefrences = activity.getSharedPreferences(
                    Constants.MYSHOPPAL,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPrefrences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstname} ${user.lastname}")

                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingActivity ->
                    {
                        activity.userdetailssuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideprogressdialog()
                     }
                    is SettingActivity ->
                    {
                        activity.hideprogressdialog()
                    }
                }
            }
    }

    fun updateuserprofiledata(activity: Activity, userHashMap: HashMap<String, Any>) {
        mfirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {

                when (activity) {
                    is UserProfileActivity -> {
                        activity.onsuccessupdate()
                    }

                }


            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideprogressdialog()
                    }

                }
                Log.e(activity.javaClass.simpleName, "Error While updating the user details", e)


            }
    }

    fun storeimagetocloudstorage(activity: Activity, imageFileuri: Uri?,imagetype:String) {
        val sref: StorageReference = FirebaseStorage.getInstance().reference.child(
             imagetype + System.currentTimeMillis() + '.' +
                    Constants.getfileextension(activity, imageFileuri!!)
        )
        sref.putFile(imageFileuri!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image Url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                uri->
                Log.e("Downloadable image Url",uri.toString())
                 when(activity)
                 {
                     is UserProfileActivity ->
                     {
                         activity.imageUploadSuccess(uri)

                     }
                     is AddProductActivity->
                     {
                         activity.imageUploadSuccess(uri)
                     }
                 }


            }

        }
            .addOnFailureListener { exception->
                when(activity)
                {
                    is UserProfileActivity ->
                    {
                        activity.hideprogressdialog()

                    }
                    is AddProductActivity ->
                    {
                        activity.hideprogressdialog()

                    }
                }
                Log.e(activity.javaClass.simpleName,exception.message, exception)

            }
    }
    fun uploadproductdetails(activity:AddProductActivity, productInfo: Product) {
        mfirestore.collection("products")
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

               activity.productuploadsuccess()
             }
            .addOnFailureListener { e ->
                activity.hideprogressdialog()
                activity.productuploadfailed()

            }
    }


     fun getProductsList(fragment: Fragment)
         {
         mfirestore.collection(Constants.PRODUCT)
             .whereEqualTo(Constants.USER_ID,getCurrentUserId())
             .get()
             .addOnSuccessListener { document->
               val productsList:ArrayList<Product> = ArrayList()
                 for(i in document.documents)
                 {
                     var product = i.toObject(Product::class.java)
                    product!!.product_id =i.id
                     productsList.add(product!!)
                 }

                 when(fragment)
                 {
                     is ProductsFragment ->
                     {
                              fragment.successProductListFromFirestore(productsList)
                     }
                 }
             }

                  }

    fun getDashboardItemList(fragment: DashboardFragment)
    {
        mfirestore.collection(Constants.PRODUCT)
            .get()
            .addOnSuccessListener { document->
                 val productsList:ArrayList<Product> = ArrayList()
                for(i in document.documents)
                {
                    var product = i.toObject(Product::class.java)
                    product!!.product_id =i.id
                    productsList.add(product!!)
                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener {
                e->
                fragment.hideprogressdialog()
            }
    }
    fun deleteproduct(fragment:ProductsFragment, productId:String)
    {
        mfirestore.collection(Constants.PRODUCT)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener {
                fragment.hideprogressdialog()
            }
    }

    fun getproductdetails(activity:ProductDetailsActivity, productid:String)
    {
        mfirestore.collection(Constants.PRODUCT)
            .document(productid)
            .get()
            .addOnSuccessListener {
                document->
                val product = document.toObject(Product::class.java)
                activity.ProductDetailsSuccess(product!!)

            }
            .addOnFailureListener {
                e->
                activity.hideprogressdialog()
            }
    }

    fun addtoCartItems(activity:ProductDetailsActivity, addToCart: CartItem)
    {
        mfirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSucess()
            }
            .addOnFailureListener {
                activity.hideprogressdialog()
            }
    }

    fun checkIfItemexitIncart(activity:ProductDetailsActivity, productId:String)
    {
        mfirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserId())
            .whereEqualTo(Constants.PRODUCT_ID,productId)
            .get()
                .addOnSuccessListener { document->
                      if(document.documents.size>0)
                      {
                          activity.productExitIncart()
                      } else
                      {
                          activity.hideprogressdialog()
                      }
            }
            .addOnFailureListener {
                activity.hideprogressdialog()
            }
    }

         fun getcartlist(activity:Activity)

         {
             mfirestore.collection(Constants.CART_ITEMS)
                 .whereEqualTo(Constants.USER_ID,getCurrentUserId())
                 .get()
                 .addOnSuccessListener { document->
                     val list:ArrayList<CartItem> = ArrayList()
                     for(i in document.documents)
                     {
                         val cartItem = i.toObject(CartItem::class.java)
                         cartItem!!.id= i.id
                         list.add(cartItem!!)
                     }
                     when (activity)
                     {
                         is CartListActivity ->
                         {
                             activity.successcartitemlist(list)
                         }

                         is CheckoutActivity->
                         {
                             activity.successCartItemsList(list)

                         }
                     }
                 }
                 .addOnFailureListener {
                     when (activity)
                     {
                         is CartListActivity ->
                         {
                             activity.hideprogressdialog()
                          }
                         is CheckoutActivity ->
                         {
                             activity.hideprogressdialog()
                         }
                     }
                 }
         }

    fun getAllProductsList(activity: Activity) {
        // The collection name for PRODUCTS
        mfirestore.collection(Constants.PRODUCT)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                 // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                   when (activity) {
                  is   CartListActivity ->
                  {activity.successproductlistfromfirestor(productsList)}

                       is   CheckoutActivity ->
                       {activity.successproductlistfromfirestor(productsList)}
                   }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {
                    is   CartListActivity ->{ activity.hideprogressdialog()}

                    is CheckoutActivity ->{activity.hideprogressdialog()}
                 }
                Log.e("Get Product List", "Error while getting all product list.", e)
            }
    }
    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {

        // Cart items collection name
        mfirestore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .update(itemHashMap) // A HashMap of fields which are to be updated.
            .addOnSuccessListener {

                // TODO Step 4: Notify the success result of the updated cart items list to the base class.
                // START
                // Notify the success result of the updated cart items list to the base class.
                when (context) {
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is CartListActivity -> {
                        context.hideprogressdialog()
                    }
                }

                Log.e(
                    context.javaClass.simpleName,
                    "Error while updating the cart item.",
                    e
                )
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {

        // Cart items collection name
        mfirestore.collection(Constants.CART_ITEMS)
            .document(cart_id) // cart id
            .delete()
            .addOnSuccessListener {

                // Notify the success result of the removed cart item from the list to the base class.
                when (context) {
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (context) {
                    is CartListActivity -> {
                        context.hideprogressdialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
                )
            }
    }

    fun addAddress(activity: Add_edit_Address, addressInfo: Address) {

        // Collection name address.
        mfirestore.collection(Constants.ADDRESSES)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideprogressdialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while adding the address.",
                    e
                )
            }
    }

    fun getAddressesList(activity: address_list) {
        // The collection name for PRODUCTS
        mfirestore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                // Here we get the list of boards in the form of documents.
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                // Here we have created a new instance for address ArrayList.
                val addressList: ArrayList<Address> = ArrayList()

                // A for loop as per the list of documents to convert them into Boards ArrayList.
                for (i in document.documents) {

                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id

                    addressList.add(address)
                }

                activity.successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.

                activity.hideprogressdialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the address list.", e)
            }
    }

    fun placeOrder(activity: CheckoutActivity, order: Orders) {

        mfirestore.collection(Constants.ORDERS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(order, SetOptions.merge())
            .addOnSuccessListener {

                // TODO Step 9: Notify the success result.
                // START
                // Here call a function of base activity for transferring the result to it.
                activity.orderPlacedSuccess()
                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                activity.hideprogressdialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }
    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>) {

        val writeBatch = mfirestore.batch()

        // Prepare the sold product details
        for (cart in cartList) {


         }

        // Here we will update the product stock in the products collection based to cart quantity.
        for (cart in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.STOCK_QUANTITY] =
                (cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()

            val documentReference = mfirestore.collection(Constants.PRODUCT)
                .document(cart.product_id)

            writeBatch.update(documentReference, productHashMap)
        }

        // Delete the list of cart items
        for (cart in cartList) {

            val documentReference = mfirestore.collection(Constants.CART_ITEMS)
                .document(cart.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {

            activity.allDetailsUpdatedSuccessfully()

        }.addOnFailureListener { e ->
            // Here call a function of base activity for transferring the result to it.
            activity.hideprogressdialog()

            Log.e(
                activity.javaClass.simpleName,
                "Error while updating all the details after order placed.",
                e
            )
        }
    }
    fun getMyOrdersList(fragment: OrdersFragment) {
        mfirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Orders> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Orders::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                // TODO Step 7: Notify the success result to base class.
                // START
                fragment.populateOrdersListInUI(list)
                // END
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.

                fragment.hideprogressdialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)
            }
    }

}