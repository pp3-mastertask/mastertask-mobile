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
    private lateinit var txtNascimento: EditText
    private lateinit var txtContato   : EditText
    private lateinit var txtLocalidade: EditText
    private lateinit var dtDisponibilidade: CalendarView

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
        val btnCancelAddSkill: Button = dg.findViewById(R.id.add_skill_cancel)
        val btnAdicionarSkill: Button = dg.findViewById(R.id.add_skill_confirm)

        btnCancelAddSkill.setOnClickListener({
            dg.cancel()
        })

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
            || this.txtNascimento.text.toString() == ""
            || this.dtDisponibilidade.date.toString() == ""
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

        val timestampList = ArrayList<Timestamp>()
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, this.dtDisponibilidade.date.toString(), Toast.LENGTH_SHORT).show()
        timestampList.add(Timestamp(TimeUnit.SECONDS.toSeconds(this.dtDisponibilidade.date), 0))
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show()
        val userUpdatedData = hashMapOf(
            "contato" to this.txtContato.text.toString(),
            "cpf" to this.txtCpf.text.toString(),
            "dataInicio" to Timestamp.now(),
            "dataNascimento" to Timestamp(this.txtNascimento.text.toString().toLong(), 0),
            "disponibilidade" to timestampList,
            "endereco" to this.txtLocalidade.text.toString(),
            "habilidades" to skillsHashMapList,
            "somaAvaliacoes" to 0.0,
            "nome" to this.auth.currentUser!!.displayName,
            "numServicosFeitos" to 0
        )
        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show()

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
        val currentUserEmail: String = this.auth.currentUser!!.email.toString()

        var currentUserData: Map<String, Any>? = null

        this.db.collection("usuarios")
            .document(currentUserEmail)
            .get()
            .addOnSuccessListener { it ->
                if (it.exists()) {
                    currentUserData = it.data

                    if (currentUserData != null) {
                        Toast.makeText(this, currentUserData.toString(), Toast.LENGTH_SHORT).show()
                        for ((k, v) in currentUserData!!) {
                            Toast.makeText(this, k.toString(), Toast.LENGTH_LONG).show()
//                            when (k) {
//                                "contato" -> this.txtContato.setText(v.toString())
//                                "cpf" -> this.txtCpf.setText(v.toString())
//                                "dataNascimento" -> this.txtNascimento.setText(v.toString())
//                                "disponibilidade" -> {
//                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//
//                                    try {
//                                        // Parse the timestamp string into a Date object
//                                        val date = dateFormat.parse(v.toString())
//
//                                        // Convert the Date to milliseconds since the epoch
//                                        val timeInMillis = date.time
//
//                                        // Set the selected date in the CalendarView
//                                        this.dtDisponibilidade.date = timeInMillis
//                                    } catch (e: Exception) {
//                                        Toast.makeText(this, "Não foi possível carregar a data de disponibilidade!", Toast.LENGTH_LONG).show()
//                                    }
//                                }
//                                "endereco" -> this.txtLocalidade.setText(v.toString())
//                                "habilidades" -> {
//                                    val skillsHashMapList = v as ArrayList<Any>
//
//                                    for (hashMap in skillsHashMapList) {
//                                        if (hashMap is HashMap<*, *>) {
//                                            this.list_skills.add(
//                                                SkillModel(
//                                                    hashMap["habilidade"]?.toString() ?: "",
//                                                    hashMap["preco"]?.toString()?.toDouble() ?: 0.0
//                                                )
//                                            )
//
//                                            this.recyclerAdapter.notifyDataSetChanged()
//                                        }
//                                    }
//                                }
//                            }
                        }
                    }
                }
            }
    }
}