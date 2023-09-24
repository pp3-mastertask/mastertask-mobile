package com.example.mastertask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CardViewAdapter
import com.example.mastertask.Models.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeSearch.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeSearch : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_home_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = ArrayList<User>()
        addItemsToRecyclerViewArrayList(source)

        val adapter = CardViewAdapter(source)

        val recyclerView = view.findViewById(R.id.recycler_view_results) as RecyclerView

         recyclerView.adapter = adapter
         adapter.notifyDataSetChanged()
    }

    fun addItemsToRecyclerViewArrayList(source: ArrayList<User>) {
        source.add(
            User("Marcos", "Campinas - SP", "19984474403", 4.7,
                listOf("Pintura", "Elétrica"))
        )
        source.add(
            User("Cleyton", "Valinhos - SP", "19933452522", 5.0,
                listOf("Enanador", "Mecânico"))
        )
        source.add(
            User("Richard", "Jaguariúna - SP", "19982823482", 3.4,
                listOf("Formatação PC", "Conserto de eletrodomêsticos"))
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeSearch.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeSearch().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}