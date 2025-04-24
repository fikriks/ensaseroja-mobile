package com.algoritma.melani_AE6.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.algoritma.melani_AE6.MainActivity;
import com.algoritma.melani_AE6.databinding.ActivityInstructionBinding;


public class Instruction extends AppCompatActivity {
    private ActivityInstructionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInstructionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mengatur insets untuk layout utama
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigasi kembali ke MainActivity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
