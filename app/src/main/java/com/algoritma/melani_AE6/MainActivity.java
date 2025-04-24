package com.algoritma.melani_AE6;  // Deklarasi package untuk aplikasi

// Import library yang diperlukan
import android.content.Intent;  // Untuk intent (navigasi antar activity)
import android.os.Bundle;  // Untuk menyimpan state activity
import android.widget.ImageView;  // Untuk menampilkan gambar
import android.widget.LinearLayout;  // Layout container linear
import android.widget.TextView;  // Untuk menampilkan teks

import androidx.activity.EdgeToEdge;  // Untuk edge-to-edge display
import androidx.appcompat.app.AppCompatActivity;  // Base class untuk activity
import androidx.core.graphics.Insets;  // Untuk mengatur insets (padding sistem)
import androidx.core.view.ViewCompat;  // Kompatibilitas tampilan
import androidx.core.view.WindowInsetsCompat;  // Untuk window insets

import com.algoritma.melani_AE6.activity.Instruction;  // Activity untuk instruksi
import com.algoritma.melani_AE6.activity.ScannerQR;  // Activity untuk scanner QR
import com.algoritma.melani_AE6.databinding.ActivityMainBinding;  // View binding


public class MainActivity extends AppCompatActivity {
    // Deklarasi variabel binding untuk view binding
    private ActivityMainBinding binding;
    // Deklarasi komponen UI
    private LinearLayout btnScanner, instruction;  // Tombol scanner dan instruksi
    private ImageView profilePerusahaan;  // Gambar profil perusahaan
    private TextView descMain, judulPerusahaan;  // Deskripsi dan judul perusahaan
    private final String url = "https://ensaseroja.my.id/foto_profile_perusahaan/";  // URL gambar profil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Mengaktifkan edge-to-edge display
        binding = ActivityMainBinding.inflate(getLayoutInflater());  // Inflate layout dengan view binding
        setContentView(binding.getRoot());  // Set content view dari root binding

        // Bind views dari layout
        btnScanner = binding.btnScanner;  // Tombol scanner QR
        instruction = binding.instruction;  // Tombol instruksi

        // Mengatur edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mengatur aksi tombol scanner
        btnScanner.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ScannerQR.class));  // Buka activity scanner QR
            finish();  // Tutup activity saat ini
        });

        // Mengatur aksi tombol instruksi
        instruction.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Instruction.class));  // Buka activity instruksi
            finish();  // Tutup activity saat ini
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // Panggil implementasi parent
        finish();  // Tutup activity saat back button ditekan
    }
}
