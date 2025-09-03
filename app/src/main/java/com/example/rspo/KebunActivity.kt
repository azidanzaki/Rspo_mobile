package com.example.rspo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityFormKebunBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Intent

class KebunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormKebunBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormKebunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init API service
        api = ApiClient.instance.create(ApiService::class.java)

        // Load data Kebun untuk spinner _root_ide_package_.com.example.rspo.KebunFotoActivity
        loadKebun()

        // Load data Afdeling untuk spinner
        loadAfdeling()

        // Tanggal Masuk Picker
        binding.etTanggalMasuk.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                val pickedDate = Calendar.getInstance()
                pickedDate.set(y, m, d)
                val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                binding.etTanggalMasuk.setText(formatter.format(pickedDate.time))
            }, year, month, day).show()
        }

        // Tombol Batal
        binding.btnBatal.setOnClickListener {
            finish()
        }

        // Tombol Selanjutnya
        binding.btnSelanjutnya.setOnClickListener {
            val tanggal = binding.etTanggalMasuk.text.toString()
            val namaKebun = binding.spinnerKebun.text.toString()
            val afdeling = binding.spinnerAfdeling.text.toString()
            val blanko = binding.etNomorBlanko.text.toString()
            val nopol = binding.etNopol.text.toString()
            val supir = binding.etSupir.text.toString()

            if (tanggal.isEmpty() || namaKebun.isEmpty() || namaKebun.isEmpty() ||
                blanko.isEmpty() || nopol.isEmpty() || supir.isEmpty()
            ) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            } else {
                // Kirim ke halaman foto
                val intent = Intent(this, KebunFotoActivity::class.java).apply {
                    putExtra("tanggal", tanggal)
                    putExtra("nama_kebun", namaKebun)
                    putExtra("afdeling", afdeling)
                    putExtra("blanko", blanko)
                    putExtra("nopol", nopol)
                    putExtra("supir", supir)
                }
                startActivity(intent)

            }
        }

    }

    private fun loadKebun() {
        api.getKebun().enqueue(object : Callback<KebunResponse> {
            override fun onResponse(call: Call<KebunResponse>, response: Response<KebunResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val kebunList = response.body()?.data ?: emptyList()
                    val namaList = kebunList.map { it.nama_kebun }

                    val adapter = ArrayAdapter(
                        this@KebunActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        namaList
                    )
                    binding.spinnerKebun.setAdapter(adapter)
                } else {
                    Toast.makeText(this@KebunActivity, "Gagal ambil data Kebun", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<KebunResponse>, t: Throwable) {
                Toast.makeText(this@KebunActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadAfdeling() {
        api.getAfdeling().enqueue(object : Callback<AfdelingResponse> {
            override fun onResponse(call: Call<AfdelingResponse>, response: Response<AfdelingResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val afdelingList = response.body()?.data ?: emptyList()
                    val namaList = afdelingList.map { it.afdeling }

                    val adapter = ArrayAdapter(
                        this@KebunActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        namaList
                    )
                    binding.spinnerAfdeling.setAdapter(adapter)
                } else {
                    Toast.makeText(this@KebunActivity, "Gagal ambil data Afdeling", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AfdelingResponse>, t: Throwable) {
                Toast.makeText(this@KebunActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
