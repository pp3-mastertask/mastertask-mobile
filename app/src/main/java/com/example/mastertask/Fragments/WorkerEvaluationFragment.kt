package com.example.mastertask.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.BadgeViewAdapter
import com.example.mastertask.Data.Avaliacao
import com.example.mastertask.Data.User
import com.example.mastertask.Models.AvaliacaoViewModel
import com.example.mastertask.Models.UserViewModel
import com.example.mastertask.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

private const val ID_SERVICO = "idServico"
private const val IMAGEM = "imagem"
private const val NOME = "nome"
private const val EMAIL = "email"
private const val ENDERECO = "endereco"
private const val SOMA_AVALIACOES = "somaAvaliacoes"
private const val NUM_SERVICOES_FEITOS = "numServicosFeitos"

/**
 * A simple [Fragment] subclass.
 * Use the [WorkerEvaluation.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkerEvaluationFragment : Fragment() {
    private var idServico: String? = null
    private var imagem: String? = null
    private var nome: String? = null
    private var endereco: String? = null
    private var somaAvaliacoes: Double = 0.0
    private var numServicosFeitos: Long = 0
    private var email: String? = null

    val userViewModel : UserViewModel by viewModels()
    val avaliacaoViewModel: AvaliacaoViewModel by viewModels()

    private lateinit var lbNome : TextView
    private lateinit var lbEndereco : TextView
    private lateinit var lbAvaliacao : TextView

    private lateinit var txtFeedback: EditText
    private lateinit var ratingBar: RatingBar

    private lateinit var btnNaoFinalizouServico : Button
    private lateinit var btnFinalizouServico : Button
    private lateinit var btnNaoTerminarAvaliacao : Button
    private lateinit var btnTerminarAvaliacao : Button

    private lateinit var imgFotoPerfil : ShapeableImageView

    private lateinit var rvHabilidades : RecyclerView
    private lateinit var habilidades: List<Map<String?, Any?>>

    private lateinit var avaliacao: Avaliacao
    private lateinit var usuario: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.idServico = it.getString(ID_SERVICO)
            this.imagem = it.getString(IMAGEM)
            this.nome = it.getString(NOME)
            this.endereco = it.getString(ENDERECO)
            this.somaAvaliacoes = it.getDouble(SOMA_AVALIACOES)
            this.numServicosFeitos = it.getLong(NUM_SERVICOES_FEITOS)
            this.email = it.getString(EMAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_worker_evaluation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.avaliacao = Avaliacao()
        this.userViewModel.getItem(this.email!!)

        this.initModels()
        this.initViews(view)
    }

    fun initViews(view: View) {
        this.lbNome = view.findViewById(R.id.nameProfissional)
        this.lbEndereco = view.findViewById(R.id.location)
        this.lbAvaliacao = view.findViewById(R.id.stars)

        this.txtFeedback = view.findViewById(R.id.edit_text)
        this.ratingBar = view.findViewById(R.id.ratingBar)

        this.imgFotoPerfil = view.findViewById(R.id.imgFotoPerfil)

        this.rvHabilidades = view.findViewById(R.id.rvHabilidades)

        this.btnNaoFinalizouServico = view.findViewById(R.id.btnNao)
        this.btnFinalizouServico = view.findViewById(R.id.btnSim)
        this.btnNaoTerminarAvaliacao = view.findViewById(R.id.btnNaoObrigado)
        this.btnTerminarAvaliacao = view.findViewById(R.id.btnPronto)

        this.btnTerminarAvaliacao.isEnabled = false

        this.addValues()
        this.addEventListeners()
    }

    fun addEventListeners() {
        this.btnNaoTerminarAvaliacao.setOnClickListener {  parentFragmentManager.beginTransaction().replace(
            R.id.fragment_container, ServicesFragment()
        ).commit() }

        this.btnNaoFinalizouServico.setOnClickListener {
            this.avaliacao.terminado = false
            this.btnTerminarAvaliacao.isEnabled = true
        }
        this.btnFinalizouServico.setOnClickListener {
            this.avaliacao.terminado = true
            this.btnTerminarAvaliacao.isEnabled = true
        }

        this.btnTerminarAvaliacao.setOnClickListener {
            if (this.txtFeedback.text.toString() != "")
                this.avaliacao.comentario = this.txtFeedback.text.toString()
            else
                this.avaliacao.comentario = ""

            val avaliacao = this.ratingBar.rating.toDouble()
            this.avaliacao.estrelas = avaliacao

            this.avaliacao.servico = idServico

            this.avaliacaoViewModel.create(this.avaliacao)

            this.usuario.somaAvaliacoes = this.usuario.somaAvaliacoes?.plus(avaliacao)
            this.usuario.numServicosFeitos = this.usuario.numServicosFeitos?.plus(1)

            this.userViewModel.update(this.usuario)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ServicesFragment()).commit()
        }
    }

    fun addValues() {
        Picasso.get().load(imagem).into(imgFotoPerfil)

        this.lbNome.text = this.nome
        this.lbEndereco.text = this.endereco
        if (this.numServicosFeitos == 0L)
            this.lbAvaliacao.text = "0.0"
        else
            this.lbAvaliacao.text = (this.somaAvaliacoes.div(this.numServicosFeitos)).toString()

        val adapter_servicos = BadgeViewAdapter(habilidades, null)
        this.rvHabilidades.adapter = adapter_servicos
        adapter_servicos.notifyDataSetChanged()
    }

    fun setHabilidades(hab: List<Map<String?, Any?>>) {
        this.habilidades = hab
    }

    fun initModels() {
        this.userViewModel.updateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                this.userViewModel.getList()
            }
        }

        this.userViewModel.getItemLiveData.observe(viewLifecycleOwner) {
            this.usuario = it
        }

        this.avaliacaoViewModel.createLiveData.observe(viewLifecycleOwner) {
            if (it) {
                this.avaliacaoViewModel.getList()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param idServico Service id.
         * @param imagem Profile picture.
         * @param nome Worker's name.
         * @param endereco Worker address.
         * @param somaAvaliacoes Evaluation sum.
         * @param numServicosFeitos Number of done services.
         * @param email Worker email.
         * @return A new instance of fragment WorkerEvaluation.
         */
        @JvmStatic
        fun newInstance(idServico: String?, imagem: String, nome: String, endereco: String,
                        somaAvaliacoes: Double, numServicosFeitos: Long, email: String) =
            WorkerEvaluationFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_SERVICO, idServico)
                    putString(IMAGEM, imagem)
                    putString(NOME, nome)
                    putString(ENDERECO, endereco)
                    putDouble(SOMA_AVALIACOES, somaAvaliacoes)
                    putLong(NUM_SERVICOES_FEITOS, numServicosFeitos)
                    putString(EMAIL, email)
                }
            }
    }
}