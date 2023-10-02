package com.example.mastertask.Models

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mastertask.Data.Service
import com.example.mastertask.Data.Status
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ServiceViewModel: ViewModel() {

    private var db = Firebase.firestore
    private val services = "servicos"

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Service>> by lazy {
        MutableLiveData<List<Service>>()
    }

    val getItemLiveData : MutableLiveData<Service> by lazy {
        MutableLiveData<Service>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(service: Service) {
        val docRef = db.collection(services)
        docRef.add(service.toMap()).addOnSuccessListener {
            createLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("create", it.localizedMessage!!)
            createLiveData.postValue(false)
        }
    }

    fun update(service: Service) {
        val docRef = db.collection(services)
        docRef.document(service.id!!).update(service.toMap()).addOnSuccessListener {
            updateLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("update", it.localizedMessage!!)
            updateLiveData.postValue(false)
        }
    }

    fun delete(id: String) {
        val docRef = db.collection(services)
        docRef.document(id).delete().addOnSuccessListener {
            deleteLiveData.postValue(true)
        }.addOnFailureListener {
            Log.d("delete", it.localizedMessage!!)
            deleteLiveData.postValue(false)
        }
    }

    fun getList() {
        val docRef = db.collection(services)
        docRef.get().addOnSuccessListener {
            val services = ArrayList<Service>()
            for (item in it.documents) {
                val service = Service()
                service.id = item.id
                service.dataHora = item.data!!["dataHora"] as Timestamp?
                service.emailCliente = item.data!!["emailCliente"] as ContactsContract.CommonDataKinds.Email?
                service.emailTrab = item.data!!["emailTrab"] as ContactsContract.CommonDataKinds.Email?
                service.habilidade = item.data!!["habilidade"] as String?
                service.status = item.data!!["status"] as Status?
                service.terminado = item.data!!["terminado"] as Boolean?
                services.add(service)
            }

            getListLiveData.postValue(services)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getListLiveData.postValue(null)
        }
    }

    fun getItem(id: String) {
        val docRef = db.collection(services)
        docRef.document(id).get().addOnSuccessListener {
            val service = Service()
            service.id = it.id
            service.dataHora = it.data!!["dataHora"] as Timestamp?
            service.emailCliente = it.data!!["emailCliente"] as ContactsContract.CommonDataKinds.Email?
            service.emailTrab = it.data!!["emailTrab"] as ContactsContract.CommonDataKinds.Email?
            service.habilidade = it.data!!["habilidade"] as String?
            service.status = it.data!!["status"] as Status?
            service.terminado = it.data!!["terminado"] as Boolean?
            getItemLiveData.postValue(service)
        }.addOnFailureListener {
            Log.d("get", it.localizedMessage!!)
            getItemLiveData.postValue(null)
        }
    }
}