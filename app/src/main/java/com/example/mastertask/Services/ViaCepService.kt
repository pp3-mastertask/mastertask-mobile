package com.example.mastertask.Services

import com.example.mastertask.Data.Responses.CepResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("/{cep}")
    fun getCep(@Path("cep") cep: String): Call<List<CepResponse>>
}