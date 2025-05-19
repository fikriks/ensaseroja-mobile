package com.algoritma.melani_AE6.activity;

import  android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.algoritma.melani_AE6.MainActivity;
import com.algoritma.melani_AE6.databinding.ActivityInstructionBinding;

public class Instruction extends AppCompatActivity {
    private ActivityInstructionBinding binding;  // Variabel untuk view binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inisialisasi view binding
        binding = ActivityInstructionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  // Set content view dari root binding

        // Mengatur insets untuk layout utama (edge-to-edge display)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Mengatur padding sesuai system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set click listener for back button
        binding.btnBack.setOnClickListener(v -> goBackToMainActivity());
    }

    private void goBackToMainActivity() {
        startActivity(new Intent(Instruction.this, MainActivity.class));
        finish();  // Menutup activity saat ini
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // Panggil implementasi parent
        // Kembali ke MainActivity saat tombol back ditekan
        startActivity(new Intent(this, MainActivity.class));
        finish();  // Tutup activity saat ini
    }
}
