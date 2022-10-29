package com.example.lostandfound

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.lostandfound.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var profileUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.profileImg.setOnClickListener{
            selectImage()
        }

        binding.button.setOnClickListener{
            val name = binding.nameET.text.toString()
            val roll = binding.rollET.text.toString()
            val email = binding.emailET.text.toString()
            val email2 = email.replace('.',',')
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val whatsappNum = binding.whatsappET.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(name, roll, email2, whatsappNum)
            database.child(email2).setValue(User).addOnSuccessListener {
                binding.nameET.text?.clear()
                binding.rollET.text?.clear()
                binding.emailET.text?.clear()
                binding.whatsappET.text?.clear()
            }

            uploadImage()
//            email?.replace(',','.')

            if (name.isNotEmpty() && roll.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && whatsappNum.isNotEmpty()){
                if (email.endsWith("@iitp.ac.in")){
                    if(pass == confirmPass) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                            if(it.isSuccessful){
                                sendEmailVerification()
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Enter IITP Email Address only", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val storage = FirebaseStorage.getInstance().getReference("images/$userID/profilePic")
        storage.putFile(profileUri)
            .addOnSuccessListener {
                binding.profileImg.setImageURI(null)
//                Toast.makeText(this@SignUpActivity, "Image uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(this@SignUpActivity, "Failed", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            profileUri = data?.data!!
            binding.profileImg.setImageURI(profileUri)
        }
    }
    private fun sendEmailVerification() {
        //get instance of firebase auth
        val firebaseAuth = FirebaseAuth.getInstance()
        //get current user
        val firebaseUser = firebaseAuth.currentUser

        //send email verification
        firebaseUser!!.sendEmailVerification()
            .addOnSuccessListener {
                Toast.makeText(this@SignUpActivity, "Verification Link sent to Email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@SignUpActivity, "Failed to send due to " + e.message, Toast.LENGTH_SHORT).show()
            }

    }
}