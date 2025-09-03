package com.example.rspo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API Ke Database
        api = ApiClient.instance.create(ApiService::class.java)

        binding.btnLogin.setOnClickListener {
            val login = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username/Email & password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.checkPrivacy.isChecked) {
                Toast.makeText(this, "Harap setujui privacy policy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat request JSON
            val request = HashMap<String, String>()
            request["login"] = login
            request["password"] = password

            // Panggil API login
            api.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        Toast.makeText(this@LoginActivity, res.message, Toast.LENGTH_SHORT).show()
                        if (res.status == "success") {
                            startActivity(Intent(this@LoginActivity, Dashboard::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Klik Sign UP
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
