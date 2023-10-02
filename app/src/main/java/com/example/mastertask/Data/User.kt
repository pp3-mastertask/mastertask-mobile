package com.example.mastertask.Data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var id: String? = null,
    var contato: String? = null,
    var cpf: String? = null,
    var dataInicio: Timestamp? = null,
    var dataNascimento: Timestamp? = null,
    var endereco: String? = null,
    var habilidades: List<DocumentReference>? = null,
    var nome: String? = null,
    var numServicosFeitos: Long? = null,
    var somaAvaliacoes: Long? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "contato" to contato,
            "cpf" to cpf,
            "dataInicio" to dataInicio,
            "dataNascimento" to dataNascimento,
            "endereco" to endereco,
            "habilidades" to habilidades,
            "nome" to nome,
            "numServicosFeitos" to numServicosFeitos,
            "somaAvaliacoes" to somaAvaliacoes,
        )
    }
}