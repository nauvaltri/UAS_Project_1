    package com.example.uas_project_1

    import android.content.Context
    import android.content.Intent
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import com.example.uas_project_1.databinding.ActivityMainBinding
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.example.uas_project_1.databinding.FragmentLoginTabBinding
    import com.google.android.material.textfield.TextInputEditText
    import com.google.firebase.auth.FirebaseUser
    import com.google.firebase.auth.auth
    import com.google.firebase.firestore.FirebaseFirestore

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"

    /**
     * A simple [Fragment] subclass.
     * Use the [LoginTabFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class LoginTabFragment : Fragment() {
        // TODO: Rename and change types of parameters
        private var param1: String? = null
        private var param2: String? = null
        private lateinit var binding:FragmentLoginTabBinding
        private lateinit var auth: FirebaseAuth
        private lateinit var firebase: FirebaseFirestore
        private lateinit var sharedPreferences: SharedPreferences

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
            binding = FragmentLoginTabBinding.inflate(layoutInflater, container, false)
            auth = Firebase.auth
            firebase = FirebaseFirestore.getInstance()


            // Mendapatkan referensi ke elemen-elemen antarmuka pengguna
            val email: EditText = binding.loginEmail
            val password: EditText = binding.loginPassword
            val loginBtn: Button = binding.btnLogin

            // Mengecek apakah kredensial pengguna sudah tersimpan di SharedPreferences
            val sharedPreferences =
                requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val isLoggedin = sharedPreferences.getBoolean("isLoggedIn", false)
    //        val savedEmail = sharedPreferences.getString("email", null)
    //        val savedPassword = sharedPreferences.getString("password", null)
            val editor = sharedPreferences.edit()


            // Jika pengguna sudah login sebelumnya, langsung navigasi ke menu utama
            if (isLoggedin) {
                val userType = sharedPreferences.getString("userType", "guest")
                navigateToMainMenu(userType)
            } else {

                // Set onClickListener untuk tombol login
                loginBtn.setOnClickListener {
                    // Mengecek apakah kolom email dan password tidak kosong
                    if (email.text.toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (password.text.toString().isEmpty()) {
                        Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT)
                            .show()
                    }


                    // Melakukan otentikasi Firebase menggunakan email dan password
                    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(requireActivity()) { taskk ->
                            val currentUser = auth.currentUser
                            if (taskk.isSuccessful) {
                                if (currentUser != null) {
                                    // Menyimpan kredensial pengguna di SharedPreferences
                                    // Menentukan tipe pengguna berdasarkan alamat email
                                    val userType = if (currentUser.email == "admin@gmail.com") {
                                        "admin"
                                    }
                                    else{
                                        "user"}
                                        editor.putBoolean("isLoggedIn", true)
                                        editor.putString("userType", userType)
                                        editor.apply()


                                    // Navigasi ke menu utama berdasarkan tipe pengguna
                                        navigateToMainMenu(userType)

    //
                                    }
                            } else {

                                // Menampilkan pesan jika login gagal

                                Toast.makeText(requireActivity(),"Login Failed",Toast.LENGTH_SHORT).show()
                                    }
                        }
                }
            }
            // Mengembalikan tampilan dari fragment
            return binding.root
        }
        //Fungsi untuk menavigasi ke menu utama berdasarkan tipe pengguna.
        private fun navigateToMainMenu(userType:String?){
            val intentTo =when (userType){
                "user" -> Intent (requireActivity(),MainActivity::class.java)
                "admin" -> Intent(requireActivity(),AdminHomeMainActivity::class.java)
                else -> null
            }
            startActivity(intentTo!!)
        }
    }