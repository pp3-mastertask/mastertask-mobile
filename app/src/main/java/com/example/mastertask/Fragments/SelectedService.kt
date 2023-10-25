package com.example.mastertask.Fragments

import HabilidadeAdapter //new
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mastertask.Data.Service
import android.view.textclassifier.SelectionEvent
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Data.User
import com.example.mastertask.Models.HabilidadeViewModel //new
import com.example.mastertask.R
import com.google.firebase.Timestamp
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(ID)
            nome = it.getString(NOME)
            endereco = it.getString(ENDERECO)
            contato = it.getString(CONTATO)
            somaAvaliacoes = it.getDouble(SOMAAVALIACOES)
            numServicosFeitos = it.getLong(NUMSERVICOESFEITOS))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_service, container, false)
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