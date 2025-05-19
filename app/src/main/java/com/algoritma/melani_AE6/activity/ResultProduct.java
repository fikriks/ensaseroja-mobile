package com.algoritma.melani_AE6.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.algoritma.melani_AE6.MainActivity;
import com.algoritma.melani_AE6.databinding.ActivityResultProductBinding;
import com.bumptech.glide.Glide;

public class ResultProduct extends AppCompatActivity {
    // Binding untuk view menggunakan View Binding
    private ActivityResultProductBinding binding;
    // URL dasar untuk gambar produk
    private final String url = "https://ensaseroja.my.id/foto_product/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout menggunakan View Binding
        binding = ActivityResultProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mengatur padding untuk edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listener for back button
        binding.btnBack.setOnClickListener(v -> goBackToMainActivity());

        // Mengambil data dari Intent yang dikirim
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Menampilkan data produk ke UI
            binding.namaProduk.setText("Nama Produk : " + extras.getString("produk"));
            binding.komposisi.setText("Komposisi : " + extras.getString("komposisi"));
            binding.ijin.setText("No PIR-T : " + extras.getString("no_pirt"));
            binding.produsen.setText("Di produksi oleh : " + extras.getString("produksi"));
            binding.tglProduksi.setText("Tanggal Produksi: " + extras.getString("tgl_produksi"));
            binding.tglExpire.setText("Tanggal Expire: " + extras.getString("tgl_expire"));

            // Menampilkan gambar produk menggunakan Glide
            Glide.with(this)
                    .load(url + extras.getString("foto"))
                    .circleCrop()  // Membuat gambar berbentuk lingkaran
                    .into(binding.imageProduk);
        } else {
            // Menampilkan pesan jika produk tidak terdaftar
            Toast.makeText(this, "Produk tidak terdaftar", Toast.LENGTH_SHORT).show();

            // Kembali ke MainActivity setelah 3 detik
            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(ResultProduct.this, MainActivity.class));
                finish();  // Menutup activity saat ini
            }, 3000);
        }
    }

    private void goBackToMainActivity() {
        startActivity(new Intent(ResultProduct.this, MainActivity.class));
        finish();  // Menutup activity saat ini
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Kembali ke ScannerQR saat tombol back ditekan
        startActivity(new Intent(this, MainActivity.class));
        finish();  // Menutup activity saat ini
    }
}
