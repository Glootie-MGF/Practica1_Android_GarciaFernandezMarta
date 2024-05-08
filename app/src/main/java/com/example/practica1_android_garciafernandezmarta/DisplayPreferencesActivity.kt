package com.example.practica1_android_garciafernandezmarta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityDisplayPreferencesBinding
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityPreferencesBinding

class DisplayPreferencesActivity : AppCompatActivity() {
    /*
    Esta clase mostrará las preferencias guardadas del usuario.
     */
    private lateinit var binding: ActivityDisplayPreferencesBinding
    private lateinit var prefs: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = Preferences(this)
        title = "PandaFood by MGF"

        mostrarPreferencias()
        setListener()
    }

    private fun setListener() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun mostrarPreferencias() {
        val (nombre, edad, sexo, dieta, gusto) = prefs.getPreferencias()

        binding.tvValorNombre.text = nombre
        binding.tvValorEdad.text = edad.toString()
        binding.tvValorSexo.text = sexo
        binding.tvValorDietas.text = dieta
        binding.tvValorGustos.text = gusto
    }

}