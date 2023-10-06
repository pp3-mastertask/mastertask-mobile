package com.example.mastertask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapter
import com.example.mastertask.Adapters.CardViewAdapterServices
import com.example.mastertask.Data.CardServiceInfo
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.Status
import com.example.mastertask.Models.UserViewModel
import com.example.mastertask.Data.User
import com.example.mastertask.Models.ServiceViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServicesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val userViewModel : UserViewModel by viewModels()
    val serviceViewModel : ServiceViewModel by viewModels()

    var usersArrayList : ArrayList<User> = ArrayList()
    var serviceArrayList : ArrayList<Service> = ArrayList()

    var currentUser : User = User()
    var currentService : Service = Service()
    var listCardServiceInfo = ArrayList<CardServiceInfo>()

    lateinit var recycler_view_novas_solicitacoes : RecyclerView
    lateinit var recycler_view_servicos_a_serem_feitos_por_voce : RecyclerView
    lateinit var recycler_view_servicos_solicitados_por_voce : RecyclerView

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
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModels()
        initViews(view)
    }

    fun initViews(view : View) {
        recycler_view_servicos_solicitados_por_voce = view.findViewById(R.id.recycler_view_servicos_solicitados_por_voce) as RecyclerView
        recycler_view_novas_solicitacoes = view.findViewById(R.id.recycler_view_novas_solicitacoes_de_servicos) as RecyclerView
        recycler_view_servicos_a_serem_feitos_por_voce = view.findViewById(R.id.recycler_view_servicos_a_serem_feitos_por_voce) as RecyclerView

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
        }

        userViewModel.getItemLiveData.observe(viewLifecycleOwner) {
            currentUser = it!!
            addToList()
        }

        serviceViewModel.createLiveData.observe(viewLifecycleOwner) {
            if (it) {
                serviceViewModel.getList()
            }
        }

        serviceViewModel.updateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                serviceViewModel.getList()
            }
        }

        serviceViewModel.deleteLiveData.observe(viewLifecycleOwner) {
            if (it) {
                serviceViewModel.getList()
            }
        }

        serviceViewModel.getListLiveData.observe(viewLifecycleOwner) {
            serviceArrayList = ArrayList()
            serviceArrayList.addAll(it)

            setUpRecyclerViews()
        }
    }

    fun setUpRecyclerViews() {
        // mudar depois
        val seuEmail = "mcvsk.filho@gmail.com"

        var lista : List<Service> = serviceArrayList.filter { it.emailCliente == seuEmail }
            .sortedByDescending { it.dataHora }
        lista.take(10)
        if (!lista.isEmpty())
            setUpRecyclerView(recycler_view_servicos_solicitados_por_voce, lista)

        lista = serviceArrayList.filter { it.emailTrab == seuEmail && it.status == Status.Aceito.toString() }
            .sortedByDescending { it.dataHora }
        lista.take(10)
        if (!lista.isEmpty())
            setUpRecyclerView(recycler_view_servicos_a_serem_feitos_por_voce, lista)

        lista = serviceArrayList.filter { it.emailTrab == seuEmail && it.status == Status.Pendente.toString() }
        lista.take(10)
        if (!lista.isEmpty())
            setUpRecyclerView(recycler_view_novas_solicitacoes, lista)
    }

    fun setUpRecyclerView(recyclerView: RecyclerView, lista: List<Service>) {
        getListFromServices(lista)

        val adapter = CardViewAdapterServices(listCardServiceInfo)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun addToList() {
        val user = currentUser
        val service = currentService

        val data = CardServiceInfo(
            user.nome, user.endereco, user.contato, user.somaAvaliacoes, user.numServicosFeitos,
            service.dataHora, service.emailCliente, service.emailTrab, service.habilidades,
            service.status, service.terminado)

        listCardServiceInfo.add(data)
    }

    fun getListFromServices(lista : List<Service>) {
        lista.forEach {
            currentService = it
            userViewModel.getItem(it.emailTrab!!)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ServicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}