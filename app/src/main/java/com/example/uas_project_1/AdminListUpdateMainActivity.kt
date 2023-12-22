package com.example.uas_project_1

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_project_1.databinding.ActivityAdminListUpdateMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AdminListUpdateMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminListUpdateMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private  var imageUri: Uri ?= null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imageViewUpdate.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListUpdateMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewUpdate.setOnClickListener{
            getContent.launch("image/*")
        }

        val title = binding.adminListUpdateTitle
        val author = binding.adminListUpdateAuthor
        val description = binding.adminListUpdateDescription

        val originalImageUrl = intent.getStringExtra("itemImageUrl")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(binding.imageViewUpdate)

        title.setText(intent.getStringExtra("itemTitle").toString())
        author.setText(intent.getStringExtra("itemAuthor").toString())
        description.setText(intent.getStringExtra("itemDesc").toString())

        binding.btnUpdateData.setOnClickListener {
            Log.d("NOPAL","$title, ${imageUri}")
            Log.d("NOPAL","$title, ${imageUri}")
            Log.d("NOPAL","$title, ${imageUri}")
            uploadData(imageUri)
        }

        Firebase.auth.signOut()
        binding.addBack.setOnClickListener{
            startActivity(Intent(this@AdminListUpdateMainActivity,AdminHomeMainActivity::class.java))
        }

    }
    private fun uploadData(imageUri: Uri? = null) {
        val updatedTitle = binding.adminListUpdateTitle.text.toString()
        val updatedAuthor = binding.adminListUpdateAuthor.text.toString()
        val updatedDescription = binding.adminListUpdateDescription.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Admin")



        if (imageUri != null) {
            // Generate a unique ID for the image
            val imageId = Uri.parse(intent.getStringExtra("itemImageUrl")).lastPathSegment?.removePrefix("images/")
            Log.d("msg","ID NYA TUUUU $imageId")

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = ItemData("1",updatedTitle, updatedAuthor, updatedDescription, imageUrl.toString())
                    database.child(imageId!!).setValue(item)
                        .addOnCompleteListener {
                            // Handle completion, e.g., clear input fields and show a success message
                            binding.adminListUpdateTitle.text!!.clear()
                            binding.adminListUpdateAuthor.text!!.clear()
                            binding.adminListUpdateDescription.text!!.clear()
                            startActivity(Intent(this,AdminHomeMainActivity::class.java))
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no new image is selected, update the data without uploading a new image
            val imageId = Uri.parse(intent.getStringExtra("itemImageUrl")).lastPathSegment?.removePrefix("images/")

            val updatedList = mapOf<String, String>(
                "title" to updatedTitle,
                "author" to updatedAuthor,
                "description" to updatedDescription
            )

            // Update the data with the new title
            database.child(imageId!!).updateChildren(updatedList)
                .addOnCompleteListener {
                    // Handle completion, e.g., clear input fields and show a success message
                    binding.adminListUpdateTitle.text!!.clear()
                    binding.adminListUpdateAuthor.text!!.clear()
                    binding.adminListUpdateDescription.text!!.clear()
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }


}