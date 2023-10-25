package com.example.mastertask.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapter
import com.example.mastertask.Adapters.NotFoundAdapter
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.User
import com.example.mastertask.Models.ServiceViewModel
import com.example.mastertask.Models.UserViewModel
import com.example.mastertask.OnCardClickListener
import com.example.mastertask.R
import com.google.firebase.auth.FirebaseAuth
import java.security.Timestamp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeInit.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeInit : Fragment(), OnCardClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recycler_view_recomendacoes: RecyclerView
    lateinit var recycler_view_novos: RecyclerView
    lateinit var recycler_view_jacontratados: RecyclerView

    val userViewModel: UserViewModel by viewModels()
    val serviceViewModel: ServiceViewModel by viewModels()

    var usersArrayList: ArrayList<User> = ArrayList()
    var servicesArrayList: ArrayList<Service> = ArrayList()

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userEmail = currentUser?.email

    private val lista: List<User> = listOf(
        User(id = "1", contato = "contato@example.com", cpf = "123.456.789-00", dataInicio = Timestamp.now(), dataNascimento = Timestamp.now(), disponibilidade = disponibilidade, endereco = "Rua Exemplo, 123", habilidades = habilidades, nome = "Nome Exemplo", numServicosFeitos = 10L, somaAvaliacoes = 4.5),
        User(id = "2", contato = "contato@example.com", cpf = "123.456.789-00", dataInicio = Timestamp.now(), dataNascimento = Timestamp.now(), disponibilidade = disponibilidade, endereco = "Rua Exemplo, 123", habilidades = habilidades, nome = "Nome Exemplo", numServicosFeitos = 10L, somaAvaliacoes = 4.5),
        User(id = "3", contato = "contato@example.com", cpf = "123.456.789-00", dataInicio = Timestamp.now(), dataNascimento = Timestamp.now(), disponibilidade = disponibilidade, endereco = "Rua Exemplo, 123", habilidades = habilidades, nome = "Nome Exemplo", numServicosFeitos = 10L, somaAvaliacoes = 4.5),
    )

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

        setUpRecyclerView(recycler_view_recomendacoes, lista) //ERA PRA TER UM THIS COMO TERCEIRO ARGUMENTO NESSA P***
        setUpRecyclerView(recycler_view_novos, lista)
        setUpRecyclerView(recycler_view_jacontratados, lista)
    }

    override fun onCardClick(selectedService: SelectedService) {
        val selectedServiceFragment = SelectedService.newInstance(selectedService)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedServiceFragment)
            .addToBackStack(null)
            .commit()
    }

    fun initViews(view : View) {
        recycler_view_recomendacoes = view.findViewById(R.id.recycler_view_recomendacoes) as RecyclerView
        recycler_view_novos = view.findViewById(R.id.recycler_view_novos) as RecyclerView
        recycler_view_jacontratados = view.findViewById(R.id.recycler_view_jacontratados) as RecyclerView

        serviceViewModel.getList()
        userViewModel.getList()
    }

    fun initModels() {
        userViewModel.getListLiveData.observe(viewLifecycleOwner) {
            usersArrayList = ArrayList()
            usersArrayList.addAll(it)

            setUpRecyclerViews()
        }

        serviceViewModel.getListLiveData.observe(viewLifecycleOwner) {
            servicesArrayList = ArrayList()
            servicesArrayList.addAll(it)
        }
    }

    fun setUpRecyclerViews() {
        var lista : List<User> =
            usersArrayList.filter { it.habilidades!!.isNotEmpty() && it.id != userEmail }
                .sortedByDescending { it.somaAvaliacoes!!/it.numServicosFeitos!! }
        lista.take(25)
        if (lista.isNotEmpty())
            setUpRecyclerView(recycler_view_recomendacoes, lista)
        else
            setUpNotFound(recycler_view_recomendacoes, "Não há recomendações para você no momento.")

        lista = usersArrayList.filter { it.habilidades!!.isNotEmpty() && it.id != userEmail }
            .sortedByDescending { it.dataInicio }
        lista.take(25)
        if (lista.isNotEmpty())
            setUpRecyclerView(recycler_view_novos, lista)
        else
            setUpNotFound(recycler_view_novos, "Não há usuários novos para você no momento.")

        val listaEmailsTrabalhadoresServicosSolicitados = ArrayList<String>()
        servicesArrayList.forEach {
            if (it.emailCliente == userEmail)
                listaEmailsTrabalhadoresServicosSolicitados.add(it.emailTrab!!)
        }

        lista = usersArrayList.filter { it.id in listaEmailsTrabalhadoresServicosSolicitados }
            .sortedByDescending { it.dataInicio }
        lista.take(25)
        if (lista.isNotEmpty())
            setUpRecyclerView(recycler_view_jacontratados, lista)
        else
            setUpNotFound(recycler_view_jacontratados, "Você ainda não solicitou nenhum serviço.")
    }

    fun setUpRecyclerView(recyclerView: RecyclerView, lista: List<User>) {
        val adapter = CardViewAdapter(lista)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun setUpNotFound(recyclerView: RecyclerView, mensagem: String) {
        val adapter = NotFoundAdapter(mensagem)
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