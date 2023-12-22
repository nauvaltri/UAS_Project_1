//package com.example.uas_project_1
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.uas_project_1.databinding.ActivityAddItemBinding
//
//class addItemActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityAddItemBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAddItemBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.addButton.setOnClickListener {
//            val title = binding.addTitle.text.toString()
//            val desc = binding.addDesc.text.toString()
//            val date = binding.addDate.text.toString()
//
//            insert(Note(title = title, description = desc, date = date))
//            finish()
//        }
//    }
//
//    private fun insert(note: Note) {
//        MainActivity.executorService.execute { MainActivity.mNotesDao.insert(note) }
//    }
//}