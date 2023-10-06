package com.example.mastertask.Data

import com.google.firebase.Timestamp

class CardServiceInfo (var nome: String?, var endereco: String?, var contato: String?,
                       var somaAvaliacoes: Double?, var numServicosFeitos: Long?,
                       var dataHora: Timestamp?, var emailCliente: String?,
                       var emailTrab: String?, var habilidades: List<Map<String?, Any?>>?,
                       var status: String?, var terminado: Boolean?)