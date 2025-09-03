package com.example.rspo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rspo.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataHistory = listOf(
            HistoryItem("Input dari PKS", "26 Juni 2023", "PKS Tandun"),
            HistoryItem("Input dari Admin", "01 Juli 2023", "Data tambahan"),
            HistoryItem("Verifikasi", "05 Juli 2023", "Verifikasi berhasil")
        )

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = HistoryAdapter(dataHistory)

        // binding untuk navbar
        binding.bottomNav.navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.bottomNav.navHome.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }

        binding.bottomNav.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
