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

class EditarContaActivity : AppCompatActivity() {

    private lateinit var btnAddSkill: Button

    private lateinit var lbAuthTest: TextView

    private var list_skills = ArrayList<SkillModel>()
    private lateinit var recyclerAdapter:EditAccountSkillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_conta)

        val email = intent.getStringExtra("email")

        lbAuthTest = findViewById(R.id.txtTesteAuth)
        lbAuthTest.text = email

        this.btnAddSkill = this.findViewById(R.id.criarConta_addSkill)
        btnAddSkill.setOnClickListener({ createAddSkillDialog() })

        this.recyclerAdapter = EditAccountSkillsAdapter(this.list_skills)

        val recyclerView: RecyclerView = findViewById(R.id.criarConta_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
    }

    private fun createAddSkillDialog() {
        val dg: Dialog = Dialog(this)

        dg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dg.setCancelable(true)
        dg.setContentView(R.layout.add_skill_dialog)

        val txtNomeSkill: EditText = dg.findViewById(R.id.add_skill_name)
        val txtPrecoSkill: EditText = dg.findViewById(R.id.add_skill_price)
        val btnCancelar: Button = dg.findViewById(R.id.add_service_cancel)
        val btnAdicionarSkill: Button = dg.findViewById(R.id.add_service_confirm)

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
}