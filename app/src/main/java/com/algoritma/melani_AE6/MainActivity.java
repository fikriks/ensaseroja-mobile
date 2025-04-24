package com.algoritma.melani_AE6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.algoritma.melani_AE6.activity.Instruction;
import com.algoritma.melani_AE6.activity.ScannerQR;
import com.algoritma.melani_AE6.api.APIServer;
import com.algoritma.melani_AE6.api.ResponseAPI;
import com.algoritma.melani_AE6.databinding.ActivityMainBinding;
import com.algoritma.melani_AE6.model.ResponseHome;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private LinearLayout btnScanner, instruction;
    private ImageView profilePerusahaan;
    private TextView descMain, judulPerusahaan;
    private final String url = "https://ensaseroja.my.id/foto_profile_perusahaan/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bind views from layout
        btnScanner = binding.btnScanner;
        instruction = binding.instruction;

        // Set edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set button actions
        btnScanner.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ScannerQR.class));
            finish();
        });

        instruction.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Instruction.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
