package com.example.practica1_android_garciafernandezmarta.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1_android_garciafernandezmarta.databinding.NoticiasLayoutBinding
import com.example.practica1_android_garciafernandezmarta.models.NoticiaModel
import com.squareup.picasso.Picasso

class NoticiasViewHolder (v: View): RecyclerView.ViewHolder(v){
    private val binding = NoticiasLayoutBinding.bind(v)

    fun render(noticia: NoticiaModel, onItemClick : (NoticiaModel)->Unit){
        binding.tvTittle.text=noticia.titulo
        Picasso.get().load(noticia.imagen).into(binding.ivNoticia)

        itemView.setOnClickListener{
            onItemClick(noticia) //Configura el evento de clic en la noticia
        }
    }
}