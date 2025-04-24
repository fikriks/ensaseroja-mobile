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
        profilePerusahaan = binding.profilePerusahaan;
        descMain = binding.descMain;
        judulPerusahaan = binding.judulPerusahaan;

        // Set edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch and display home data
        getDataHome();

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

    private void getDataHome() {
        ResponseAPI res = APIServer.konekRetrofit().create(ResponseAPI.class);
        Call<ResponseHome> process = res.getDataHome();
        process.enqueue(new Callback<ResponseHome>() {
            @Override
            public void onResponse(Call<ResponseHome> call, Response<ResponseHome> response) {
                if (response.code() == 200 && response.body() != null && response.body().getCode() != 404) {
                    Log.d("Response", "getDataHome:" + response.body());
                    // Load company profile image using Glide
                    Glide.with(MainActivity.this)
                            .load(url + response.body().getData().get(0).getFoto())
                            .into(profilePerusahaan);

                    // Set text views
                    descMain.setText(response.body().getData().get(0).getDeskripsi());
                    judulPerusahaan.setText(response.body().getData().get(0).getNama());
                } else {
                    Toast.makeText(MainActivity.this, "Data tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseHome> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error MainActivity", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
