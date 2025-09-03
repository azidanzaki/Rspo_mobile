package com.example.rspo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rspo.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Klik Unit PKS dan Kebun
        val openPksPage = Intent(this, PksActivity::class.java)
        val openKebunPage = Intent(this, KebunActivity::class.java)
        binding.layoutUnitPks.setOnClickListener { startActivity(openPksPage) }
        binding.layoutUnitKebun.setOnClickListener { startActivity(openKebunPage) }


        // Data history dummy (sama kayak di HistoryActivity)
        val dataHistory = listOf(
            HistoryItem("Input dari PKS", "26 Juni 2023", "PKS Tandun"),
            HistoryItem("Input dari Admin", "01 Juli 2023", "Data tambahan"),
            HistoryItem("Verifikasi", "05 Juli 2023", "Verifikasi berhasil")
        )

        // Ambil hanya 3 data terbaru
        val latestHistory = dataHistory.take(3)

        // Set adapter ke recyclerDashboardHistory
        binding.recyclerDashboardHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerDashboardHistory.adapter = HistoryAdapter(latestHistory)

        // Klik teks "Lihat semua" → HistoryActivity
        binding.historyHeader.historySeeAll.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }


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

