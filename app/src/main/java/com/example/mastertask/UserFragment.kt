package com.example.mastertask

import BadgeAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    private lateinit var imgPhoto: ImageView

    private lateinit var badgeAdapter: BadgeAdapter
    private var list_skills = ArrayList<SkillModel>()

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

        this.imgPhoto = view.findViewById(R.id.imgPhoto)

        this.addValues()
        this.addEventListeners()
    }

    fun addValues() {

        val currentUser = this.auth.currentUser!!
        val userEmail = currentUser.email.toString()

        var currentUserData: Map<String, Any>? = null

        this.badgeAdapter = BadgeAdapter(this.list_skills)
        this.rvHabilidades.adapter = this.badgeAdapter
        this.rvHabilidades.layoutManager = GridLayoutManager(activity?.applicationContext, 3, RecyclerView.VERTICAL, false)

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
                                    this.lbAge.setText(v.toString())
                                }
                                "endereco" -> this.lbAddress.setText(v.toString())
                                "habilidades" -> {
                                    val skillsHashMapList = v as ArrayList<Any>

                                    for (hashMap in skillsHashMapList) {
                                        if (hashMap is HashMap<*, *>) {
                                            this.list_skills.add(
                                                SkillModel(
                                                    hashMap["habilidade"]?.toString() ?: "",
                                                    hashMap["preco"]?.toString()?.toDouble() ?: 0.0
                                                )
                                            )

                                            this.badgeAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

    fun addEventListeners() {
        this.btnEditarPerfil.setOnClickListener {
            //val intent = Intent(this, EditarContaActivity::class.java)
            //startActivity(intent)
            //this.finish()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            UserFragment().apply {

            }
    }
}

