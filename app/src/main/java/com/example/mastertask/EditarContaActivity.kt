package com.example.mastertask

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.EditAccountSkillsAdapter
import com.example.mastertask.Models.SkillModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditarContaActivity : AppCompatActivity() {

    private lateinit var btnAddSkill          : Button
    private lateinit var btnConfirmEditAccount: Button
    private lateinit var btnCancelEditAccount : Button

    private lateinit var txtCpf       : EditText
    private lateinit var txtNascimento: EditText
    private lateinit var txtContato   : EditText
    private lateinit var txtLocalidade: EditText
    private lateinit var dtDisponibilidade: CalendarView

    private var list_skills = ArrayList<SkillModel>()
    private lateinit var recyclerAdapter:EditAccountSkillsAdapter

    private lateinit var received_email: String
    private lateinit var received_name: String

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_conta)

        this.received_email = intent.getStringExtra("email").toString()
        this.received_name  = intent.getStringExtra("name").toString()

        this.configureElements()
        this.configureRecyclerView()

        //this.fillInputs()
    }

    private fun configureElements() {
        this.btnAddSkill           = this.findViewById(R.id.editarConta_btnAddSkill)
        this.btnConfirmEditAccount = this.findViewById(R.id.editarConta_btnConfirmar)
        this.btnCancelEditAccount  = this.findViewById(R.id.editarConta_btnCancelar)

        this.txtCpf        = findViewById(R.id.editarConta_txtCpf)
        this.txtNascimento = findViewById(R.id.editarConta_txtDataNascimento)
        this.txtContato    = findViewById(R.id.editarConta_txtTelefone)
        this.txtLocalidade = findViewById(R.id.editarConta_txtLocalidade)
        this.dtDisponibilidade = findViewById(R.id.editarConta_calendarioDisponibilidade)

        this.setEventListeners()
    }

    private fun configureRecyclerView() {
        this.recyclerAdapter = EditAccountSkillsAdapter(this.list_skills)

        val recyclerView: RecyclerView = findViewById(R.id.editarConta_recyclerView)
        val layoutManager              = LinearLayoutManager(this)
        recyclerView.layoutManager     = layoutManager
        recyclerView.adapter           = this.recyclerAdapter
    }

    private fun setEventListeners() {
        this.btnAddSkill          .setOnClickListener({ this.createAddSkillDialog() })
        this.btnCancelEditAccount .setOnClickListener({ this.handleCancelEditAccount() })
        this.btnConfirmEditAccount.setOnClickListener({ this.handleConfirmEditAccount() })
    }

    private fun createAddSkillDialog() {
        val dg: Dialog = Dialog(this)

        dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dg.setCancelable(true)
        dg.setContentView(R.layout.add_skill_dialog)

        val txtNomeSkill: EditText = dg.findViewById(R.id.add_skill_name)
        val txtPrecoSkill: EditText = dg.findViewById(R.id.add_skill_price)
        val btnCancelar: Button = dg.findViewById(R.id.add_skill_cancel)
        val btnAdicionarSkill: Button = dg.findViewById(R.id.add_skill_confirm)

        btnCancelar.setOnClickListener({ dg.cancel() })

        btnAdicionarSkill.setOnClickListener {
            this.list_skills.add(SkillModel(
                txtNomeSkill.text.toString(),
                txtPrecoSkill.text.toString().toDouble()
            ))

            this.recyclerAdapter.notifyDataSetChanged()

            txtNomeSkill.text.clear()
            txtPrecoSkill.text.clear()
        }

        dg.show()
    }

    private fun handleCancelEditAccount() {
        // redirecionar para a primeira pagina de edicao de usuario
        this.finish()
    }

    private fun handleConfirmEditAccount() {
        Toast.makeText(this, this.dtDisponibilidade.date.toString(), Toast.LENGTH_LONG).show()
        val userUpdatedData = hashMapOf(
            "contato" to this.txtContato.text.toString(),
            "cpf" to this.txtCpf.text.toString(),
            "dataNascimento" to this.txtNascimento.text.toString(),
            "disponibilidade" to this.dtDisponibilidade.date.toString(),
            "endereco" to this.txtLocalidade.text.toString(),
            "habilidades" to null,
            "mediaAtual" to 0.0,
            "nome" to this.auth.currentUser!!.displayName,
            "numServicosFeitos" to 0
        )

        this.db.collection("usuarios")
            .document(this.auth.currentUser!!.email.toString())
            .update(userUpdatedData as Map<String, Any?>)
            .addOnSuccessListener {
                Toast.makeText(this, "Mudanças feitas com sucesso!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Não foi possível atualizar o perfil!", Toast.LENGTH_LONG).show()
            }
    }

    private fun fillInputs() {
        // testar se o usuario tem dados, se tiver, preencher os inputs
    }
}