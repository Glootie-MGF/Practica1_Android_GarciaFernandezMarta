package com.example.practica1_android_garciafernandezmarta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityDetalleBinding
import com.example.practica1_android_garciafernandezmarta.models.NoticiaModel
import com.squareup.picasso.Picasso

class DetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Tus Noticias - PandaFood"

        // Recupera los datos de la noticia y muestra la información en la interfaz.
        recuperarNoticia()
        // Configura el listener del botón "Volver".
        setListener()
    }
    private fun setListener() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
    private fun recuperarNoticia() {
        val datos = intent.extras
        val noticia = datos?.getSerializable("NOTICIA") as NoticiaModel
        binding.tvTitulo2.text = noticia.titulo
        Picasso.get().load(noticia.imagen).into(binding.ivImagen)
        binding.tvAutor.text= noticia.autor
        binding.tvFecha.text= noticia.fechaPublicacion.toString()
        binding.tvResumen.text=noticia.contenido
    }
}