package com.example.mastertask.Data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var id: String? = null,
    var contato: String? = null,
    var cpf: String? = null,
    var dataNascimento: Timestamp? = null,
    var endereco: String? = null,
    var habilidades: List<String>? = null,
    var mediaAtual: Double? = null,
    var nome: String? = null,
    var numServicosFeitos: Int? = null
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "contato" to contato,
            "cpf" to cpf,
            "dataNascimento" to dataNascimento,
            "endereco" to endereco,
            "habilidades" to habilidades,
            "mediaAtual" to mediaAtual,
            "nome" to nome,
            "numServicosFeitos" to numServicosFeitos
        )
    }
}