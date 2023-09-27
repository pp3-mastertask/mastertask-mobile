package com.example.mastertask.Data

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Habilidade(
    var id: String? = null,
    var habilidade: String? = null,
    var preco: Double? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "habilidade" to habilidade,
            "preco" to preco
        )
    }
}