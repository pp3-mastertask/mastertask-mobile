package com.example.mastertask

import HabilidadeViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapter
import com.example.mastertask.Data.Habilidade
import com.example.mastertask.Data.User
import com.example.mastertask.Models.UserViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val QUERY = "query"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeSearch.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeSearch : Fragment() {
    private var query: String? = null

    private var db = Firebase.firestore

    lateinit var recycler_view_results : RecyclerView

    val userViewModel : UserViewModel by viewModels()
    val habilidadeViewModel : HabilidadeViewModel by viewModels()

    var usersArrayList : ArrayList<User> = ArrayList()
    var habilidadesArrayList : ArrayList<Habilidade> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            query = it.getString(QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModels()
        initViews(view)
    }

    fun initViews(view : View) {
        recycler_view_results = view.findViewById(R.id.recycler_view_results) as RecyclerView

        userViewModel.getList()
    }

    fun initModels() {
        userViewModel.createLiveData.observe(viewLifecycleOwner) {
            if (it) {
                userViewModel.getList()
            }
        }

        userViewModel.updateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                userViewModel.getList()
            }
        }

        userViewModel.deleteLiveData.observe(viewLifecycleOwner) {
            if (it) {
                userViewModel.getList()
            }
        }

        userViewModel.getListLiveData.observe(viewLifecycleOwner) {
            usersArrayList = ArrayList()
            usersArrayList.addAll(it)

            setUpRecyclerViews()
        }

        habilidadeViewModel.getItemLiveData.observe(viewLifecycleOwner) {
            habilidadesArrayList.add(it)
        }
    }

    fun setUpRecyclerViews() {
        val lista : MutableList<User> = mutableListOf()

        var la : List<User> = usersArrayList.filter {
            it.nome!!.contains(query!!, true)
        }
        lista.addAll(la)

        la = usersArrayList.filter {
            it.endereco!!.contains(query!!, true)
        }
        lista.addAll(la)

        getSkillsFromQuery(lista)

        lista.sortByDescending { it.somaAvaliacoes!!/it.numServicosFeitos!! }

        lista.take(10)
        if (lista.size != 0)
            setUpRecyclerView(recycler_view_results, lista)
    }

    fun getSkillsFromQuery(lista : MutableList<User>) {
        val skills : MutableList<MutableList<Habilidade>> = mutableListOf()
        usersArrayList.forEach {
            it.habilidades!!.forEach {
                habilidadeViewModel.getItem(it)
            }
            skills.add(habilidadesArrayList)
            habilidadesArrayList.clear()
        }

        skills.forEach {
            it.forEach {
                val habilidade = it
                if (habilidade.habilidade!!.contains(query!!, true)) {
                    val la = usersArrayList.filter {
                        it.habilidades!!.contains(
                            db.document("habilidades/${habilidade.id!!}"))
                    }
                    lista.addAll(la)
                }
            }
        }
    }

    fun setUpRecyclerView(recyclerView: RecyclerView, lista: List<User>) {
        val listaHabilidades : MutableList<MutableList<Habilidade>> = mutableListOf()

        lista.forEach {
            it.habilidades!!.forEach {
                habilidadeViewModel.getItem(it)
            }
            listaHabilidades.add(habilidadesArrayList)
            habilidadesArrayList.clear()
        }

        listaHabilidades.take(10)

        val adapter = CardViewAdapter(lista, listaHabilidades)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param query Query.
         * @return A new instance of fragment HomeSearch.
         */
        @JvmStatic
        fun newInstance(query: String) =
            HomeSearch().apply {
                arguments = Bundle().apply {
                    putString(QUERY, query)
                }
            }
    }
}