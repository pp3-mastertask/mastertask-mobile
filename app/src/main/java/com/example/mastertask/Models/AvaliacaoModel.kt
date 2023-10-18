package com.example.mastertask.Models

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastertask.Data.Avaliacao
import com.example.mastertask.Data.Status
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AvaliacaoViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val evaluations = "avaliacao"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Avaliacao>> by lazy {
        MutableLiveData<List<Avaliacao>>()
    }

    val getItemLiveData : MutableLiveData<Avaliacao> by lazy {
        MutableLiveData<Avaliacao>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(avaliacao: Avaliacao) {
        val docRef = db.collection(evaluations)
        docRef.add(avaliacao.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(avaliacao: Avaliacao) {
        val docRef = db.collection(evaluations)
        docRef.document(avaliacao.id!!).update(avaliacao.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(evaluations)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(evaluations)
        docRef.get().addOnSuccessListener {
            val evaluations = ArrayList<Avaliacao>()
            for (item in it.documents) {
                val evaluation = Avaliacao()
                evaluation.id = item.id
                evaluation.comentario = item.data!!["comentario"] as String?
                try {
                    evaluation.estrelas = item.data!!["estrelas"] as Double?
                } catch (e: Exception) {
                    val a = item.data!!["estrelas"] as Long?
                    evaluation.estrelas = a?.toDouble()
                }
                evaluation.servico = item.data!!["servico"] as String?
                evaluations.add(evaluation)
            }

            getListLiveData.postValue(evaluations)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

    fun getItem(id: String) {
        val docRef = db.collection(evaluations)
        docRef.document(id).get().addOnSuccessListener {
            val evaluation = Avaliacao()
            evaluation.id = it.id
            evaluation.comentario = it.data!!["comentario"] as String?
            try {
                evaluation.estrelas = it.data!!["estrelas"] as Double?
            } catch (e: Exception) {
                val a = it.data!!["estrelas"] as Long?
                evaluation.estrelas = a?.toDouble()
            }
            evaluation.servico = it.data!!["servico"] as String?
            getItemLiveData.postValue(evaluation)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getItemLiveData.postValue(null)
        }
    }
}