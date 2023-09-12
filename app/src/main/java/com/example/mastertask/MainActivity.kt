package com.example.mastertask

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val db = Firebase.firestore

    lateinit var btnGoogleAuth: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("204806293004-5u8uugk78i2s86qvhjqo1jrviucr0g05.apps.googleusercontent.com")
            .requestEmail()
            .build()

        Log.d("AUTHENTICATION", gso.serverClientId.toString())
        this.googleSignInClient = GoogleSignIn.getClient(this, gso)

        this.btnGoogleAuth = findViewById(R.id.main_btnGoogleAuth)

        this.btnGoogleAuth.setOnClickListener {
            this.authenticateWithGoogle()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            this.handleAuthenticationResult(task)
        }
    }

    private fun authenticateWithGoogle() {
        val authIntent = this.googleSignInClient.signInIntent
        this.launcher.launch(authIntent)
    }

    private fun handleAuthenticationResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                //this.insertAccountIntoDatabase(account)
                this.handleShowHomePage(account)
            }
        }
        else
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun insertAccountIntoDatabase(account: GoogleSignInAccount) {
        db.collection("usuarios").get().addOnSuccessListener {
            result ->
            for (document in result) {
                //lbTeste.text = lbTeste.text.toString().plus("${document.data}")
            }
        }
    }

    private fun handleShowHomePage(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        this.auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, CriarContaActivity::class.java)
                intent.putExtra("email", account.email)
                startActivity(intent)
            }
        }
    }
}