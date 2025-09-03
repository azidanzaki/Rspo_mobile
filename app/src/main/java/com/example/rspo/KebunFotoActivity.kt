package com.example.rspo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rspo.databinding.ActivityGambarKebunBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class KebunFotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGambarKebunBinding

    private val PICK_IMAGE_REQUEST = 100
    private var selectedImageViewId: Int? = null

    // Variabel untuk menyimpan URI foto
    private var foto1Uri: Uri? = null
    private var foto2Uri: Uri? = null
    private var foto3Uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGambarKebunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val tanggal = intent.getStringExtra("tanggal")
        val namaKebun = intent.getStringExtra("nama_kebun")
        val afdeling = intent.getStringExtra("afdeling")
        val blanko = intent.getStringExtra("blanko")
        val nopol = intent.getStringExtra("nopol")
        val supir = intent.getStringExtra("supir")

        Toast.makeText(this, "Data diterima: $namaKebun - $nopol", Toast.LENGTH_SHORT).show()

        // Klik pada foto untuk pilih gambar
        binding.foto1.setOnClickListener { openGallery(binding.foto1.id) }
        binding.foto2.setOnClickListener { openGallery(binding.foto2.id) }
        binding.foto3.setOnClickListener { openGallery(binding.foto3.id) }

        // Tombol kembali
        binding.btnKembali.setOnClickListener { finish() }

        // Tombol selanjutnya → kirim data ke server
        binding.btnSelanjutnya.setOnClickListener {
            uploadKebun(tanggal, namaKebun, afdeling, blanko, nopol, supir)
        }
    }

    private fun openGallery(imageViewId: Int) {
        selectedImageViewId = imageViewId
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null && selectedImageViewId != null) {
                when (selectedImageViewId) {
                    binding.foto1.id -> {
                        binding.foto1.setImageURI(imageUri)
                        foto1Uri = imageUri
                    }
                    binding.foto2.id -> {
                        binding.foto2.setImageURI(imageUri)
                        foto2Uri = imageUri
                    }
                    binding.foto3.id -> {
                        binding.foto3.setImageURI(imageUri)
                        foto3Uri = imageUri
                    }
                }
            }
        }
    }

    // Konversi URI ke MultipartBody.Part
    private fun uriToMultipart(uri: Uri?, name: String): MultipartBody.Part? {
        if (uri == null) return null

        val file = File(cacheDir, name)
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }

        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }

    // Fungsi upload data + foto
    private fun uploadKebun(
        tanggal: String?,
        namaKebun: String?,
        afdeling: String?,
        blanko: String?,
        nopol: String?,
        supir: String?
    ) {
        val api = ApiClient.instance.create(ApiService::class.java)

        // Konversi teks menjadi RequestBody
        val tanggalRb = RequestBody.create("text/plain".toMediaTypeOrNull(), tanggal ?: "")
        val kebunRb = RequestBody.create("text/plain".toMediaTypeOrNull(), namaKebun ?: "")
        val afdelingRb = RequestBody.create("text/plain".toMediaTypeOrNull(), afdeling ?: "")
        val blankoRb = RequestBody.create("text/plain".toMediaTypeOrNull(), blanko ?: "")
        val nopolRb = RequestBody.create("text/plain".toMediaTypeOrNull(), nopol ?: "")
        val supirRb = RequestBody.create("text/plain".toMediaTypeOrNull(), supir ?: "")

        // Multipart foto
        val foto1Part = uriToMultipart(foto1Uri, "foto1")
        val foto2Part = uriToMultipart(foto2Uri, "foto2")
        val foto3Part = uriToMultipart(foto3Uri, "foto3")

        // Panggil API
        api.insertKebun(tanggalRb, kebunRb, afdelingRb, blankoRb, nopolRb, supirRb, foto1Part, foto2Part, foto3Part)
            .enqueue(object : Callback<KebunInputResponse> {
                override fun onResponse(call: Call<KebunInputResponse>, response: Response<KebunInputResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@KebunFotoActivity,
                            response.body()?.message ?: "Berhasil dikirim",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Pindah ke Dashboard dan hapus semua aktivitas sebelumnya
                        val intent = Intent(this@KebunFotoActivity, Dashboard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@KebunFotoActivity,
                            "Gagal: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onFailure(call: Call<KebunInputResponse>, t: Throwable) {
                    Toast.makeText(
                        this@KebunFotoActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
