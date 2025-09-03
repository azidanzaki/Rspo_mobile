package com.example.rspo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityFormKebunBinding

class KebunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormKebunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormKebunBinding.inflate(layoutInflater)
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
