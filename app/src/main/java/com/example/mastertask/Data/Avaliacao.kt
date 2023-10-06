package com.example.mastertask.Data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Avaliacao(
    var id: String? = null,
    var comentario: String? = null,
    var estrelas: Double? = null,
    var servico: String? = null,
    var terminado: Boolean? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "comentario" to comentario,
            "estrelas" to estrelas,
            "servico" to servico
        )
    }
}