package com.example.mastertask

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var lbTeste : TextView

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lbTeste = findViewById(R.id.lbTeste)

        db.collection("usuarios").get().addOnSuccessListener {
            result ->
            for (document in result) {
                lbTeste.text = lbTeste.text.toString().plus("${document.data}")
            }
        }

        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }
}