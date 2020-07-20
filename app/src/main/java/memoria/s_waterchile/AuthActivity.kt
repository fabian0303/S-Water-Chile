package memoria.s_waterchile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Analytics Event
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        var bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completada")
        analytics.logEvent("InitScreen",bundle)

        setUp()
        session()
    }

    override fun onStart(){
        super.onStart()
        authLayout.visibility = View.VISIBLE
    }

    private  fun session(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null){
            authLayout.visibility = View.INVISIBLE
            showHome(email)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?: "")
                        }else{
                            showAlertLoginGoogle()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlertLoginGoogle()
            }
        }
    }


    private fun setUp(){
        title = "Autenticación"


        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText2.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(emailEditText.text.toString(),
                                passwordEditText2.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful){
                                showHome(it.result?.user?.email ?: "")
                            }else{
                                println(emailEditText.text.toString())
                                println(passwordEditText2.text.toString())
                                showAlertRegister()
                            }
                        }
            }
        }

        loginButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText2.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(emailEditText.text.toString(),
                                passwordEditText2.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful){
                                showHome(it.result?.user?.email ?: "")
                            }else{
                                showAlertLogin()
                            }
                        }
            }
        }


        googleButton.setOnClickListener {
            //Config
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken((getString(R.string.default_web_client_id)))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }
    }

    private  fun showAlertLogin(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private  fun showAlertRegister(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error registrando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private  fun showAlertLoginGoogle(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error registrando al usuario con google")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showHome(email: String){
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(homeIntent)
    }

}

