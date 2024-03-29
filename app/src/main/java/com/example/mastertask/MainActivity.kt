package com.example.mastertask

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

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

        this.googleSignInClient = GoogleSignIn.getClient(this, gso)

        this.btnGoogleAuth = findViewById(R.id.main_btnGoogleAuth)
        btnGoogleAuth.isEnabled = false

        if (auth.currentUser != null) {
            this.db.collection("usuarios")
                .document(auth.currentUser!!.email.toString()).get().addOnSuccessListener {
                    if (it.data!!["cpf"] != "") {
                        val intent = Intent(this, Application::class.java)
                        startActivity(intent)
                        this.finish()
                    }
                    else {
                        auth.signOut()
                        googleSignInClient.signOut()
                    }
                    btnGoogleAuth.isEnabled = true
                }
        }
        else {
            btnGoogleAuth.isEnabled = true
        }

        this.btnGoogleAuth.setOnClickListener {
            this.authenticateWithGoogle()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // User selected (ok) an email
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            this.handleAuthenticationResult(task)
        }
    }

    private fun authenticateWithGoogle() {
        // Initialize google intent for select email
        val authIntent = this.googleSignInClient.signInIntent
        this.launcher.launch(authIntent)
    }

    private fun handleAuthenticationResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            // Get the account, insert to database and go to home page
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                this.db.collection("usuarios")
                    .document(account.email.toString()).get().addOnSuccessListener {
                        if (it.data != null) {
                            if (it.data!!["cpf"] == "") {
                                this.handleShowPage(account, EditarContaActivity())
                            }
                            else {
                                this.handleShowPage(account, Application())
                            }
                        }
                        else {
                            this.insertAccountIntoDatabase(account)
                            this.handleShowPage(account, EditarContaActivity())
                        }
                    }
            }
        }
        else
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun insertAccountIntoDatabase(account: GoogleSignInAccount) {
        try {
            val createdUserData = hashMapOf(
                "cep" to "",
                "contato" to "",
                "cpf" to "",
                "dataInicio" to Timestamp.now(),
                "dataNascimento" to Timestamp(-1, 0),
                "imagem" to account.photoUrl.toString(),
                "endereco" to "",
                "habilidades" to ArrayList<HashMap<*,*>>(),
                "numeroResidencia" to 0,
                "somaAvaliacoes" to 0.0,
                "nome" to account.displayName.toString(),
                "numServicosFeitos" to 0
            )

            this.db.collection("usuarios")
                .document(account.email.toString())
                .set(createdUserData)
        }
        catch (error: Exception) {
            Toast.makeText(this, "Um erro ocorreu ao cadastrar o usuário", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleShowPage(account: GoogleSignInAccount, activity: AppCompatActivity) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        this.auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, activity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }
}