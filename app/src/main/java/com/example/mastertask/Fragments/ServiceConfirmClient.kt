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
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.User
import com.example.mastertask.Models.ServiceViewModel
import com.example.mastertask.Models.UserViewModel
import com.example.mastertask.R
import com.example.mastertask.R.id
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Currency
import java.util.Date

private const val ID = "id"
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

//TODO: criar todas as views do xml confirm service client, preencher informacoes (ver status confirm worker), eventlisteners dos botoes de confirmar solicitacao de servico e de cancelar servico proposto
class ServiceConfirmClient : Fragment() {
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
            dataHora = Timestamp(Date(it.getLong(DATAHORA)))
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
        if ((somaAvaliacoes!!.div(numServicosFeitos!!)).isNaN())
            lbAvaliacaoPrestador.text = 0.0.toString()
        else
            lbAvaliacaoPrestador.text = (somaAvaliacoes!!.div(numServicosFeitos!!)).toString()
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
        lbTotalAPagar.text = format.format(precoTotal)

        val adapterHabilidades = BadgeViewAdapter(habilidades)
        rvServicosSolicitados.adapter = adapterHabilidades
        adapterHabilidades.notifyDataSetChanged()

        val adapterPrices = ServicePriceAdapter(habilidades)
        rvPrecosServicos.adapter = adapterPrices
        adapterPrices.notifyDataSetChanged()
    }

    fun addEventListeners(){
        btnConcluir.setOnClickListener {
            val service = Service(id, dataHora, emailCliente, emailTrab, habilidades, "Aceito")
            serviceViewModel.create(service)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ServicesFragment()).commit()
        }

        btnCancelar.setOnClickListener {
            val service = Service(id, dataHora, emailCliente, emailTrab, habilidades,
                "Cancelado")
            serviceViewModel.update(service)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ServicesFragment()).commit()
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

        //addValues()
        //addEventListeners()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(id: String, nome: String, imgUrl: String, endereco: String, contato: String,
                        somaAvaliacoes: Double, numServicosFeitos: Long, dataHora: Timestamp
                        emailCliente: String, emailTrab: String, status: String?) =
            ServiceConfirmWorker().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(ID, nome)
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