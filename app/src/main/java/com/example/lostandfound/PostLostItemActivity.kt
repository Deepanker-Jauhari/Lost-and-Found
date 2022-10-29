package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.lostandfound.databinding.ActivityPostLostItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PostLostItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostLostItemBinding
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostLostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email2 = intent.getStringExtra("path_key")

        database = FirebaseDatabase.getInstance().getReference("Users")
        if (email2 != null) {
            database.child(email2).get().addOnSuccessListener {
                val fullName = it.child("fullName").value
                val rollNumber = it.child("rollNumber").value
                val phoneNumber = it.child("phoneNumber").value

                val nameTextView: TextView = findViewById(R.id.nameET)
                nameTextView.text = fullName as CharSequence?

                val numberTextView: TextView = findViewById(R.id.whatsappET)
                numberTextView.text = phoneNumber as CharSequence?

            }
        }

        binding.textView2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener{
            //
            //
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}