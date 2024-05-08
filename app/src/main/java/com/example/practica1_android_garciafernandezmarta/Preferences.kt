package com.example.practica1_android_garciafernandezmarta

import android.content.Context

class Preferences (val c: Context){
    /*
    Esta clase se encargará del manejo de SharedPreferences para guardar y recuperar las preferencias del usuario.
     */
    private val storage = c.getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE)

    fun setPreferencias(nombre: String, edad: Int, sexo: String, dieta: String, gusto: String) {
        storage.edit().apply {
            putString("NOMBRE", nombre)
            putInt("EDAD", edad)
            putString("SEXO", sexo)
            putString("DIETA", dieta)
            putString("GUSTO", gusto)
            apply()
        }
    }

    fun getPreferencias(): Quint<String, Int, String, String, String> {
        val nombre = storage.getString("NOMBRE", "") ?: ""
        val edad = storage.getInt("EDAD", 0)
        val sexo = storage.getString("SEXO", "") ?: ""
        val dieta = storage.getString("DIETA", "") ?: ""
        val gusto = storage.getString("GUSTO", "") ?: ""
        return Quint(nombre, edad, sexo, dieta, gusto)
    }

    fun borrarTodo() {
        storage.edit().clear().apply()
    }

    data class Quint<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
}
