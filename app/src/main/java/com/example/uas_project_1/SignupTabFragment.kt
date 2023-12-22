package com.example.uas_project_1

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uas_project_1.databinding.FragmentSignupTabBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupTabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupTabBinding
    private lateinit var sharePreferences : SharedPreferences

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
        binding= FragmentSignupTabBinding.inflate(layoutInflater,container,false)
        auth= Firebase.auth

        sharePreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()

        val username: EditText = binding.signupUsername
        val email:EditText = binding.signupEmail
        val password: EditText = binding.signupPassword
        val registerNow: Button = binding.btnRegister

        registerNow.setOnClickListener {

            if (email.text.toString().isEmpty()){
                Toast.makeText(requireActivity(),"PLEASE FILL THE EMAIl",Toast.LENGTH_SHORT).show()
            }
            if (password.text.toString().isEmpty()){
                Toast.makeText(requireActivity(),"PLEASE FILL THE PASSWORD",Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(requireActivity()) {task ->
                    if(task.isSuccessful){
                        editor.putString("username",username.text.toString())
                        editor.putString("email",email.text.toString())
                        editor.putString("password",password.text.toString())
                        editor.apply()

                        email.text?.clear()
                        password.text?.clear()
                        username.text?.clear()
                        Toast.makeText(requireActivity(),"Register Successfull!",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity(),"Register Failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupTabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}