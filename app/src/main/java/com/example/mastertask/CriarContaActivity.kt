package com.example.mastertask

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.CreateAccountServicesAdapter
import com.example.mastertask.Models.ServiceModel

class CriarContaActivity : AppCompatActivity() {

    private lateinit var btnAddService: Button

    private lateinit var lbAuthTest: TextView

    private var list_servicos = ArrayList<ServiceModel>()
    private lateinit var recyclerAdapter:CreateAccountServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        val email = intent.getStringExtra("email")

        lbAuthTest = findViewById(R.id.txtTesteAuth)
        lbAuthTest.text = email

        this.btnAddService = this.findViewById(R.id.criarConta_addService)
        btnAddService.setOnClickListener({ createAddServiceDialog() })

        this.recyclerAdapter = CreateAccountServicesAdapter(this.list_servicos)

        val recyclerView: RecyclerView = findViewById(R.id.criarConta_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
    }

    private fun createAddServiceDialog() {
        val dg: Dialog = Dialog(this)

        dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dg.setCancelable(true)
        dg.setContentView(R.layout.add_service_dialog)

        val txtNomeServico: EditText = dg.findViewById(R.id.add_service_name)
        val txtPrecoServico: EditText = dg.findViewById(R.id.add_service_price)
        val btnCancelar: Button = dg.findViewById(R.id.add_service_cancel)
        val btnAdicionarServico: Button = dg.findViewById(R.id.add_service_confirm)

        btnCancelar.setOnClickListener({ dg.cancel() })

        btnAdicionarServico.setOnClickListener {
            this.list_servicos.add(ServiceModel(
                txtNomeServico.text.toString(),
                txtPrecoServico.text.toString().toDouble()
            ))

            this.recyclerAdapter.notifyDataSetChanged()

            txtNomeServico.text.clear()
            txtPrecoServico.text.clear()
        }

        dg.show()
    }
}