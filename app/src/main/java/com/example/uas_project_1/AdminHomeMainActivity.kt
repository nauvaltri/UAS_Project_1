package com.example.uas_project_1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_project_1.databinding.ActivityAdminHomeMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class AdminHomeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var itemAdapter :ItemAdapter
    private lateinit var itemList : ArrayList<ItemData>
    private lateinit var recyclerViewItem : RecyclerView

    private lateinit var auth: FirebaseAuth

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkReceiver: BroadcastReceiver




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.logout.setOnClickListener{
            val sharedPreferences = this.getSharedPreferences("user_data",Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("IsLoggedIn",false).apply()

            auth.signOut()
            startActivity(Intent(this@AdminHomeMainActivity,OpeningActivity::class.java))
        }

        recyclerViewItem = binding.recyclerPiw
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = ItemAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        database = FirebaseDatabase.getInstance().getReference("Admin")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list
                itemList.clear()

                // Iterate through the snapshot and add items to the list
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(ItemData::class.java)
                    if (item != null) {
                        itemList.add(item)
                    }
                }

                // Notify the adapter that the data has changed
                itemAdapter.notifyDataSetChanged()
                Log.d("msg",itemList.size.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
                Toast.makeText(this@AdminHomeMainActivity, "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })
        Firebase.auth.signOut()
        binding.logout.setOnClickListener{
            startActivity(Intent(this@AdminHomeMainActivity,OpeningActivity::class.java))
        }
        with(binding) {
            fabAdd.setOnClickListener {
                val intent = Intent(this@AdminHomeMainActivity, AdminListAddMainActivity::class.java)
                startActivity(intent)
            }
        }


    }
}