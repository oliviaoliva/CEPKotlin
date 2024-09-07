package com.example.myapplication.model

import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoServiceIF {
    @GET("{cep}/json/")
    suspend fun getEndereco(@Path("cep") cep: String): Endereco
}