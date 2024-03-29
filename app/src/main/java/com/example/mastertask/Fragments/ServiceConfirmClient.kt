package com.example.mastertask.Fragments

import ServicePriceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.Data.Service
import com.example.mastertask.Models.ServiceViewModel
import com.example.mastertask.R
import com.google.firebase.Timestamp
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Currency
import java.util.Date

private const val NOME = "nome"
private const val IMGURL = "imgUrl"
private const val ENDERECO = "endereco"
private const val CONTATO = "contato"
private const val SOMAAVALIACOES = "somaAvaliacoes"
private const val DATAHORA = "dataHora"
private const val NUMEROSERVICOSFEITOS = "numServicosFeitos"
private const val EMAILCLIENTE = "emailCliente"
private const val EMAILTRAB = "emailTrab"
private const val STATUS = "status"

class ServiceConfirmClient : Fragment() {
    private var nome: String? = null
    private var imgUrl: String? = null
    private var endereco: String? = null
    private var contato: String? = null
    private var somaAvaliacoes: Double? = null
    private var numServicosFeitos: Long? = null
    private var dataHora: Timestamp? = null
    private var emailCliente: String? = null
    private var emailTrab: String? = null
    private var habilidades: List<Map<String?, Any?>>? = null
    private var status: String? = null

    private lateinit var lbNomePrestador : TextView
    private lateinit var lbAvaliacaoPrestador : TextView
    private lateinit var lbEnderecoPrestador : TextView
    private lateinit var lbDataPrevista : TextView
    private lateinit var lbTotalAPagar : TextView

    private lateinit var imgFotoPerfil: ImageView

    private lateinit var btnCancelar : Button
    private lateinit var btnConcluir : Button

    private lateinit var rvServicosSolicitados : RecyclerView
    private lateinit var rvPrecosServicos : RecyclerView

    val serviceViewModel : ServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nome = it.getString(NOME)
            imgUrl = it.getString(IMGURL)
            endereco = it.getString(ENDERECO)
            contato = it.getString(CONTATO)
            somaAvaliacoes = it.getDouble(SOMAAVALIACOES)
            numServicosFeitos = it.getLong(NUMEROSERVICOSFEITOS)
            dataHora = Timestamp(Date(it.getLong(DATAHORA)))
            emailCliente = it.getString(EMAILCLIENTE)
            emailTrab = it.getString(EMAILTRAB)
            status = it.getString(STATUS)
        }
    }

    fun setHabilidades(hab: List<Map<String?, Any?>>){
        habilidades = hab
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_service_confirm_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    fun addValues() {
        Picasso.get().load(imgUrl).into(imgFotoPerfil)

        lbNomePrestador.text = nome
        lbEnderecoPrestador.text = endereco
        if (numServicosFeitos!! == 0L)
            lbAvaliacaoPrestador.text = 0.0.toString()
        else
            lbAvaliacaoPrestador.text = (somaAvaliacoes!!.div(numServicosFeitos!!)).toString()
        lbDataPrevista.text = SimpleDateFormat("dd/MM/yyyy").format(dataHora!!.toDate())

        var precoTotal = 0.0
        habilidades!!.forEach {
            if (it["preco"] is Long)
                precoTotal += (it["preco"] as Long).toDouble()
            else {
                precoTotal += it["preco"] as Double
            }
        }
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(2)
        format.setCurrency(Currency.getInstance("BRL"))
        lbTotalAPagar.text = format.format(precoTotal)

        val adapterHabilidades = BadgeViewAdapter(habilidades, null)
        rvServicosSolicitados.adapter = adapterHabilidades
        adapterHabilidades.notifyDataSetChanged()

        val adapterPrices = ServicePriceAdapter(habilidades)
        rvPrecosServicos.adapter = adapterPrices
        adapterPrices.notifyDataSetChanged()
    }

    fun addEventListeners(){
        btnConcluir.setOnClickListener {
            val service = Service("", dataHora, emailCliente, emailTrab, habilidades, status)
            serviceViewModel.create(service)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }

        btnCancelar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
        }
    }

    fun initViews(view: View){
        lbNomePrestador = view.findViewById(R.id.lb_nome_prestador) as TextView
        lbEnderecoPrestador = view.findViewById(R.id.lb_endereco_prestador) as TextView
        lbAvaliacaoPrestador = view.findViewById(R.id.lb_avaliacao_prestador) as TextView
        lbTotalAPagar = view.findViewById(R.id.lb_preco_total) as TextView
        lbDataPrevista = view.findViewById(R.id.lb_data_prevista) as TextView

        imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil) as ImageView

        btnConcluir = view.findViewById(R.id.btnConcluir) as Button
        btnCancelar = view.findViewById(R.id.btnCancelar) as Button

        rvServicosSolicitados = view.findViewById(R.id.rv_servicos_solicitados) as RecyclerView
        rvPrecosServicos = view.findViewById(R.id.recycler_view_precos_servicos) as RecyclerView

        addValues()
        addEventListeners()
    }

    companion object {
        @JvmStatic
        fun newInstance(nome: String, imgUrl: String, endereco: String, contato: String,
                        somaAvaliacoes: Double, numServicosFeitos: Long, dataHora: Timestamp,
                        emailCliente: String, emailTrab: String, status: String?) =
            ServiceConfirmClient().apply {
                arguments = Bundle().apply {
                    putString(NOME, nome)
                    putString(IMGURL, imgUrl)
                    putString(ENDERECO, endereco)
                    putString(CONTATO, contato)
                    putDouble(SOMAAVALIACOES, somaAvaliacoes)
                    putLong(NUMEROSERVICOSFEITOS, numServicosFeitos)
                    putLong(DATAHORA, dataHora.toDate().time)
                    putString(EMAILCLIENTE, emailCliente)
                    putString(EMAILTRAB, emailTrab)
                    putString(STATUS, status)
                }
            }
    }
}