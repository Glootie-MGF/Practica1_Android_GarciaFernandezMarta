package com.example.practica1_android_garciafernandezmarta

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mapa: GoogleMap
    private val LOCATION_PERMISOS = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Tus Supermercados - PandaFood"

        cargarMapa()
        setListener()
    }

    private fun setListener() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarMapa() {
        val fragment = SupportMapFragment() // Creamos un fragment de tipo SupportMapFragment
        fragment.getMapAsync(this) // Corrutina interna

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fg_container_maps, fragment)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mapa = p0
        mapa.uiSettings.isZoomControlsEnabled = true

        mapa.setOnMyLocationButtonClickListener(this)
        mapa.setOnMyLocationClickListener(this)
        //Controles de zoom activados
        mapa.uiSettings.isZoomControlsEnabled = true
        mostrarAnimacion(LatLng(36.83443647455365, -2.4632862732957674))

        //PERMISOS AVANZADOS DE ANDROID:
        // Vamos a activar la localización
        ponerLocalizacion()

        // Añadir marcadores de supermercados
        agregarMarcadoresSupermercados()
    }

    private fun agregarMarcadoresSupermercados() {
        // Coordenadas de diferentes supermercados en Almería
        val supermercado1 = LatLng(36.83283446655504, -2.4602095850282786)
        val supermercado2 = LatLng(36.83356437772508, -2.457055307245898)
        val supermercado3 = LatLng(36.830898526484326, -2.4546270889206667)
        val supermercado4 = LatLng(36.8349780709877, -2.4618724188039067)
        val supermercado5 = LatLng(36.83673430687201, -2.4642540745813175)

        agregarMarcador(supermercado1, "Supermercado Consum")
        agregarMarcador(supermercado2, "Supermercado Mercadona")
        agregarMarcador(supermercado3, "Supermercado Carrefour Express")
        agregarMarcador(supermercado4, "Supermercado La Plaza de Día")
        agregarMarcador(supermercado5, "Supermercado Super Eco")
    }

    private fun agregarMarcador(coordenadas: LatLng, titulo: String) {
        val marker = MarkerOptions().position(coordenadas).title(titulo)
        mapa.addMarker(marker)
    }

    private fun ponerLocalizacion() {

        if (!::mapa.isInitialized) return // Si no se ha inicializado el mapa, nos vamos
        //Si ambos permisos está concedidos podemos usar la localización
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapa.isMyLocationEnabled = true
        } else {
            pedirPermisos()
        }
    }

    private fun mostrarAnimacion(coordenadas: LatLng) {
        // La animación es un zoom de nivel 20f en 5 segundos
        mapa.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas, 15f),
            5000,
            null
        )
    }

    private fun pedirPermisos() {

        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            mostrarExplicacion() // Han denegado los permisos, damos una expicación al usuario
        } else {
            escogerPermisos() //Pedimos los permisos
        }
    }

    private fun escogerPermisos() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISOS
        )
    }

    // Sobrescribimos método para tratar permisos
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISOS) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                mapa.isMyLocationEnabled = true

            } else {
                Toast.makeText(this, "El usuario ha rechazado los permisos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun mostrarExplicacion() {

        AlertDialog.Builder(this)
            .setTitle("Permisos de localización")
            .setMessage("Para que la APP le pueda ofrecer la localización necesitamos que acepte los permisos")
            .setPositiveButton("Abrir Ajustes") { view, _ ->
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)) //Se activa un fragment automático para abrir la pantalla de ajustes
                view.dismiss()
            }
            .setNegativeButton("CANCELAR", null)
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "¡Estás aquí!", Toast.LENGTH_SHORT).show()
        return false // Para que funcione hay que devolver False
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(
            this,
            "Coordenadas: Lat: ${p0.latitude}, Long: ${p0.longitude}",
            Toast.LENGTH_LONG
        ).show()
    }
}