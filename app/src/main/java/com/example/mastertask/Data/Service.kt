package com.example.mastertask.Data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Service(
    var id: String? = null,
    var dataHora: Timestamp? = null,
    var emailCliente: String? = null,
    var emailTrab: String? = null,
    var habilidades: List<Map<String?, Any?>>? = null,
    var status: String? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "dataHora" to dataHora,
            "emailCliente" to emailCliente,
            "emailTrab" to emailTrab,
            "habilidades" to habilidades,
            "status" to status,
        )
    }
}