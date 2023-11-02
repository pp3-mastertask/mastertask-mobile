package com.example.mastertask.Fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.Data.User
import com.example.mastertask.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

private const val NOME = "nome"
private const val IMGURL = "imgUrl"
private const val ENDERECO = "endereco"
private const val CONTATO = "contato"
private const val SOMAAVALIACOES = "somaAvaliacoes"
private const val NUMSERVICOESFEITOS = "numServicosFeitos"
private const val EMAILCLIENTE = "emailCliente"
private const val EMAILTRAB = "emailTrab"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedService.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedService : Fragment() {
    private var imgUrl: String? = null
    private var nome: String? = null
    private var endereco: String? = null
    private var contato: String? = null
    private var somaAvaliacoes: Double? = null
    private var numServicosFeitos: Long? = null
    private var emailCliente: String? = null
    private var emailTrab: String? = null
    private var habilidades: List<Map<String?, Any?>>? = null

    private var selectedDate : Timestamp = Timestamp.now()

    private var selectedSkills: ArrayList<Map<String?, Any?>>? = ArrayList()

    lateinit var nameWorker: TextView
    lateinit var location : TextView
    lateinit var stars : TextView
    lateinit var phone : TextView

    lateinit var cancelar : Button
    lateinit var proximo : Button

    lateinit var calendar : CalendarView

    lateinit var rvHabilidades : RecyclerView

    lateinit var imgFotoPerfil : ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgUrl = it.getString(IMGURL)
            nome = it.getString(NOME)
            endereco = it.getString(ENDERECO)
            emailCliente = it.getString(EMAILCLIENTE)
            emailTrab = it.getString(EMAILTRAB)
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
        ColocarEventListeners()
    }

    fun PreencherInfo(){
        nameWorker.setText(nome)
        location.setText(endereco)
        phone.setText(contato.toString())
        if (numServicosFeitos != 0L)
            stars.setText(somaAvaliacoes!!.div(numServicosFeitos!!).toString())
        stars.setText("0.0")

        val badgeHabilidades = BadgeViewAdapter(habilidades, object :
            BadgeViewAdapter.OnBadgeClickListener {
            override fun onBadgeClick (badge: Map<String?, Any?>, position: Int, holder: View) {

                if(!selectedSkills!!.contains(badge)){
                    selectedSkills!!.add(badge)
                    holder.background = resources.getDrawable(R.drawable.badge_selected)
                } else {
                    selectedSkills!!.remove(badge)
                    holder.background = resources.getDrawable(R.drawable.badge)
                }
            }})

        rvHabilidades.adapter = badgeHabilidades
        badgeHabilidades.notifyDataSetChanged()

        Picasso.get().load(imgUrl).into(imgFotoPerfil)
    }

    fun ColocarEventListeners(){
        cancelar.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }

        proximo.setOnClickListener {
            if (selectedSkills!!.isEmpty()) {
                Toast.makeText(context, "Selecione um serviço antes de prosseguir!",
                    Toast.LENGTH_LONG).show()
            }
            else {
                val currentUser = FirebaseAuth.getInstance().currentUser!!
                val userEmail = currentUser.email.toString()
                 val y = ServiceConfirmClient.newInstance(nome!!, imgUrl!!, endereco!!, contato!!,
                     somaAvaliacoes!!, numServicosFeitos!!, selectedDate, userEmail,
                     emailTrab!!, "Pendente")
                 y.setHabilidades(selectedSkills!!)
                 parentFragmentManager.beginTransaction()
                     .replace(R.id.fragment_container, y).commit()
            }
        }

        calendar.setOnDateChangeListener(object: CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, day: Int) {
                val c = java.util.Calendar.getInstance()
                c.set(year, month, day)
                selectedDate = Timestamp(Date(c.timeInMillis))
            }
        })
    }

    fun initViews(view : View) {
        nameWorker = view.findViewById(R.id.nameProfissional) as TextView
        location = view.findViewById(R.id.location) as TextView
        phone = view.findViewById(R.id.phone) as TextView
        stars = view.findViewById(R.id.stars) as TextView
        rvHabilidades = view.findViewById(R.id.recycler_view_skills_available) as RecyclerView
        calendar = view.findViewById(R.id.calendarView) as CalendarView
        cancelar = view.findViewById(R.id.btnCancelar) as Button
        proximo  = view.findViewById(R.id.btnProximo) as Button
        imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil) as ShapeableImageView
    }

    fun setHabilidade(habilidade: List<Map<String?, Any?>>?){
        habilidades = habilidade
    }


    companion object {
        @JvmStatic
        fun newInstance(user: User) = SelectedService().apply {
            arguments = Bundle().apply {
                putString(IMGURL, user.imagem)
                putString(NOME, user.nome)
                putString(ENDERECO, user.endereco)
                putString(EMAILTRAB, user.id)
                putString(CONTATO, user.contato)
                putDouble(SOMAAVALIACOES, user.somaAvaliacoes!!)
                putLong(NUMSERVICOESFEITOS, user.numServicosFeitos!!)
            }
        }
    }
}