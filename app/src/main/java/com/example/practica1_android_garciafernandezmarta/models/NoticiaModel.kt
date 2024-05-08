package com.example.practica1_android_garciafernandezmarta.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class NoticiaModel(
    @SerializedName("title") val titulo: String,
    @SerializedName("urlToImage") val imagen: String,
    @SerializedName("description") val resumen: String,
    @SerializedName("author") val autor: String,
    @SerializedName("publishedAt") val fechaPublicacion: Date,
    @SerializedName("content") val contenido: String
): Serializable

data class ListaNoticias(
    @SerializedName("articles") val lista: List<NoticiaModel>
)