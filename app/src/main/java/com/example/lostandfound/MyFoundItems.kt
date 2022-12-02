package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfound.databinding.ActivityFeedLostItemBinding
import com.example.lostandfound.databinding.ActivityMyFoundItemsBinding
import com.example.lostandfound.databinding.ActivityMyLostItemsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyFoundItems : AppCompatActivity() {

    private lateinit var binding: ActivityMyFoundItemsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var thingsList: ArrayList<FoundItems>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFoundItemsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_my_found_items)

        recyclerView = findViewById(R.id.recyclerview_my_found_items)
        recyclerView.layoutManager = LinearLayoutManager(this@MyFoundItems)

        thingsList = arrayListOf()

        binding.textView4.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fetchData()
    }

    private fun fetchData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        db.collection("user").document(userID).collection("Found Items").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val things: FoundItems? = data.toObject(FoundItems::class.java)
                        if (things != null) {
                            thingsList.add(things)
                        }
                        recyclerView.adapter = MyFoundItemsAdapter(this, thingsList)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}