package com.example.studentass.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.auth0.android.jwt.JWT
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.models.AuthLoginData
import com.example.studentass.models.AuthLoginTokens
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginFragment : Fragment() {
    companion object {
        const val credentialsLogin = "ritg"
        const val credentialsPassword = "ritg"

        var loginTokens = AuthLoginTokens("", "")
        var loginRole = "NONE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidEmail(s.toString())) {
                    emailIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    emailOkIv.visibility = View.VISIBLE
                }
                else {
                    emailIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    emailOkIv.visibility = View.GONE
                }
            }
        })
        passwordEt.addTextChangedListener (object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidPassword(s.toString())) {
                    passwordIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthField))
                    passwordOkIv.visibility = View.VISIBLE
                }
                else {
                    passwordIv?.setColorFilter(ContextCompat.getColor(context!!, R.color.colorAuthInactive))
                    passwordOkIv.visibility = View.GONE
                }
            }
        })
        loginBn.setOnClickListener { _ -> onLoginButtonClick() }
        registrationTv.setOnClickListener { _ ->  onRegistrationTextViewClick()}
    }

    private fun login(login: String, password: String) {
        val url = "http://test.asus.russianitgroup.ru/api/auth/login"
        val body = AuthLoginData(login, password)

        val credential = Credentials.basic(credentialsLogin, credentialsPassword)
        val requestBody = GsonBuilder().create().toJson(body).toRequestBody()

        val client = OkHttpClient()
        val request = Request.Builder().header("Authorization", credential).method("POST", requestBody).url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Login request error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val loginTokensObject = GsonBuilder().create().fromJson(
                        response.body!!.string(),
                        AuthLoginTokens::class.java
                    )
                    loginTokens = loginTokensObject
                    val accessToken = loginTokens.accessToken
                    val jwt = JWT(accessToken)
                    loginRole = jwt.getClaim("role").asString()!!
                    MainActivity.mHandler.post {
                        //goToMainActivity()
                    }
                } catch (e: Exception) {
                    MainActivity.mHandler.post {
                        Toast.makeText(context, "Response interpretation error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    /*fun goToMainActivity() {
        val intentActivity = Intent(this, MainActivity::class.java)
        startActivity(intentActivity)
    }*/

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun onLoginButtonClick() {
        try {
            val emailText : String = emailEt?.text.toString()
            val passwordText : String = passwordEt?.text.toString()

            if (emailText.isEmpty())
                throw Exception("Не указан адрес эл. почты")
            if (passwordText.isEmpty())
                throw Exception("Не указан пароль")

            loginBn.startAnimation()
            //login(emailText, passwordText)
            loginRole = "student"
            MainActivity.switchFragment(this, MainActivity.mainFragment)
        }
        catch (e: Exception) {
            val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
            loginBn.startAnimation(shake)

            val errorMessage : String = "Ошибка: " + e.message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRegistrationTextViewClick() {
        MainActivity.switchFragment(this, MainActivity.registrationFragment)
    }
}