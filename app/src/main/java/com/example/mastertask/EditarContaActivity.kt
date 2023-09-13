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
import com.example.mastertask.Adapters.EditAccountSkillsAdapter
import com.example.mastertask.Models.SkillModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class EditarContaActivity : AppCompatActivity() {

    private lateinit var btnAddSkill          : Button
    private lateinit var btnConfirmEditAccount: Button
    private lateinit var btnCancelEditAccount : Button

    private lateinit var txtCpf       : EditText
    private lateinit var txtNascimento: EditText
    private lateinit var txtContato   : EditText
    private lateinit var txtLocalidade: EditText
    private lateinit var dtDisponibilidade: EditText

    private lateinit var lbAuthTest: TextView

    private var currentUser = FirebaseAuth.getInstance().currentUser

    private var list_skills = ArrayList<SkillModel>()
    private lateinit var recyclerAdapter:EditAccountSkillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_conta)

        val email = intent.getStringExtra("email")

        lbAuthTest = findViewById(R.id.txtTesteAuth)
        lbAuthTest.text = email

        this.configureElements()
        this.configureRecyclerView()

        this.fillInputs()
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
        this.btnAddSkill          .setOnClickListener({ createAddSkillDialog() })
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
    }

    private fun handleConfirmEditAccount() {
        val userUpdatedData = hashMapOf(
            "contato" to txtContato.text.toString(),
            "cpf" to txtCpf.text.toString(),
            "dataNascimento" to null,
            "disponibilidade" to "",
            "endereco" to "",
            "habilidades" to null,
            "mediaAtual" to 0.0,
            "nome" to "",
            "numServicosFeitos" to 0
        )
    }

    private fun fillInputs() {
        // testar se o usuario tem dados, se tiver, preencher os inputs
    }
}