package com.example.mastertask.Models
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData//nao kkk eu n sei oq é isso mas o mmoço o sof sabekkkkkkk
import androidx.lifecycle.ViewModel

class HabilidadeViewModel : ViewModel() {
    private val habilidadesLiveData: MutableLiveData<List<String>> = MutableLiveData()

    fun getHabilidades(): LiveData<List<String>> {
        val habilidades = listOf("Mecanica", "Eletrica", "Encanamento", "Faxina") //deve ter mais coisaskkkkkkkkkkk me nao lembro no momentok
        habilidadesLiveData.value = habilidades
        return habilidadesLiveData
    }
}