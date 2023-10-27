package com.example.mastertask.Data

import com.google.firebase.Timestamp

class CardServiceInfo (var id: String?, var nome: String?, var imgFoto: String?,
                       var endereco: String?, var contato: String?,
                       var somaAvaliacoes: Double?, var numServicosFeitos: Long?,
                       var dataHora: Timestamp?, var emailCliente: String?,
                       var emailTrab: String?, var habilidades: List<Map<String?, Any?>>?,
                       var status: String?)