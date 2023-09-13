package com.example.mastertask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeInit.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeInit : Fragment() {
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
        val rootView: View = inflater.inflate(R.layout.fragment_home, container, false)

        val source = ArrayList<User>()
        addItemsToRecyclerViewArrayList(source)
//        setUpRecyclerView(rootView, R.id.recycler_view_novos, source)
//        setUpRecyclerView(rootView, R.id.recycler_view_recomendacoes, source)
//        setUpRecyclerView(rootView, R.id.recycler_view_jacontratados, source)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_init, container, false)
    }

    fun setUpRecyclerView(rootView: View, id: Int, source: ArrayList<User>) {
        val horizontalLayout = LinearLayoutManager(activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL, false)
        val adapter = CardViewAdapter(source)

        val recyclerView : RecyclerView = rootView.findViewById(id)
        recyclerView.layoutManager = horizontalLayout
        recyclerView.adapter = adapter
    }

    fun addItemsToRecyclerViewArrayList(source: ArrayList<User>) {
        source.add(User("Marcos", "Campinas - SP", "19984474403", 4.7,
            listOf("Pintura", "Elétrica")))
        source.add(User("Cleyton", "Valinhos - SP", "19933452522", 5.0,
            listOf("Enanador", "Mecânico")))
        source.add(User("Richard", "Jaguariúna - SP", "19982823482", 3.4,
            listOf("Formatação PC", "Conserto de eletrodomêsticos")))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeInit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeInit().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}