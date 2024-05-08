package com.example.practica1_android_garciafernandezmarta

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica1_android_garciafernandezmarta.adapters.NoticiaAdapter
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityPrincipalBinding
import com.example.practica1_android_garciafernandezmarta.models.NoticiaModel
import com.example.practica1_android_garciafernandezmarta.providers.ApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var auth: FirebaseAuth
    // Adaptador para el RecyclerView que muestra las noticias
    private val adapter = NoticiaAdapter(emptyList(), {noticia -> detalleNoticia(noticia)})
    // Clave de la API para realizar las solicitudes.
    var api = ""
    // Permisos para cámara
    private val CAMERA_PERMISOS = 1500
    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            // En mi caso NO voy a hacer nada con la foto
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        api = "8806de16827a4c76bc1075b5b206d65c"
        title = "PandaFood by MGF"

        pintarEmail()
        setRecycler() // Configura el RecyclerView
        traerNoticias() // Realiza la llamada a la API y muestra las noticias
        setListeners()
    }

    private fun setListeners() {
        binding.btnCamara.setOnClickListener {
            checkPermisos()
        }
        binding.btnSupermercados.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }

    private fun pintarEmail() {
        binding.tvEmail.text = "¡Hola ${auth.currentUser?.email}!"
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recView.layoutManager=layoutManager

        // Asigna el adaptador al RecyclerView
        binding.recView.adapter = adapter
    }

    private fun traerNoticias() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Realiza la llamada a la API y obtiene la lista de noticias
            val miLista = ApiClient.apiClient.traerNoticias(api).lista
            // Actualiza la lista de noticias en el adaptador.
            adapter.lista = miLista
            // Notifica al adaptador que los datos han cambiado.
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
            }
        }
    }
    private fun detalleNoticia(noticia: NoticiaModel){
        val i = Intent(this, DetalleActivity::class.java).apply{
            putExtra("NOTICIA", noticia)
        }
        startActivity(i)
    }

    // Después de haber creado el MENU
    // Añadimos y sobrescribimos el siguiente metodo
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_opciones, menu) // Para que aparezca el menú con las opciones
        return super.onCreateOptionsMenu(menu)
    }

    // Para interactuar con los item del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_mis_preferencias ->{
                startActivity(Intent(this, PreferencesActivity::class.java))
            }
            R.id.item_cerrar_sesion ->{
                Firebase.auth.signOut()
                finish()
            }
            R.id.item_salir ->{
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Implementación de la CÁMARA
    private fun checkPermisos() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            abrirCamara()
        }else{
            pedirPermisos()
        }
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        responseLauncher.launch(intent)
    }

    private fun pedirPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            // Si se han rechazado los permisos
            mostrarExplicacion()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISOS)
        }
    }

    private fun mostrarExplicacion() {

        AlertDialog.Builder(this)
            .setTitle("Permisos de la cámara")
            .setMessage("Para que la APP le pueda ofrecer el uso de la cámara necesitamos que acepte los permisos")
            .setPositiveButton("Abrir Ajustes") { view, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)) //Se activa un fragment automático para abrir la pantalla de ajustes
                view.dismiss()
            }
            .setNegativeButton("CANCELAR", null)
            .setCancelable(false)
            .create()
            .show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==CAMERA_PERMISOS){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                abrirCamara()
            }else{
                Toast.makeText(this, "¡¡PERMISOS RECHAZADOS!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}