package com.example.mastertask.Data

class Status {
    var status: String = "status"
    get() = field
    set(value) {
        if (status in listOf("Pendente", "Aceito", "Cancelado", "Finalizado"))
            field = value
        else
            throw Exception("Status inválido!")
    }
}