package com.example.mastertask

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment


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

    lateinit var searchView: SearchView

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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        definirEventListeners(view)
        parentFragmentManager.beginTransaction().replace(R.id.home_tabs_container, HomeInit()).commit()
    }

    fun definirEventListeners(view: View) {
        searchView = view.findViewById(R.id.search_view) as SearchView

        val id =
            searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById<View>(id) as TextView
        textView.setTextColor(Color.DKGRAY)
        textView.setHintTextColor(Color.LTGRAY)

//        val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.)
//
//        searchIcon.setColorFilter(
//            resources.getColor(R.color.text_primary),
//            PorterDuff.Mode.SRC_IN
//        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "") {
                    val s = HomeSearch.newInstance(query)
                    changeScreen(s)
                }
                else
                    changeScreen(HomeInit())
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText != "") {
                    val s = HomeSearch.newInstance(newText)
                    changeScreen(s)
                }
                else
                    changeScreen(HomeInit())
                return true
            }

            fun changeScreen(fragment: Fragment) {
                parentFragmentManager.beginTransaction().replace(R.id.home_tabs_container, fragment).commit()
            }
        })
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