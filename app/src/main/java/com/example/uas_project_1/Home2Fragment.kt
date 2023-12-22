package com.example.uas_project_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_project_1.Database.Note
import com.example.uas_project_1.Database.NoteDao
import com.example.uas_project_1.Database.NoteRoomDatabase
import com.example.uas_project_1.databinding.FragmentHome2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentHome2Binding
    private lateinit var database : DatabaseReference
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var itemAdapter :AdapterUser
//    private lateinit var itemList : ArrayList<ItemData>

    private lateinit var dao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(layoutInflater)

        recyclerViewItem = binding.recyclerPiwUser
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(requireActivity())

        itemAdapter = AdapterUser(emptyList())
        recyclerViewItem.adapter = itemAdapter



        // Initialize Room database
        dao = NoteRoomDatabase.getDatabase(requireContext()).dao()

        // Inisialisasi referensi Firebase
        database = FirebaseDatabase.getInstance().getReference("Admin")

        // Ambil data dari Firebase dan perbarui itemList
        fetchFilmFromFirebase()


        return binding.root
    }


    private fun fetchFilmFromFirebase() {

        // ValueEventListener untuk mengambil data dari Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filmList = mutableListOf<Note>()

                for (dataSnapshot in snapshot.children) {
                    val filmEntity = dataSnapshot.getValue(Note::class.java)
                    Log.e("eror",dataSnapshot.toString())
                    filmEntity?.let { filmList.add(it) }
                }

                // Perbarui Room database dengan data baru dari Firebase
                GlobalScope.launch(Dispatchers.IO) {
                    dao.deleteAllFilm()
                    dao.insertFilm(filmList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani kesalahan jika diperlukan
            }
        })

        // Amati perubahan di LiveData dari Room dan perbarui adapter
        dao.getAllFilm().observe(viewLifecycleOwner, Observer { films ->
            itemAdapter.updateData(films)
        })




    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}