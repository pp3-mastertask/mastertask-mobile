package com.example.mastertask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var lbTeste : TextView

    val db = Firebase.firestore

    lateinit var btnGoogleAuth: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGoogleAuth = this.findViewById(R.id.main_btnGoogleAuth)

        btnGoogleAuth.setOnClickListener({
            val intent = Intent(this, CriarContaActivity::class.java)
            startActivity(intent)
        })

        //lbTeste = findViewById(R.id.lbTeste)

//        db.collection("usuarios").get().addOnSuccessListener {
//            result ->
//            for (document in result) {
//                lbTeste.text = lbTeste.text.toString().plus("${document.data}")
//            }
//        }
    }
}