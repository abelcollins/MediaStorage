package com.thecloudforme.mediastorage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var CLIENT_ID = "123867608923-9dkfn28gdkdo9lu2pv1igg01njvbi7q8.apps.googleusercontent.com"
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    fun showErrorPopup(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ -> }
        builder.show()
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateSignInUI(account)
        } catch (e: ApiException) {
            updateSignInUI(null)
        }
    }

    private fun updateSignInUI(account: GoogleSignInAccount?) {
        Log.i("DEBUGGING", account.toString())
        if (account != null) {
            // Do something with the google account
            // Create an Intent to start the second activity
            val intent = Intent(this, MainActivity::class.java)
            // Send data to next activity
            intent.putExtra("GoogleAccount", account)
            //Start Main Page Activity
            startActivity(intent)
            finish()
        } else {
            // Handle error
            showErrorPopup(this, "Login Failed, Please Try Again")
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
