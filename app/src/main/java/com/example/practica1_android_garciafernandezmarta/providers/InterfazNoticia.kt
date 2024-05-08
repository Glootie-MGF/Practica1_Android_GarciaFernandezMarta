package com.example.practica1_android_garciafernandezmarta.providers

import com.example.practica1_android_garciafernandezmarta.models.ListaNoticias
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface InterfazNoticia {
    // Traemos noticias relacionadas con comida
    @GET("v2/everything?q=food+recipes+diet+nutrition")

    suspend fun traerNoticias(@Header("Authorization") apiKey:String): ListaNoticias
}