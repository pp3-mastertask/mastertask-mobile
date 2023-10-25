package com.example.mastertask.Fragments

import HabilidadeAdapter //new
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Models.HabilidadeViewModel //new
import com.example.mastertask.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedService.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedService : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var habilidadeAdapter: HabilidadeAdapter
    private lateinit var recyclerViewHabilidades: RecyclerView
    private val habilidadeViewModel: HabilidadeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    interface OnHabilidadeSelecterdListener{
        fun onHabilidadeSelected(habilidade: String)
    }

    private var habilidadeListener: OnHabilidadeSelecterdListener? = null

    override fun onCardClick(param1: String){
        val selectedServiceFragment = SelectedService.newInstance(param1)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, param1)
            //.addToBackStack(null)
            .commit()
    }

   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {
       val view = inflater.inflate(R.layout.fragment_selected_service, container, false)
       recyclerViewHabilidades = view.findViewById(R.id.recycler_view_skills_available)

       //adaptador para lista de habilidades la e o listener
       habilidadeAdapter = HabilidadeAdapter {habilidade ->
           habilidadeListener?.onHabilidadeSelected(habilidade)
       }
       recyclerViewHabilidades.adapter = habilidadeAdapter

       //carrega a lista de habilidade usando o viewmodel
       habilidadeViewModel.getHabilidades().observe(viewLifecycleOwner) {habilidades ->
           habilidadeAdapter.submitList(habilidades)
       }

       return view

       // Inflate the layout for this fragment
       //return inflater.inflate(R.layout.fragment_selected_service, container, false)
   }

    fun setOnHabilidadeSelectedListener(listener: OnHabilidadeSelecterdListener){
        habilidadeListener = listener
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
        fun newInstance(param1: String, param2: String) =
            SelectedService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}