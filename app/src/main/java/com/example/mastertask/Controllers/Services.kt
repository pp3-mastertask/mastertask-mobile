package com.example.mastertask.Controllers

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Services {

    val db: FirebaseFirestore = Firebase.firestore

    fun postService(service: String) {
        db.collection("servicos")
            .add(service)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
            }
    }

    fun getServices(): QuerySnapshot? {
        var a: QuerySnapshot? = null
        db.collection("servicos")
            .get()
            .addOnSuccessListener { result ->
                a = result
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return a
    }

    fun getService(id: String): MutableMap<String, Any>? {
        var a: MutableMap<String, Any>? = null
        db.collection("servicos")
            .whereEqualTo(FieldPath.documentId(), id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    a = document.data
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        return a
    }

    fun deleteService(id: String) {
        db.collection("services")
            .document(id)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun updateService(service: String) {
        db.collection("servicos").document(service).set(service)
    }
}