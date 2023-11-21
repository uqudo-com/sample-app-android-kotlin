package com.uqudo.android.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.auth0.jwk.UrlJwkProvider
import com.uqudo.android.sample.databinding.ActivityMainBinding
import com.uqudo.android.sample.jwk.JwkKeyResolver
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.uqudo.sdk.core.DocumentBuilder
import io.uqudo.sdk.core.SessionStatus
import io.uqudo.sdk.core.UqudoBuilder
import io.uqudo.sdk.core.UqudoSDK
import io.uqudo.sdk.core.domain.model.DocumentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartPassportOnboarding.setOnClickListener { startEnrollment() }
    }


    private fun startEnrollment() {
        val documentType =
            DocumentBuilder(this).setDocumentType(DocumentType.PASSPORT).enableReading().build()

        try {
            val authorizationToken = "ACCESS_TOKEN_HERR" // access_token from the API response
            val uqudoIntent =
                UqudoBuilder.Enrollment().setToken(authorizationToken).add(documentType)
                    .enableFacialRecognition().build(this)
            resultLauncher.launch(uqudoIntent)

        } catch (_: Exception) {

        }

    }

    private suspend fun parseJWS(resultJWS: String): Jws<Claims>? {
        val claims = withContext(Dispatchers.IO) {
            Jwts.parserBuilder().setSigningKeyResolver(
                    JwkKeyResolver(
                        UrlJwkProvider("https://id.uqudo.io/api")
                    )
                ).build().parseClaimsJws(resultJWS)
        }
        return claims
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultJWS = data?.getStringExtra("data")

                // Parsing the JWS (JSON Web Signature) result on the client side, as returned from the SDK, is strongly discouraged.
                // We only show this for demonstration purposes.
                // This practice can prevent the verification of the data's integrity.
                // To maintain security, the "resultJWS" value should be sent to your backend server, where signature verification and parsing should be conducted.
                // This method allows for a reliable confirmation of the data's integrity.

                CoroutineScope(Dispatchers.Main).launch {
                    val claims = parseJWS(resultJWS!!)
                    Toast.makeText(
                        applicationContext, claims?.body.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                val data: Intent? = result.data
                if (data != null) {
                    val sessionStatus =
                        (data.getParcelableExtra("key_session_status") as SessionStatus?)!!
                    println("Enrollment failed due to ${sessionStatus.sessionStatusCode.message} at ${sessionStatus.sessionTask}")
                    Toast.makeText(
                        applicationContext, sessionStatus.sessionStatusCode.message, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

}
