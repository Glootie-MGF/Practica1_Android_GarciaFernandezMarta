package com.example.practica1_android_garciafernandezmarta

import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practica1_android_garciafernandezmarta.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var pass = ""

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleGoogleSignInResult(result)
            // Con el responseLauncher busca si tenemos guardados los credenciales de nuestra cuenta
            // y si no los tenemos nos deja registrar el usuario
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        // Inicialización de Firebase
        auth = Firebase.auth
        title = "PandaFood by MGF"

        setListeners()
    }

    // Clase que se va a lanzar una vez que se lanza el método onCreate y
    // nos detecta si tenemos un usuario creado para acceder al Activity
    override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        if (usuario != null) {
            irActivityPrincipal()
        }
    }

    private fun setListeners() {
        binding.btnRegistrar.setOnClickListener {
            if (comprobarCampos()) {
                registroBasico()
            }
        }
        binding.btnLogin.setOnClickListener {
            if (comprobarCampos()) {
                loginBasico()
            }
        }
        binding.btnGoogle.setOnClickListener {
            loginGoogle()
        }
    }

    // Método para lanzar el Activity Principal
    private fun irActivityPrincipal() {
        startActivity(Intent(this, PrincipalActivity::class.java))
    }

    // Comprobamos que los campos introducidos sean correctos
    private fun comprobarCampos(): Boolean {
        email = binding.txtEmail.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.error = "Introduce un email válido"
            return false
        }
        pass = binding.txtPassword.text.toString().trim()
        if (pass.length < 6) {
            binding.txtPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }
        return true
    }

    //Método para registrarnos con un email y contraseña
    private fun registroBasico() {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginBasico()
                } else {
                    Log.e("RegistroBasico", "Error en el registro", it.exception)
                    Toast.makeText(this, "Este email ya esta registrado", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Método para loguearnos con un email y contraseña
    private fun loginBasico() {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    irActivityPrincipal()
                } else {
                    Log.e("LoginBasico", "Error en el inicio de sesión", it.exception)
                    Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Método para loguearnos con google
    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id)) // el valor lo cogemos del archivo python google-services que esta en la carpeta de este proyecto, guardado en values
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut() //Esto es importante ponerlo para que nos cierre sesión y podamos elegir otro usuario

        responseLauncher.launch(googleClient.signInIntent) //Lanzamos un intent
    }

    private fun handleGoogleSignInResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                    Firebase.auth.signInWithCredential(credentials)
                        .addOnCompleteListener(this) { signInTask ->
                            if (signInTask.isSuccessful) {
                                irActivityPrincipal()
                            } else {
                                Log.e("GoogleSignIn", "Error en el inicio de sesión con Google", signInTask.exception)
                                Toast.makeText(this, "Error en el inicio de sesión con Google", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error en el inicio de sesión con Google", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "El usuario ha cancelado", Toast.LENGTH_SHORT).show()
        }
    }

}
