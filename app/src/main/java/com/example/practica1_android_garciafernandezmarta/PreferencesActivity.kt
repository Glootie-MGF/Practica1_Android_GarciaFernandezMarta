package com.example.practica1_android_garciafernandezmarta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {
    /*
    Esta clase gestionará el formulario donde el usuario introduce sus preferencias.
    Aquí se recogerán los datos del usuario y se guardarán usando SharedPreferences.
     */
    private lateinit var binding: ActivityPreferencesBinding
    private lateinit var prefs: Preferences

    var edad = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = Preferences(this)
        title = "PandaFood by MGF"

        configurarSpinner()
        setListeners()
    }

    private fun setListeners() {
        binding.btnGuardar.setOnClickListener {
            guardarPreferencias()
        }
        binding.btnReset.setOnClickListener {
            resetPreferencias()
        }
        binding.btnVolver.setOnClickListener {
            finish()
        }
        // Listener para el seekbar de edad
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                edad = p1
                pintarEdad()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun guardarPreferencias() {
        val nombre = binding.etNombre.text.toString()
        edad = binding.seekBar.progress
        val sexo = when (binding.rgNacionalidad.checkedRadioButtonId) {
            R.id.rb_hombre -> "Hombre"
            R.id.rb_mujer -> "Mujer"
            else -> ""
        }
        val dieta = mutableListOf<String>()
        val gustoSeleccionado = binding.spGustos.selectedItem.toString()

        if (binding.ckMediterranea.isChecked) dieta.add("Mediterránea")
        if (binding.ckVegana.isChecked) dieta.add("Vegana/Vegetariana")
        if (binding.ckPaleo.isChecked) dieta.add("Paleo")

        prefs.setPreferencias(nombre, edad, sexo, dieta.joinToString(", "), gustoSeleccionado)

        val intent = Intent(this, DisplayPreferencesActivity::class.java)
        startActivity(intent)
    }

    private fun resetPreferencias() {
        binding.etNombre.setText("")
        binding.seekBar.progress = 0
        binding.rgNacionalidad.clearCheck()
        binding.ckVegana.isChecked = false
        binding.ckPaleo.isChecked = false
        binding.ckMediterranea.isChecked = false
        binding.spGustos.setSelection(0)
    }

    private fun pintarEdad(){
        // Recuperamos la cadena de texto
        binding.txtEdad.text = String.format(getString(R.string.txt_edad), edad)
    }
    private fun configurarSpinner() {
        val adapterGustos = ArrayAdapter.createFromResource(this, R.array.opciones_gustos, android.R.layout.simple_spinner_item)

        adapterGustos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spGustos.adapter = adapterGustos
    }
}