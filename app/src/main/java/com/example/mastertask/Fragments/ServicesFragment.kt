package com.example.mastertask.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapterServices
import com.example.mastertask.Adapters.NotFoundAdapter
import com.example.mastertask.Data.CardServiceInfo
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.User
import com.example.mastertask.Models.ServiceViewModel
import com.example.mastertask.Models.UserViewModel
import com.example.mastertask.R
import com.google.firebase.auth.FirebaseAuth

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

    lateinit var recycler_view_novas_solicitacoes : RecyclerView
    lateinit var recycler_view_servicos_a_serem_feitos_por_voce : RecyclerView
    lateinit var recycler_view_servicos_solicitados_por_voce : RecyclerView

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser = auth.currentUser!!
    val userEmail = currentUser.email

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
        serviceViewModel.getList()
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

        var lista : List<Service> = serviceArrayList.filter { it.emailCliente == userEmail &&
            it.status != "Finalizado (cliente)" && it.status != "Cancelado (cliente)"}
            .sortedByDescending { it.dataHora }
        if (!lista.isEmpty()) {
            val x = createList(lista)
            val adapter = CardViewAdapterServices(x, object :
                CardViewAdapterServices.OnItemClickListener {
                override fun onItemClick(item: CardServiceInfo?) {
                    val y = StatusServiceClient.newInstance(
                        item!!.id!!, item.nome!!, item.endereco!!,
                        item.contato!!, item.somaAvaliacoes!!, item.numServicosFeitos!!,
                        item.dataHora!!, item.emailCliente!!, item.emailTrab!!, item.status!!
                    )
                    y.setHabilidades(item.habilidades!!)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, y).commit()
                }
            })
            recycler_view_servicos_solicitados_por_voce.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else {
            val adapter = NotFoundAdapter("Você não solicitou nenhum serviço até o momento.")
            recycler_view_servicos_solicitados_por_voce.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        lista = serviceArrayList.filter { it.emailTrab == userEmail && it.status == "Aceito" }
            .sortedByDescending { it.dataHora }
        if (!lista.isEmpty()) {
            val x = createList(lista)
            val adapter = CardViewAdapterServices(x, object :
                CardViewAdapterServices.OnItemClickListener {
                override fun onItemClick(item: CardServiceInfo?) {
                    val y = StatusServiceWorker.newInstance(
                        item!!.id!!, item.nome!!, item.endereco!!,
                        item.contato!!, item.somaAvaliacoes!!, item.numServicosFeitos!!,
                        item.dataHora!!, item.emailCliente!!, item.emailTrab!!, item.status!!
                    )
                    y.setHabilidades(item.habilidades!!)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, y).commit()
                }
            })
            recycler_view_servicos_a_serem_feitos_por_voce.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else {
            val adapter = NotFoundAdapter("Você não tem que prestar nenhum serviço no momento.")
            recycler_view_servicos_a_serem_feitos_por_voce.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        lista = serviceArrayList.filter { it.emailTrab == userEmail && it.status == "Pendente" }
        if (!lista.isEmpty()) {
            val x = createList(lista)
            val adapter = CardViewAdapterServices(x, object :
                CardViewAdapterServices.OnItemClickListener {
                override fun onItemClick(item: CardServiceInfo?) {
                    val y = ServiceConfirmWorker.newInstance(
                        item!!.id!!, item.nome!!, item.endereco!!,
                        item.contato!!, item.somaAvaliacoes!!, item.numServicosFeitos!!,
                        item.dataHora!!, item.emailCliente!!, item.emailTrab!!, item.status!!
                    )
                    y.setHabilidades(item.habilidades!!)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, y).commit()
                }
            })
            recycler_view_novas_solicitacoes.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else {
            val adapter = NotFoundAdapter("Você não possui novas solicitações de serviços.")
            recycler_view_novas_solicitacoes.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun createList(lista: List<Service>)
    : ArrayList<CardServiceInfo> {
        val listCardServiceInfo = ArrayList<CardServiceInfo>()

        lista.forEach {
            val user: User
            if (it.emailCliente == userEmail) {
                val emailTrab = it.emailTrab
                user = usersArrayList.filter {
                    it.id == emailTrab
                }.get(0)
            }
            else {
                val emailCliente = it.emailCliente
                user = usersArrayList.filter {
                    it.id == emailCliente
                }.get(0)
            }
            val data = CardServiceInfo(
                it.id, user.nome, user.endereco, user.contato, user.somaAvaliacoes,
                user.numServicosFeitos, it.dataHora, it.emailCliente, it.emailTrab, it.habilidades,
                it.status)
            listCardServiceInfo.add(data)
        }

        return listCardServiceInfo
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