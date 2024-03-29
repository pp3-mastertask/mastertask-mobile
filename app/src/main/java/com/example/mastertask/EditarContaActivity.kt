package com.example.mastertask

import android.app.Dialog
import android.content.Intent
import android.icu.util.Calendar
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
import androidx.recyclerview.widget.RecyclerView
import br.com.colman.simplecpfvalidator.isCpf
import com.example.mastertask.Adapters.EditAccountSkillsAdapter
import com.example.mastertask.Data.Responses.CepResponse
import com.example.mastertask.Models.SkillModel
import com.example.mastertask.Services.ViaCepService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditarContaActivity : AppCompatActivity() {

    private lateinit var btnAddSkill          : Button
    private lateinit var btnConfirmEditAccount: Button
    private lateinit var btnCancelEditAccount : Button

    private lateinit var txtCpf       : EditText
    private lateinit var dtNascimento: CalendarView
    private lateinit var txtContato   : EditText
    private lateinit var txtCEP: EditText
    private lateinit var txtNumero: EditText
    private lateinit var lbLogradouro: TextView

    private var list_skills = ArrayList<SkillModel>()
    private lateinit var recyclerAdapter:EditAccountSkillsAdapter

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private var selectedDate : Timestamp = Timestamp.now()

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
        this.txtCEP = findViewById(R.id.editarConta_txtCEP)
        this.txtNumero = findViewById(R.id.editarConta_txtNumero)
        this.lbLogradouro = findViewById(R.id.editarConta_lbLogradouro)

        this.setEventListeners()
    }

    private fun configureRecyclerView() {
        this.recyclerAdapter = EditAccountSkillsAdapter(this.list_skills, object :
            EditAccountSkillsAdapter.OnBadgeClickListener {
                override fun onBadgeClick (badge: SkillModel, position: Int) {
                    val dg: Dialog = Dialog(this@EditarContaActivity)

                    dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dg.setCancelable(true)
                    dg.setContentView(R.layout.add_skill_dialog)

                    val txtNomeSkill: EditText = dg.findViewById(R.id.add_skill_name)
                    val txtPrecoSkill: EditText = dg.findViewById(R.id.add_skill_price)
                    val btnCancelAddSkill: Button = dg.findViewById(R.id.add_skill_cancel)
                    val btnAdicionarSkill: Button = dg.findViewById(R.id.add_skill_confirm)

                    txtNomeSkill.setText(badge.nome)
                    txtPrecoSkill.setText(badge.preco.toString())

                    btnCancelAddSkill.setOnClickListener{ dg.cancel() }

                    btnAdicionarSkill.text = "Editar"
                    btnAdicionarSkill.setOnClickListener {
                        if (txtNomeSkill.text.toString() != "" && txtPrecoSkill.text.toString() != "") {
                            list_skills[position] = SkillModel(txtNomeSkill.text.toString(), txtPrecoSkill.text.toString().toDouble())
                        }

                        this@EditarContaActivity.recyclerAdapter.notifyDataSetChanged()

                        dg.cancel()
                    }
                    dg.show()
                }
            }
        )

        val recyclerView: RecyclerView = findViewById(R.id.editarConta_recyclerView)
        recyclerView.adapter           = this.recyclerAdapter
    }

    private fun setEventListeners() {
        this.btnAddSkill          .setOnClickListener({ this.createAddSkillDialog() })
        this.btnCancelEditAccount .setOnClickListener({ this.handleCancelEditAccount() })
        this.btnConfirmEditAccount.setOnClickListener({ this.handleConfirmEditAccount() })
        this.dtNascimento.setOnDateChangeListener(object: CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, day: Int) {
                val c = java.util.Calendar.getInstance()
                c.set(year, month, day)
                selectedDate = Timestamp(Date(c.timeInMillis))
            }
        })

        this.txtCpf.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 3 && (s?.length ?: 0) <= 13) {
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
                if ((s?.length ?: 0) >= 2 && (s?.length ?: 0) <= 11) {
                    val formattedText = formatString(s.toString(), "CONTACT")
                    txtContato.removeTextChangedListener(this)
                    txtContato.setText(formattedText)
                    txtContato.setSelection(formattedText.length)
                    txtContato.addTextChangedListener(this)
                }
            }
        })

        this.txtCEP.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 5 && (s?.length ?: 0) <= 8) {
                    val formattedText = formatString(s.toString(), "CEP")
                    txtCEP.removeTextChangedListener(this)
                    txtCEP.setText(formattedText)
                    txtCEP.setSelection(formattedText.length)
                    txtCEP.addTextChangedListener(this)
                }

                if (s != null)
                    if (s.length == 9) {
                        var cep = s.toString().replace("-", "")
                        isValidCEP(cep) {
                            if (it != null) {
                                val enderecoFinalString = it.logradouro + " - " + it.bairro + ", " +
                                        it.localidade + " - " + it.uf
                                lbLogradouro.text = "Logradouro: " + enderecoFinalString
                            } else {
                                Toast.makeText(this@EditarContaActivity,
                                    "Erro ao consultar o CEP informado", Toast.LENGTH_SHORT).show()
                            }
                        }
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
                        if (i % 3 == 0 && i > 0 && i != 9) {
                            append('.')
                        }
                        if (i == 9 || i == 10) {
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
                    "CEP" -> {
                        if (i == 5) {
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
        this.finish()
    }

    private fun areFieldsEmpty(): Boolean {
        if (this.txtContato.text.toString() == ""
            || this.txtCpf.text.toString() == ""
            || this.dtNascimento.date.toString() == ""
            || this.txtCEP.text.toString()  == ""
            || this.txtNumero.text.toString() == "") {
            return true
        }

        return false
    }

    private fun isValidCEP(cep: String, callback: (CepResponse?) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val viaCepService = retrofit.create(ViaCepService::class.java)
        val call = viaCepService.getCep(cep)

        call.enqueue(object: Callback<CepResponse> {
            override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
                if (response.isSuccessful) {
                    val cepData = response.body()
                    if (cepData?.logradouro != null)
                        callback(cepData)
                    else
                        callback(null)
                }
                else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<CepResponse>, t: Throwable) {
                callback(null)
            }
        })
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

        // Verificar CPF
        val cpf = this.txtCpf.text.toString()
        if (!cpf.isCpf(charactersToIgnore = listOf('.', '-'))) {
            Toast.makeText(this, "O CPF digitado não é válido!", Toast.LENGTH_LONG).show()
            return
        }

        // Verificar CEP
        var cep = this.txtCEP.text.toString().replace("-", "")
        var enderecoFinalString = ""
        isValidCEP(cep) {
            if (it != null) {
                val enderecoFinalString = it.logradouro + " - " + it.bairro + ", " +
                        it.localidade + " - " + it.uf
                lbLogradouro.text = "Logradouro: " + enderecoFinalString
            }
            else {
                Toast.makeText(this, "Erro ao consultar o CEP informado", Toast.LENGTH_LONG).show()
            }
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
            "cep" to this.txtCEP.text.toString(),
            "contato" to this.txtContato.text.toString(),
            "cpf" to cpf,
            "dataInicio" to Timestamp.now(),
            "dataNascimento" to selectedDate,
            "imagem" to this.auth.currentUser!!.photoUrl.toString(),
            "endereco" to this.lbLogradouro.text.toString().removePrefix("Logradouro: "),
            "habilidades" to skillsHashMapList,
            "numeroResidencia" to this.txtNumero.text.toString().toLong(),
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
                                "cep" -> this.txtCEP.setText(v.toString())
                                "contato" -> this.txtContato.setText(v.toString())
                                "cpf" -> this.txtCpf.setText(v.toString())
                                "dataNascimento" -> this.dtNascimento.setDate((v as Timestamp).toDate().time)
                                "endereco" -> this.lbLogradouro.setText("Logradouro: " + v.toString())
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
                                "numeroResidencia" -> this.txtNumero.setText(v.toString())
                            }
                        }
                    }
                }
            }
    }
}