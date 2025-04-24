package com.algoritma.melani_AE6.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.algoritma.melani_AE6.MainActivity;
import com.algoritma.melani_AE6.databinding.ActivityResultProductBinding;
import com.bumptech.glide.Glide;

public class ResultProduct extends AppCompatActivity {
    private ActivityResultProductBinding binding;
    private final String url = "https://6682-114-5-215-188.ngrok-free.app/foto_product/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            binding.namaProduk.setText("Nama Produk : " + extras.getString("produk"));
            binding.komposisi.setText("Komposisi : " + extras.getString("komposisi"));
            binding.ijin.setText("No PIR-T : " + extras.getString("no_pirt"));
            binding.produsen.setText("Di produksi oleh : " + extras.getString("produksi"));
            binding.tglProduksi.setText("Tanggal Produksi: " + extras.getString("tgl_produksi"));
            binding.tglExpire.setText("Tanggal Expire: " + extras.getString("tgl_expire"));

            Glide.with(this)
                    .load(url + extras.getString("foto"))
                    .circleCrop()
                    .into(binding.imageProduk);
        } else {
            Toast.makeText(this, "Produk tidak terdaftar", Toast.LENGTH_SHORT).show();

            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(ResultProduct.this, MainActivity.class));
                finish();
            }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, ScannerQR.class));
        finish();
    }
}
