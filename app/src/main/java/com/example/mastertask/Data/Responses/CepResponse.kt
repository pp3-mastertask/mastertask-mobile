package com.example.mastertask.Data.Responses

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

data class CepResponse(
//    @field:SerializedName("id") val cep: String,
//    @field:SerializedName("logradouro") val logradouro: String,
//    @field:SerializedName("complemento") val complemento: String,
//    @field:SerializedName("bairro") val bairro: String,
//    @field:SerializedName("localidade") val localidade: String,
//    @field:SerializedName("uf") val uf: String,
//    @field:SerializedName("ibge") val ibge: String,
//    @field:SerializedName("gia") val gia: String,
//    @field:SerializedName("ddd") val ddd: String,
//    @field:SerializedName("siafi") val siafi: String
    @field:SerializedName("complemento") val complemento: JSONObject,
    @field:SerializedName("bairro") val bairro: String,
    @field:SerializedName("cidade") val cidade: String,
    @field:SerializedName("logradouro") val logradouro: String,
    @field:SerializedName("estado_info") val estado_info: JSONObject,
    @field:SerializedName("cep") val cep: String,
    @field:SerializedName("cidade_info") val cidade_info: JSONObject,
    @field:SerializedName("estado") val estado: String,
) : Serializable