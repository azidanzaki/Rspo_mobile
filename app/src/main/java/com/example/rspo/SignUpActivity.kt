package com.example.rspo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API Ke Database
        api = ApiClient.instance.create(ApiService::class.java)

        binding.btnSignUp.setOnClickListener {
            val username = binding.inputName.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val confirmPassword = binding.inputConfirmPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.checkPrivacy.isChecked) {
                Toast.makeText(this, "Harap setujui privacy policy", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat request JSON
            val request = HashMap<String, String>()
            request["username"] = username
            request["email"] = email
            request["password"] = password
            request["confirm_password"] = confirmPassword

            // Panggil API register
            api.signUp(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val res = response.body()!!
                        Toast.makeText(this@SignUpActivity, res.message, Toast.LENGTH_SHORT).show()
                        if (res.status == "success") {
                            // Jika berhasil, pindah ke login
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Register gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Klik Login
        binding.tvSignIn.setOnClickListener {
            finish() // Kembali ke LoginActivity
        }
    }
}
