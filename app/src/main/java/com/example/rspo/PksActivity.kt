package com.example.rspo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityFormPksBinding

class PksActivity : AppCompatActivity(){
    private lateinit var binding: ActivityFormPksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // binding untuk button
        //binding.btnSelanjutnya.setOnClickListener {
        //    startActivity(Intent(this, HistoryActivity::class.java))
        //}
        binding.btnBatal.setOnClickListener {
            finish()
        }
    }
}