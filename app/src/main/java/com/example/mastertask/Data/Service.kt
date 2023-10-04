package com.example.mastertask.Data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Service(
    var id: String? = null,
    var dataHora: Timestamp? = null,
    var emailCliente: Email? = null,
    var emailTrab: Email? = null,
    var habilidades: List<Map<String?, Any?>>? = null,
    var status: Status? = null,
    var terminado: Boolean? = null
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
            "terminado" to terminado
        )
    }
}