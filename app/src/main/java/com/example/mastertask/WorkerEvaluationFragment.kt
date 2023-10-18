package com.example.mastertask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Data.Avaliacao
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.User
import com.example.mastertask.Models.AvaliacaoViewModel
import com.example.mastertask.Models.ServiceViewModel
import com.example.mastertask.Models.UserViewModel

private const val ID_AVALIACAO = ""
private const val NOME = ""
private const val EMAIL = ""
private const val ENDERECO = ""
private const val SOMA_AVALIACOES = ""
private const val NUM_SERVICOES_FEITOS = ""

/**
 * A simple [Fragment] subclass.
 * Use the [WorkerEvaluation.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkerEvaluationFragment : Fragment() {
    private var id_avaliacao: String? = null
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

    private lateinit var rvHabilidades : RecyclerView

    private lateinit var avaliacao: Avaliacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.id_avaliacao = it.getString(ID_AVALIACAO)
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

        this.initModels()
        this.initViews(view)
    }

    fun initViews(view: View) {
        this.lbNome = view.findViewById(R.id.nameProfissional)
        this.lbEndereco = view.findViewById(R.id.location)
        this.lbAvaliacao = view.findViewById(R.id.stars)

        this.txtFeedback = view.findViewById(R.id.edit_text)
        this.ratingBar = view.findViewById(R.id.ratingBar)

        this.btnNaoFinalizouServico = view.findViewById(R.id.btnNao)
        this.btnFinalizouServico = view.findViewById(R.id.btnSim)
        this.btnNaoTerminarAvaliacao = view.findViewById(R.id.btnNaoObrigado)
        this.btnTerminarAvaliacao = view.findViewById(R.id.btnPronto)

        this.btnTerminarAvaliacao.isEnabled = false

        this.addValues()
        this.addEventListeners()
    }

    fun addEventListeners() {
        this.btnNaoTerminarAvaliacao.setOnClickListener {  parentFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit() }
        // TODO: Verificar se está tudo correto
        this.btnNaoFinalizouServico.setOnClickListener {
            this.avaliacao.terminado = false
            this.btnTerminarAvaliacao.isEnabled = true
        }
        this.btnFinalizouServico.setOnClickListener {
            this.avaliacao.terminado = true
            this.btnTerminarAvaliacao.isEnabled = true
        }

        this.btnTerminarAvaliacao.setOnClickListener {
            this.avaliacao.comentario = this.txtFeedback.text.toString()
            this.avaliacao.estrelas = this.ratingBar.rating.toDouble()

            this.avaliacaoViewModel.update(this.avaliacao)
        }
    }

    fun addValues() {
        this.lbNome.text = this.nome
        this.lbEndereco.text = this.endereco
        this.lbAvaliacao.text = (this.somaAvaliacoes!!.div(this.numServicosFeitos!!)).toString()

        // TODO: Adicionar o adapter das habilidades
    }

    fun initModels() {
        this.userViewModel.updateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                this.userViewModel.getList()
            }
        }

        this.avaliacaoViewModel.updateLiveData.observe(viewLifecycleOwner) {
            if (it) {
                this.avaliacaoViewModel.getList()
            }
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
         * @param id Service id on firebase.
         * @param nome Name of the worker.
         * @param endereco Worker address.
         * @param somaAvaliacoes Evaluation sum.
         * @param numServicosFeitos Number of done services.
         * @param email Worker email.
         * @return A new instance of fragment WorkerEvaluation.
         */
        @JvmStatic
        fun newInstance(id_avaliacao: String, nome: String, endereco: String,
                        somaAvaliacoes: Double, numServicosFeitos: Long, email: String) =
            WorkerEvaluationFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_AVALIACAO, id_avaliacao)
                    putString(NOME, nome)
                    putString(ENDERECO, endereco)
                    putDouble(SOMA_AVALIACOES, somaAvaliacoes)
                    putLong(NUM_SERVICOES_FEITOS, numServicosFeitos)
                    putString(EMAIL, email)
                }
            }
    }
}