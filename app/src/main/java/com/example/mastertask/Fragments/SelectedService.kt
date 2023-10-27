package com.example.mastertask.Fragments

import HabilidadeAdapter //new
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mastertask.Data.Service
import android.view.textclassifier.SelectionEvent
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.Adapters.CardViewAdapterServices
import com.example.mastertask.Data.CardServiceInfo
import com.example.mastertask.Data.User
import com.example.mastertask.Models.HabilidadeViewModel //new
import com.example.mastertask.Models.SkillModel
import com.example.mastertask.R
import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match

private const val ID = "id"
private const val NOME = "nome"
private const val ENDERECO = "endereco"
private const val CONTATO = "contato"
private const val SOMAAVALIACOES = "somaAvaliacoes"
private const val NUMSERVICOESFEITOS = "numServicosFeitos"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedService.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedService : Fragment() {
    private var id: String? = null
    private var nome: String? = null
    private var endereco: String? = null
    private var contato: String? = null
    private var somaAvaliacoes: Double? = null
    private var numServicosFeitos: Long? = null
    private var habilidades: List<Map<String?, Any?>>? = null

    private var selectedSkills: ArrayList<Map<String?, Any?>>? = ArrayList()

    lateinit var location : TextView
    lateinit var stars : TextView
    lateinit var phone : TextView

    lateinit var cancelar : Button
    lateinit var proximo : Button

    lateinit var calendar : CalendarView

    lateinit var rvHabilidades : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            nome = it.getString(NOME)
            endereco = it.getString(ENDERECO)
            contato = it.getString(CONTATO)
            somaAvaliacoes = it.getDouble(SOMAAVALIACOES)
            numServicosFeitos = it.getLong(NUMSERVICOESFEITOS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        PreencherInfo()
    }

    fun PreencherInfo(){
        location.setText(endereco)
        phone.setText(phone.toString())
        stars.setText(stars.toString())

        val badgeHabilidades = BadgeViewAdapter(habilidades, object :
            BadgeViewAdapter.OnBadgeClickListener {
            override fun onBadgeClick (badge: Map<String?, Any?>, position: Int) {

                if(!selectedSkills!!.contains(badge)){
                    selectedSkills!!.add(badge)
                } else {
                    selectedSkills!!.remove(badge)
                }
            }})

        rvHabilidades.adapter = badgeHabilidades
        badgeHabilidades.notifyDataSetChanged()
    }

    fun ColocarEventListeners(){
        cancelar.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }

        //TODO:passar informacoes do servico para tela de confirmacao de servicodo trabalhador (todas as infos do dataclassservice dentro da pasta data)
        proximo.setOnClickListener{
            val y = ServiceConfirmClient.newInstance(
                item!!.id!!, item.nome!!, item.imgFoto!!, item.endereco!!,
                item.contato!!, item.somaAvaliacoes!!, item.numServicosFeitos!!,
                item.dataHora!!, item.emailCliente!!, item.emailTrab!!, item.status!!
            )
            y.setHabilidades(item.habilidades!!)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, y).commit()
        }
    }

    fun initViews(view : View) {
        location = view.findViewById(R.id.location) as TextView
        phone = view.findViewById(R.id.phone) as TextView
        stars = view.findViewById(R.id.stars) as TextView
        rvHabilidades = view.findViewById(R.id.recycler_view_skills_available) as RecyclerView
        calendar = view.findViewById(R.id.calendarView) as CalendarView
        cancelar = view.findViewById(R.id.btnCancelar) as Button
        proximo  = view.findViewById(R.id.btnProximo) as Button
    }

    fun setHabilidade(habilidade: List<Map<String?, Any?>>?){
        habilidades = habilidade
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectedService.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: User) = SelectedService().apply {
            arguments = Bundle().apply {
                putString(ID, user.id)
                putString(NOME, user.nome)
                putString(ENDERECO, user.endereco)
                putString(CONTATO, user.contato)
                putDouble(SOMAAVALIACOES, user.somaAvaliacoes!!)
                putLong(NUMSERVICOESFEITOS, user.numServicosFeitos!!)
            }
        }
    }
}