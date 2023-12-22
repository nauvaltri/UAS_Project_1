    package com.example.uas_project_1

    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.content.Intent
    import android.net.Uri
    import android.os.Build
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.core.app.NotificationCompat
    import com.example.uas_project_1.databinding.ActivityAdminListAddMainBinding
    import com.google.firebase.Firebase
    import com.google.firebase.auth.auth
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.storage.FirebaseStorage
    import com.google.firebase.storage.StorageReference
    import com.google.firebase.storage.UploadTask
    import java.util.UUID

    class AdminListAddMainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityAdminListAddMainBinding
        private lateinit var database: DatabaseReference
        private lateinit var storageReference: StorageReference
        private lateinit var imageUri: Uri


        // Pengelola hasil untuk mendapatkan konten (pilih gambar)
        private val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    binding.imageViewAdd.setImageURI(uri)
                    /// Opsional, Anda dapat memanggil uploadData(imageUri) di sini jika diperlukan
                }
            }



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAdminListAddMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Menetapkan OnClickListener untuk tombol tambah
            with(binding) {
                adminListAddButton.setOnClickListener {
                    uploadData(imageUri)
                }

                // Menetapkan OnClickListener untuk ImageView (pilih gambar)
                adminListAddImage.setOnClickListener {
                    getContent.launch("image/*")
                }
            }

            // Logout pengguna saat ini (opsional, sesuai dengan kebutuhan aplikasi)
            Firebase.auth.signOut()
            binding.addBack.setOnClickListener{
                startActivity(Intent(this@AdminListAddMainActivity,AdminHomeMainActivity::class.java))
            }
        }

        //Function

        // Fungsi untuk mengunggah data ke Firebase
        private fun uploadData(imageUri: Uri? = null) {
            val title: String = binding.adminListAddTitle.text.toString()
            val author: String = binding.adminListAddAuthor.text.toString()
            val description: String = binding.adminListAddDescription.text.toString()

            val imageId = UUID.randomUUID().toString()


            // Memeriksa apakah semua field diisi dan gambar dipilih
            if (title.isNotEmpty() && author.isNotEmpty() && description.isNotEmpty() && imageUri != null) {
                // Generate a unique ID for the image

                // Upload image to Firebase Storage with the generated ID
                storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
                val uploadTask: UploadTask = storageReference.putFile(imageUri)

                uploadTask.addOnSuccessListener {
                    // Image uploaded successfully, now get the download URL
                    storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                        val item = ItemData(id = imageId, title = title, author = author, desc = description,imageurl = imageUrl.toString(),)
                        database = FirebaseDatabase.getInstance().getReference("Admin")
                        database.child(imageId).setValue(item)
                            .addOnCompleteListener {
                                binding.adminListAddTitle.text!!.clear()
                                binding.adminListAddAuthor.text!!.clear()
                                binding.adminListAddDescription.text!!.clear()

    //                            // Menampilkan notifikasi

                                startActivity(Intent(this,AdminHomeMainActivity::class.java))
                                Toast.makeText(this, "Uplod data selesai ", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {

                                // Menampilkan pesan kegagalan jika ada
                                Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
                            }
                    }
                }.addOnFailureListener {

                    // Menampilkan pesan kegagalan pengunggahan gambar
                    Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }


        private fun showNotification(message: String) {
            val channelId = "MyChannelId"
            val notificationId = 1

            createNotificationChannel()

            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("PPAPB UAS Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    //        with(NotificationManagerCompat.from(this)) {
    //            notify(notificationId, builder.build())
    //        }

        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "MyChannel"
                val descriptionText = "My Notification Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("MyChannelId", name, importance).apply {
                    description = descriptionText
                }

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

    }