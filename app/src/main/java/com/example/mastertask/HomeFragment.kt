package com.example.mastertask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

        val recyclerView : RecyclerView = rootView.findViewById(R.id.recycler_view_novos)
        val recyclerViewLayoutManager : RecyclerView.LayoutManager =
            LinearLayoutManager(activity?.applicationContext)

        recyclerView.layoutManager = recyclerViewLayoutManager

        val source = ArrayList<User>()
        AddItemsToRecyclerViewArrayList(source)

        val horizontalLayout = LinearLayoutManager(activity?.applicationContext,
            LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayout

        val adapter = RecyclerViewAdapter(source)
        recyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // Function to add items in RecyclerView.
    fun AddItemsToRecyclerViewArrayList(source: ArrayList<User>) {
        source.add(User("Marcos", "Campinas - SP", "19984474403", 4.7))
        source.add(User("Cleyton", "Valinhos - SP", "19933452522", 5.0))
        source.add(User("Richard", "Jaguariúna - SP", "19982823482", 3.4))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}