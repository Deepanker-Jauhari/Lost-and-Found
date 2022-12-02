package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lostandfound.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database:DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var profile_name: TextView
    private lateinit var profile_rollno: TextView
    private lateinit var profile_number: TextView
    private lateinit var profile_email: TextView
    private lateinit var profile_img: ImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        supportActionBar?.hide() //hide the title bar
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val userID = auth.currentUser!!.uid
        readData(userID)

        profile_name = findViewById(R.id.user_profile_name)
        profile_rollno = findViewById(R.id.user_rollno)
        profile_number = findViewById(R.id.user_number)
        profile_email = findViewById(R.id.user_email)
        profile_img = findViewById(R.id.profile_img_profile)


        binding.textView4.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnMyLostItems.setOnClickListener {
            val intent = Intent(this, MyLostItems::class.java)
            startActivity(intent)
        }

        binding.btnMyFoundItems.setOnClickListener {
            val intent = Intent(this, MyFoundItems::class.java)
            startActivity(intent)
        }

        binding.btnUpdatePass.setOnClickListener {
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            clearToken(userID)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun readData(userID:String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userID).get().addOnSuccessListener {

            val name = it.child("fullName").value
            val number = it.child("phoneNumber").value
            val image = it.child("profileImageUrl").value
            val rollno = it.child("rollNumber").value
            val email = it.child("email").value
            if (image != null.toString()){
                Glide.with(this)
                    .load(image).into(profile_img)
            }
            profile_name.text = name.toString()
            profile_rollno.text = rollno.toString()
            profile_number.text = number.toString()
            profile_email.text = email.toString()
            if (progressDialog.isShowing) progressDialog.dismiss()
        }
    }

    private fun clearToken(userID: String){
        FirebaseDatabase.getInstance().getReference("tokens").child(userID)
            .removeValue()

    }



}
