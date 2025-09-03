package com.example.rspo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityFormPksBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.content.Intent

class PksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPksBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init API service
        api = ApiClient.instance.create(ApiService::class.java)

        // Load data PKS untuk spinner
        loadPks()

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
            val namaPks = binding.spinnerPks.text.toString()
            val tujuan = binding.etTujuan.text.toString()
            val blanko = binding.etNomorBlanko.text.toString()
            val nopol = binding.etNopol.text.toString()
            val supir = binding.etSupir.text.toString()

            if (tanggal.isEmpty() || namaPks.isEmpty() || tujuan.isEmpty() ||
                blanko.isEmpty() || nopol.isEmpty() || supir.isEmpty()
            ) {
                Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            } else {
                // Kirim ke halaman foto
                val intent = Intent(this, PksFotoActivity::class.java).apply {
                    putExtra("tanggal", tanggal)
                    putExtra("nama_pks", namaPks)
                    putExtra("tujuan", tujuan)
                    putExtra("blanko", blanko)
                    putExtra("nopol", nopol)
                    putExtra("supir", supir)
                }
                startActivity(intent)

            }
        }

    }

    private fun loadPks() {
        api.getPks().enqueue(object : Callback<PksResponse> {
            override fun onResponse(call: Call<PksResponse>, response: Response<PksResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val pksList = response.body()?.data ?: emptyList()
                    val namaList = pksList.map { it.nama_pks }

                    val adapter = ArrayAdapter(
                        this@PksActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        namaList
                    )
                    binding.spinnerPks.setAdapter(adapter)
                } else {
                    Toast.makeText(this@PksActivity, "Gagal ambil data PKS", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PksResponse>, t: Throwable) {
                Toast.makeText(this@PksActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
