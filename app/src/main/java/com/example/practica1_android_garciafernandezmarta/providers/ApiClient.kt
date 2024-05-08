package com.example.practica1_android_garciafernandezmarta.providers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiClient = retrofit.create(InterfazNoticia::class.java)
}