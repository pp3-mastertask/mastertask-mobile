package com.example.mastertask.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.EditarContaActivity
import com.example.mastertask.MainActivity
import com.example.mastertask.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    private lateinit var lbUsername: TextView
    private lateinit var lbFullname: TextView
    private lateinit var lbAge: TextView
    private lateinit var lbAddress: TextView

    private lateinit var rvHabilidades: RecyclerView

    private lateinit var btnEditarPerfil: Button
    private lateinit var btnSair: Button

    private lateinit var imgPhoto: ImageView

    private lateinit var badgeAdapter: BadgeViewAdapter
    private var list_skills = ArrayList<Map<String?, Any?>>()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initViews(view)
    }

    fun initViews(view: View) {
        this.lbUsername = view.findViewById(R.id.lbUsername)
        this.lbFullname = view.findViewById(R.id.lbFullName)
        this.lbAge = view.findViewById(R.id.lbAge)
        this.lbAddress = view.findViewById(R.id.lbAddress)

        this.btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)
        this.btnSair = view.findViewById(R.id.btnSair)

        this.imgPhoto = view.findViewById(R.id.imgPhoto)

        this.rvHabilidades = view.findViewById(R.id.rvHabilidades)

        this.addValues()
        this.addEventListeners()
    }

    fun addValues() {
        val currentUser = this.auth.currentUser!!
        val userEmail = currentUser.email.toString()

        var currentUserData: Map<String, Any>? = null

        this.db.collection("usuarios")
            .document(userEmail)
            .get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    currentUserData = it.data
                    if (currentUserData != null) {
                        for ((k, v) in currentUserData!!) {
                            when (k) {
                                "nome" -> {
                                    this.lbUsername.setText(v.toString())
                                    this.lbFullname.setText(v.toString())
                                }
                                "dataNascimento" -> {
                                    this.lbAge.text = SimpleDateFormat("dd/MM/yyyy")
                                        .format((v as Timestamp).toDate())
                                }
                                "endereco" -> this.lbAddress.setText(v.toString())
                                "habilidades" -> {
                                    val skillsHashMapList = v as ArrayList<Any>

                                    for (hashMap in skillsHashMapList) {
                                        this.list_skills.add(hashMap as Map<String?, Any?>)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        this.badgeAdapter = BadgeViewAdapter(this.list_skills)
        this.rvHabilidades.adapter = this.badgeAdapter
        this.badgeAdapter.notifyDataSetChanged()
    }

    fun addEventListeners() {
        this.btnEditarPerfil.setOnClickListener {
            val intent = Intent(activity, EditarContaActivity::class.java)
            startActivity(intent)
        }

        this.btnSair.setOnClickListener {
            auth.signOut()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()
                .addOnCompleteListener {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserFragment().apply {}
    }
}

