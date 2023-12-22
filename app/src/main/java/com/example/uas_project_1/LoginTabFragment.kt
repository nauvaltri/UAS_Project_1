package com.example.uas_project_1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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

        val email: EditText = binding.loginEmail
        val password: EditText = binding.loginPassword
        val loginBtn: Button = binding.btnLogin

        // Check if user credentials are saved in SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isLoggendIn = sharedPreferences.getBoolean("isLoggedIn", false)
//        val savedEmail = sharedPreferences.getString("email", null)
//        val savedPassword = sharedPreferences.getString("password", null)
        val editor = sharedPreferences.edit()

        if (isLoggendIn) {
            val userType = sharedPreferences.getString("userType", "guest")
            navigateToMainMenu(userType)
        } else {


            loginBtn.setOnClickListener {
                if (email.text.toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT)
                        .show()
                }
                if (password.text.toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT)
                        .show()
                }

                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(requireActivity()) { taskk ->
                        val currentUser = auth.currentUser
                        if (taskk.isSuccessful) {
                            if (currentUser != null) {
                                // Save user credentials in SharedPreferences
                                // Check if the user is an admin based on their email address
                                val userType = if (currentUser.email == "admin@gmail.com") {
                                    "admin"
                                }
                                else{
                                    "user"}
                                    editor.putBoolean("isLoggedIn", true)
                                    editor.putString("userType", userType)
                                    editor.apply()

                                    navigateToMainMenu(userType)

                                    // User is an admin, redirect to Admin activity
//                                    editor.putBoolean("isAdminLoggedIn", true)
//                                    editor.putString("email", email.text.toString())
//                                    editor.putString("password", password.text.toString())
//                                    editor.apply()
//                                    startActivity(
//                                        Intent(
//                                            requireActivity(),
//                                            AdminHomeMainActivity::class.java
//                                        )
//                                    )
                                }
                        } else {

                            Toast.makeText(requireActivity(),"Login Failed",Toast.LENGTH_SHORT).show()
//                                    // User is not an admin, redirect to User activity
//                                    editor.putBoolean("isUserLoggedIn", true)
//                                    editor.putString("email", email.text.toString())
//                                    editor.putString("password", password.text.toString())
//                                    editor.apply()
//                                    startActivity(
//                                        Intent(
//                                            requireActivity(),
//                                            MainActivity::class.java
//                                        )
//                                    )
                                }
                    }
            }
        }

        return binding.root
    }

    private fun navigateToMainMenu(userType:String?){
        val intentTo =when (userType){
            "user" -> Intent (requireActivity(),MainActivity::class.java)
            "admin" -> Intent(requireActivity(),AdminHomeMainActivity::class.java)
            else -> null
        }
        startActivity(intentTo!!)
    }
}