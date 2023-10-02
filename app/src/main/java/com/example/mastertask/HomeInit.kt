package com.example.mastertask

import HabilidadeViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapter
import com.example.mastertask.Data.Habilidade
import com.example.mastertask.Data.User
import com.example.mastertask.Models.UserViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeInit.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeInit : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recycler_view_recomendacoes : RecyclerView
    lateinit var recycler_view_novos : RecyclerView
    lateinit var recycler_view_jacontratados : RecyclerView

    val userViewModel : UserViewModel by viewModels()
    val habilidadeViewModel: HabilidadeViewModel by viewModels()

    var usersArrayList : ArrayList<User> = ArrayList()
    var habilidadesArrayList : ArrayList<Habilidade> = ArrayList()

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

        return inflater.inflate(R.layout.fragment_home_init, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModels()
        initViews(view)
    }

    fun initViews(view : View) {
        recycler_view_recomendacoes = view.findViewById(R.id.recycler_view_recomendacoes) as RecyclerView
        recycler_view_novos = view.findViewById(R.id.recycler_view_novos) as RecyclerView
        recycler_view_jacontratados = view.findViewById(R.id.recycler_view_jacontratados) as RecyclerView

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
        var lista : List<User> =
            usersArrayList.sortedByDescending { it.somaAvaliacoes!!/it.numServicosFeitos!! }
        lista.take(10)
        setUpRecyclerView(recycler_view_recomendacoes, lista)

        lista = usersArrayList.sortedByDescending { it.dataInicio }
        lista.take(10)
        setUpRecyclerView(recycler_view_novos, lista)

        // para pegar trabalhadores já contratados, é necessário ter o email do usuário
        // (parte de cadastro precisa estar pronta)
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeInit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeInit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}