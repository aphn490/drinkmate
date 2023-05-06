package com.example.drinkmate

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class ShoppingView : Fragment() {

    private var shopping: HashMap<String, String>? = HashMap()
    private var arr: HashMap<String, HashMap<String, *>?>? = null
    private var cartNumber = ""
    private val db = Firebase.firestore
    private val uid = FirebaseAuth.getInstance().uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        setFragmentResultListener("cartKey") { key, bundle ->
            uiScope.launch(Dispatchers.IO) {
                for (x in bundle.keySet()) {
                    cartNumber = x.toString()
                }
                arr = bundle.get(cartNumber) as HashMap<String, HashMap<String, *>?>?
                withContext(Dispatchers.Main) {
                    for (x in arr?.keys!!) {
                        shopping?.set(x, arr!![x].toString())
                    }
                    view?.findViewById<TextView>(R.id.viewCartName)?.text = shopping?.get("name").toString()
                    shopping?.remove("name")
                    shopping?.forEach { entry ->
                        val cb = CheckBox(requireContext())
                        cb.text = entry.value + " " + entry.key.toString() + "\n"
                        cb.textSize = 28F
                        cb.gravity = Gravity.CENTER
                        view?.findViewById<LinearLayout>(R.id.cartViewLayout)?.addView(cb)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val del = view.findViewById<Button>(R.id.viewCartDelete)
        val fin = view.findViewById<Button>(R.id.viewCartFinish)
        del.setOnClickListener {
            val docRef = db.collection("ShoppingCarts").document(uid)
            val updates = hashMapOf<String, Any>(
                cartNumber to FieldValue.delete()
            )
            docRef.update(updates)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
        fin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}