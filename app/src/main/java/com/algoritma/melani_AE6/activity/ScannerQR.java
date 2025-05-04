package com.algoritma.melani_AE6.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.algoritma.melani_AE6.R;
import com.algoritma.melani_AE6.api.APIServer;
import com.algoritma.melani_AE6.api.ResponseAPI;
import com.algoritma.melani_AE6.model.DataRes;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerQR extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 1;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeView;
    private boolean torchOn = false;
    private boolean isScanned = false;
    private ProgressDialog progressDialog;
    private ResponseAPI responseAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);

        // Inisialisasi API Service
        responseAPI = APIServer.konekRetrofit().create(ResponseAPI.class);

        barcodeView = findViewById(R.id.camera_preview);
        ImageButton btnTorch = findViewById(R.id.btn_torch);

        // Setup Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memproses...");
        progressDialog.setCancelable(false);

        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null && !isScanned) {
                    isScanned = true;
                    String qrContent = result.getText();

                    // Panggil API dengan QR code yang di-scan
                    fetchProductData(qrContent);
                }
            }
        });

        btnTorch.setOnClickListener(v -> {
            torchOn = !torchOn;
            barcodeView.getBarcodeView().setTorch(torchOn);
            btnTorch.setImageResource(torchOn ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startScanning();
        }
    }

    private void fetchProductData(String qrCode) {
        progressDialog.show();

        Call<DataRes> call = responseAPI.getData(qrCode);
        call.enqueue(new Callback<DataRes>() {
            @Override
            public void onResponse(Call<DataRes> call, Response<DataRes> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    DataRes produkResponse = response.body();

                    if (response.code() == 200) {
                        if(response.body().getMessage().equals("Data Original")){
                            // Pindah ke ResultActivity dengan membawa data produk
                            Intent intent = new Intent(ScannerQR.this, ResultProduct.class);
                            intent.putExtra("produk", response.body().getData().get(0).getNama());
                            intent.putExtra("komposisi", response.body().getData().get(0).getKomposisi());
                            intent.putExtra("foto", response.body().getData().get(0).getFoto());
                            intent.putExtra("no_pirt", response.body().getData().get(0).getNo_pirt());
                            intent.putExtra("produksi", response.body().getData().get(0).getProdusen());
                            intent.putExtra("tgl_produksi", response.body().getData().get(0).getTgl_produksi());
                            intent.putExtra("tgl_expire", response.body().getData().get(0).getTgl_expire());
                            startActivity(intent);
                            finish(); // Tutup ScannerActivity jika diperlukan
                        } else {
                            Toast.makeText(ScannerQR.this, "Kode QR tidak valid", Toast.LENGTH_LONG).show();
                            isScanned = false;
                        }
                    } else {
                        String errorMsg = produkResponse.getMessage() != null ?
                                produkResponse.getMessage() : "Terjadi kesalahan";
                        Toast.makeText(ScannerQR.this, errorMsg, Toast.LENGTH_LONG).show();
                        isScanned = false; // Reset langsung agar bisa scan lagi
                    }
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(ScannerQR.this, "Kode QR tidak valid", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ScannerQR.this, "Gagal memproses data", Toast.LENGTH_LONG).show();
                    }
                    isScanned = false; // Reset langsung agar bisa scan lagi
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DataRes> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ScannerQR.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                isScanned = false; // Reset langsung agar bisa scan lagi
            }
        });
    }

    private void startScanning() {
        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk scan QR", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (capture != null) {
            capture.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (capture != null) {
            capture.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (capture != null) {
            capture.onDestroy();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
