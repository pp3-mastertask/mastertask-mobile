package com.example.mastertask.Models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastertask.Data.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val users = "usuarios"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }

    val getItemLiveData : MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(user: User) {
        val docRef = db.collection(users)
        docRef.add(user.toMap()).addOnSuccessListener {
            createLiveData.setValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(user: User) {
        val docRef = db.collection(users)
        docRef.document(user.id!!).update(user.toMap()).addOnSuccessListener {
            updateLiveData.setValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(users)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.setValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(users)
        docRef.get().addOnSuccessListener {
            val users = ArrayList<User>()
            for (item in it.documents) {
                val user = User()
                user.id = item.id
                user.contato = item.data!!["contato"] as String?
                user.cpf = item.data!!["cpf"] as String?
                user.dataInicio = item.data!!["dataInicio"] as Timestamp?
                user.dataNascimento = item.data!!["dataNascimento"] as Timestamp?
                user.disponibilidade = item.data!!["disponibilidade"] as List<Timestamp>?
                user.endereco = item.data!!["endereco"] as String
                user.habilidades = item.data!!["habilidades"] as List<Map<String?, Any?>>?
                user.nome = item.data!!["nome"] as String?
                user.numServicosFeitos = item.data!!["numServicosFeitos"] as Long?
                try {
                    user.somaAvaliacoes = (item.data!!["somaAvaliacoes"] as Long).toDouble()
                }
                catch (e: Exception) {
                    user.somaAvaliacoes = item.data!!["somaAvaliacoes"] as Double?
                }
                users.add(user)
            }

            getListLiveData.setValue(users)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

    fun getItem(id: String) {
        val docRef = db.collection(users)
        docRef.document(id).get().addOnSuccessListener {
            val user = User()
            user.id = it.id
            user.contato = it.data!!["contato"] as String?
            user.cpf = it.data!!["cpf"] as String?
            user.dataInicio = it.data!!["dataInicio"] as Timestamp?
            user.dataNascimento = it.data!!["dataNascimento"] as Timestamp?
            user.disponibilidade = it.data!!["disponibilidade"] as List<Timestamp>?
            user.endereco = it.data!!["endereco"] as String?
            user.habilidades = it.data!!["habilidades"] as List<Map<String?, Any?>>?
            user.nome = it.data!!["nome"] as String?
            user.numServicosFeitos = it.data!!["numServicosFeitos"] as Long?
            try {
                user.somaAvaliacoes = (it.data!!["somaAvaliacoes"] as Long).toDouble()
            }
            catch (e: Exception) {
                user.somaAvaliacoes = it.data!!["somaAvaliacoes"] as Double?
            }
            getItemLiveData.setValue(user)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getItemLiveData.postValue(null)
        }
    }
}