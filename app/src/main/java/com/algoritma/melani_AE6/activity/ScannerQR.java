package com.algoritma.melani_AE6.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.algoritma.melani_AE6.MainActivity;
import com.algoritma.melani_AE6.databinding.ActivityScannerQrBinding;
import com.algoritma.melani_AE6.helper.Camera_helper;


public class ScannerQR extends AppCompatActivity {
    // Konstanta untuk request permission kamera
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    // Binding untuk view menggunakan View Binding
    private ActivityScannerQrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout menggunakan View Binding
        binding = ActivityScannerQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mengatur padding untuk edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Meminta permission untuk menggunakan kamera
        new Camera_helper(this).requestCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Handle hasil request permission kamera
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika permission diberikan, mulai kamera
                new Camera_helper(this).startCamera();
            } else {
                // Jika permission ditolak, tampilkan pesan
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Kembali ke MainActivity saat tombol back ditekan
        startActivity(new Intent(this, MainActivity.class));
        finish();  // Menutup activity saat ini
    }
}
