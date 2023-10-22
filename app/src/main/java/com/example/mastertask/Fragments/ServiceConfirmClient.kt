package com.example.mastertask.Fragments
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import com.example.mastertask.Data.Service
//import com.example.mastertask.Data.User
//import com.example.mastertask.Models.HabilidadeViewModel
//import com.example.mastertask.Models.ServiceViewModel
//import com.example.mastertask.Models.UserViewModel
//import com.example.mastertask.R
//import com.google.firebase.auth.FirebaseAuth
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [ServiceConfirmClient.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ServiceConfirmClient : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null //nao estarei apagando por enquantokkkk ainda falta mt coisa aq nekkkk
//    private var param2: String? = null //paciencia
//
//    private lateinit var userViewModel: UserViewModel
//    private lateinit var serviceViewModel: HabilidadeViewModel
//
//    private lateinit var userViewModel: UserViewModel
//    private lateinit var serviceViewModel: ServiceViewModel
//    private lateinit var auth: FirebaseAuth
//
//    private lateinit var selectedServices: List<Service>
//    private lateinit var currentUser: User
//
//    private lateinit var textViewCliente: //eu n sei como pega essas infos aqkkkkkk
//    private lateinit var textViewTotalPrice://eu n sei como pega essas infos aqkkkkkk
//    private lateinit var buttonSolicitar://eu n sei como pega essas infos aqkkkkkk
//
//    val currentUser = auth.currentUser
//    val clienteId = currentUser?.uid
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_service_confirm_client, container, false)
//
//
//        userViewModel = UserViewModel()
//        serviceViewModel = ServiceViewModel()
//        auth = FirebaseAuth.getInstance()
//
//        val currentUserEmail = auth.currentUser?.email
//        currentUser = userViewModel.getUserByEmail(currentUserEmail) ?: User()
//
//
//        selectedServices = // eu realmente n achei nada sobre como fazkkkk vou procurar mais um pouco ainddakkkkkkkk
//
//        //inicializa as views
//        textViewCliente = view.findViewById(R.id.text_cliente) //n lembro como chamakkkkk ja ja eu olhokkk
//        textViewTotalPrice = view.findViewById(R.id.text_total_price)
//        buttonSolicitar = view.findViewById(R.id.button_solicitar)
//
//        setupUI()//inicializa a interface do usuario???
//
//        buttonSolicitar.setOnClickListener {
//            criarServico()
//        }
//        return inflater.inflate(R.layout.fragment_service_confirm_client, container, false)
//    }
//
//    private fun setupUI() {
//        textViewCliente.text = "Cliente: ${currentUser.nome}"
//
//        val totalPrice = calcularPrecoTotal(selectedServices)
//        textViewTotalPrice.text = "Total a pagar: R$ $totalPrice"
//    }
//
//    private fun calcularPrecoTotal(selectedServices: List<Service>): Double {
//        var totalPrice = 0.0
//        for (service in selectedServices) {
//            totalPrice += service.preco //eu tenho q criar o preço em services??
//        }
//        return totalPrice
//    }
//
//    private fun criarServico() {
//        if(selectedServices.isNotEmpty()){
//            val novoSercivo = Service(
//                clienteId = currentUser.id,
//                clienteId = currentUser.id, servicos = selectedServices.map {it.id} //nada da certo nessa buceta aqkkkkk eu heinkkkk
//            )
//            serviceViewModel.createService(novoSercivo)
//
//            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container,HomeInit())?.commit()
//        }
//        else{
//
//        }
//    }
//}
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ServiceConfirm.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ServiceConfirmClient().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}