package com.example.mastertask

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mastertask.Adapters.EditAccountSkillsAdapter
import com.example.mastertask.Models.SkillModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.Document
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class EditarContaActivity : AppCompatActivity() {

    private lateinit var btnAddSkill          : Button
    private lateinit var btnConfirmEditAccount: Button
    private lateinit var btnCancelEditAccount : Button

    private lateinit var txtCpf       : EditText
    private lateinit var dtNascimento: CalendarView
    private lateinit var txtContato   : EditText
    private lateinit var txtLocalidade: EditText

    private var list_skills = ArrayList<SkillModel>()
    private lateinit var recyclerAdapter:EditAccountSkillsAdapter

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_conta)

        this.configureElements()
        this.configureRecyclerView()

        this.fillInputs()
    }

    private fun configureElements() {
        this.btnAddSkill           = this.findViewById(R.id.editarConta_btnAddSkill)
        this.btnConfirmEditAccount = this.findViewById(R.id.editarConta_btnConfirmar)
        this.btnCancelEditAccount  = this.findViewById(R.id.editarConta_btnCancelar)

        this.txtCpf        = findViewById(R.id.editarConta_txtCpf)
        this.dtNascimento = findViewById(R.id.editarConta_calendarioDataNascimento)
        this.txtContato    = findViewById(R.id.editarConta_txtTelefone)
        this.txtLocalidade = findViewById(R.id.editarConta_txtLocalidade)

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

        this.txtCpf.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 3 && (s?.length ?: 0) <= 11) {
                    val formattedText = formatString(s.toString(), "CPF")
                    txtCpf.removeTextChangedListener(this) // Remover novamente o evento
                    txtCpf.setText(formattedText)
                    txtCpf.setSelection(formattedText.length) // Colocar o cursor no fim
                    txtCpf.addTextChangedListener(this) // Adicionar novamente o evento
                }
            }
        })

        this.txtContato.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 3 && (s?.length ?: 0) <= 11) {
                    val formattedText = formatString(s.toString(), "CONTACT")
                    txtContato.removeTextChangedListener(this) // Remover novamente o evento
                    txtContato.setText(formattedText)
                    txtContato.setSelection(formattedText.length) // Colocar o cursor no fim
                    txtContato.addTextChangedListener(this) // Adicionar novamente o evento
                }
            }
        })
    }

    private fun formatString(input: String, type: String): String {
        val stripped = input.replace("[^0-9]".toRegex(), "") // Remove non-numeric characters
        val formatted = buildString {
            for (i in stripped.indices) {
                when (type) {
                    "CPF" -> {
                        if (i % 3 == 0 && i > 0) {
                            append('.')
                        }
                        if (i == 9) {
                            append('-')
                        }
                    }
                    "CONTACT" -> {
                        if (i == 0) {
                            append('(')
                        } else if (i == 2) {
                            append(')')
                        } else if (i == 7 || i == 11) {
                            append('-')
                        }
                    }
                }
                append(stripped[i])
            }
        }
        return formatted
    }

    private fun createAddSkillDialog() {
        val dg: Dialog = Dialog(this)

        dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dg.setCancelable(true)
        dg.setContentView(R.layout.add_skill_dialog)

        val txtNomeSkill: EditText = dg.findViewById(R.id.add_skill_name)
        val txtPrecoSkill: EditText = dg.findViewById(R.id.add_skill_price)
        val btnCancelAddSkill: Button = dg.findViewById(R.id.add_skill_cancel)
        val btnAdicionarSkill: Button = dg.findViewById(R.id.add_skill_confirm)

        btnCancelAddSkill.setOnClickListener{ dg.cancel() }

        btnAdicionarSkill.setOnClickListener {
            if (txtNomeSkill.text.toString() != "" && txtPrecoSkill.text.toString() != "") {
                this.list_skills.add(
                    SkillModel(
                        txtNomeSkill.text.toString(),
                        txtPrecoSkill.text.toString().toDouble()
                    )
                )

                this.recyclerAdapter.notifyDataSetChanged()

                txtNomeSkill.text.clear()
                txtPrecoSkill.text.clear()
            }
        }

        dg.show()
    }

    private fun handleCancelEditAccount() {
        if (this.areFieldsEmpty()) {
            val dg: Dialog = Dialog(this)

            dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dg.setCancelable(true)
            dg.setContentView(R.layout.cancel_edit_account_dialog)
            dg.show()
        }
        else
            this.finish()
    }

    private fun areFieldsEmpty(): Boolean {
        if (this.txtContato.text.toString() == ""
            || this.txtCpf.text.toString() == ""
            || this.dtNascimento.date.toString() == ""
            || this.txtLocalidade.text.toString()  == "") {
            return true
        }

        return false
    }

    private fun handleConfirmEditAccount() {

        if (this.areFieldsEmpty()) {
            val dg: Dialog = Dialog(this)

            dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dg.setCancelable(true)
            dg.setContentView(R.layout.cancel_edit_account_dialog)
            dg.show()
            return
        }

        val skillsHashMapList = ArrayList<Any>()
        // Para cada skill adicionada, vamos criar um hashmap, inserí-lo em um array e
        // adicionar esse array no document do usuario
        this.list_skills.forEach {
            val data = hashMapOf (
                "habilidade" to it.nome,
                "preco" to it.preco
            )

            skillsHashMapList.add(data)
        }

        val userUpdatedData = hashMapOf(
            "contato" to this.txtContato.text.toString(),
            "cpf" to this.txtCpf.text.toString(),
            "dataInicio" to Timestamp.now(),
            "dataNascimento" to Timestamp(Date(dtNascimento.date)),
            "disponibilidade" to ArrayList<Timestamp>(),
            "endereco" to this.txtLocalidade.text.toString(),
            "habilidades" to skillsHashMapList,
            "somaAvaliacoes" to 0.0,
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

        val intent = Intent(this, Application::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun fillInputs() {
        val currentUserEmail: String = this.auth.currentUser!!.email.toString()

        var currentUserData: Map<String, Any>? = null

        this.db.collection("usuarios")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    currentUserData = it.data

                    if (currentUserData != null) {
                        for ((k, v) in currentUserData!!) {
                            when (k) {
                                "contato" -> this.txtContato.setText(v.toString())
                                "cpf" -> this.txtCpf.setText(v.toString())
                                "endereco" -> this.txtLocalidade.setText(v.toString())
                                "habilidades" -> {
                                    val skillsHashMapList = v as ArrayList<Any>

                                    for (hashMap in skillsHashMapList) {
                                        if (hashMap is HashMap<*, *>) {
                                            this.list_skills.add(
                                                SkillModel(
                                                    hashMap["habilidade"]?.toString() ?: "",
                                                    hashMap["preco"]?.toString()?.toDouble() ?: 0.0
                                                )
                                            )

                                            this.recyclerAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}