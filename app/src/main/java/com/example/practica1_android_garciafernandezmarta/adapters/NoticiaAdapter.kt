package com.example.practica1_android_garciafernandezmarta.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica1_android_garciafernandezmarta.R
import com.example.practica1_android_garciafernandezmarta.models.NoticiaModel

class NoticiaAdapter (var lista: List<NoticiaModel>,
                      private val onItemClick: (NoticiaModel)->Unit): RecyclerView.Adapter<NoticiasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.noticias_layout, parent, false)
        return NoticiasViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoticiasViewHolder, position: Int) {
        holder.render(lista[position], onItemClick)
    }

    override fun getItemCount()=lista.size
}