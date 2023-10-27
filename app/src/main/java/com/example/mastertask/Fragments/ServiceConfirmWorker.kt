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
import java.util.*

private const val ID = "id"
private const val NOME = "nome"
private const val IMGURL = "imgUrl"
private const val ENDERECO = "endereco"
private const val CONTATO = "contato"
private const val SOMAAVALIACOES = "somaAvaliacoes"
private const val NUMSERVICOESFEITOS = "numServicosFeitos"
private const val DATAHORA = "dataHora"
private const val EMAILCLIENTE = "emailCliente"
private const val EMAILTRAB = "emailTrab"
private const val STATUS = "status"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceConfirmWorker.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceConfirmWorker : Fragment() {
    private var id: String? = null
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

    private lateinit var lbNomeCliente : TextView
    private lateinit var lbEnderecoCliente : TextView
    private lateinit var lbAvaliacaoCliente : TextView
    private lateinit var lbDataPrevista : TextView
    private lateinit var lbTotalAReceber : TextView

    private lateinit var imgFotoPerfil: ImageView

    private lateinit var btnCancelar : Button
    private lateinit var btnAceitar : Button

    private lateinit var rvServicosSolicitados : RecyclerView
    private lateinit var rvPrecosServicos : RecyclerView

    val serviceViewModel : ServiceViewModel by viewModels()

    var serviceArrayList : ArrayList<Service> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            nome = it.getString(NOME)
            imgUrl = it.getString(IMGURL)
            endereco = it.getString(ENDERECO)
            contato = it.getString(CONTATO)
            somaAvaliacoes = it.getDouble(SOMAAVALIACOES)
            numServicosFeitos = it.getLong(NUMSERVICOESFEITOS)
            dataHora = Timestamp(Date(it.getLong(DATAHORA)))
            emailCliente = it.getString(EMAILCLIENTE)
            emailTrab = it.getString(EMAILTRAB)
            status = it.getString(STATUS)
        }
    }

    fun setHabilidades(hab: List<Map<String?, Any?>>) {
        habilidades = hab
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_confirm_worker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModels()
        initViews(view)
    }

    fun initViews(view : View) {
        lbNomeCliente = view.findViewById(R.id.lb_nome_cliente) as TextView
        lbEnderecoCliente = view.findViewById(R.id.lb_endereco_cliente) as TextView
        lbAvaliacaoCliente = view.findViewById(R.id.lb_avaliacao_cliente) as TextView
        lbTotalAReceber = view.findViewById(R.id.lb_total_a_receber) as TextView
        lbDataPrevista = view.findViewById(R.id.lb_data_prevista) as TextView

        imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil) as ImageView

        btnCancelar = view.findViewById(R.id.btnCancelar) as Button
        btnAceitar = view.findViewById(R.id.btnAceitar) as Button

        rvServicosSolicitados = view.findViewById(R.id.rv_servicos_solicitados) as RecyclerView
        rvPrecosServicos = view.findViewById(R.id.recycler_view_precos_servicos) as RecyclerView

        addValues()
        addEventListeners()
    }

    fun initModels() {
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
        }
    }

    fun addValues() {
        Picasso.get().load(imgUrl).into(imgFotoPerfil)

        lbNomeCliente.text = nome
        lbEnderecoCliente.text = endereco
        if ((somaAvaliacoes!!.div(numServicosFeitos!!)).isNaN())
            lbAvaliacaoCliente.text = 0.0.toString()
        else
            lbAvaliacaoCliente.text = (somaAvaliacoes!!.div(numServicosFeitos!!)).toString()
        lbDataPrevista.text = SimpleDateFormat("dd/MM/yyyy").format(dataHora!!.toDate())

        var precoTotal = 0.0
        habilidades!!.forEach {
            try {
                precoTotal += (it["preco"] as Long).toDouble()
            }
            catch (e: Error) {
                precoTotal += it["preco"] as Double
            }
        }
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.setMaximumFractionDigits(2)
        format.setCurrency(Currency.getInstance("BRL"))
        lbTotalAReceber.text = format.format(precoTotal)

        val adapterHabilidades = BadgeViewAdapter(habilidades)
        rvServicosSolicitados.adapter = adapterHabilidades
        adapterHabilidades.notifyDataSetChanged()

        val adapterPrices = ServicePriceAdapter(habilidades)
        rvPrecosServicos.adapter = adapterPrices
        adapterPrices.notifyDataSetChanged()
    }

    fun addEventListeners() {
        btnAceitar.setOnClickListener {
            val service = Service(id, dataHora, emailCliente, emailTrab, habilidades, "Aceito")
            serviceViewModel.update(service)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ServicesFragment()).commit()
        }

        btnCancelar.setOnClickListener {
            val service = Service(id, dataHora, emailCliente, emailTrab, habilidades,
                "Cancelado (prestador)")
            serviceViewModel.update(service)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ServicesFragment()).commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param id Service id on firebase.
         * @param nome Name of the worker.
         * @param imgUrl Profile picture url.
         * @param endereco Worker address.
         * @param contato Worker contact.
         * @param somaAvaliacoes Evaluation sum.
         * @param numServicosFeitos Number of done services.
         * @param dataHora Datetime service was scheduled to.
         * @param emailCliente Client email.
         * @param emailTrab Worker email.
         * @param habilidades Skills selected for the service.
         * @param status Service status.
         * @return A new instance of fragment ServiceConfirmWorker.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, nome: String, imgUrl: String, endereco: String, contato: String,
                        somaAvaliacoes: Double, numServicosFeitos: Long, dataHora: Timestamp,
                        emailCliente: String, emailTrab: String, status: String?) =
            ServiceConfirmWorker().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(NOME, nome)
                    putString(IMGURL, imgUrl)
                    putString(ENDERECO, endereco)
                    putString(CONTATO, contato)
                    putDouble(SOMAAVALIACOES, somaAvaliacoes)
                    putLong(NUMSERVICOESFEITOS, numServicosFeitos)
                    putLong(DATAHORA, dataHora.toDate().time)
                    putString(EMAILCLIENTE, emailCliente)
                    putString(EMAILTRAB, emailTrab)
                    putString(STATUS, status)
                }
            }
    }
}