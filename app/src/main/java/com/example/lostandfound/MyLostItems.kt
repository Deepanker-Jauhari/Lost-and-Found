package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfound.databinding.ActivityMyLostItemsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyLostItems : AppCompatActivity() {
    private lateinit var binding: ActivityMyLostItemsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var thingsList: ArrayList<LostItems>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLostItemsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_my_lost_items)


        recyclerView = findViewById(R.id.recyclerview_my_lost_items)
        recyclerView.layoutManager = LinearLayoutManager(this@MyLostItems)

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
        db.collection("user").document(userID).collection("Lost Items").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    db.collection("user").document()
                    val thing: LostItems? = data.toObject(LostItems::class.java)
                    if (thing != null) {
                        thingsList.add(thing)
                    }
                }
                recyclerView.adapter = MyLostItemsAdapter(this, thingsList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}