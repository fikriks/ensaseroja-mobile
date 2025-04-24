package com.algoritma.melani_AE6.helper;

import static android.view.View.GONE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.algoritma.melani_AE6.R;
import com.algoritma.melani_AE6.activity.QRCodeFoundListener;
import com.algoritma.melani_AE6.activity.QRCodeImageAnalyzer;
import com.algoritma.melani_AE6.activity.ResultProduct;
import com.algoritma.melani_AE6.api.APIServer;
import com.algoritma.melani_AE6.api.ResponseAPI;
import com.algoritma.melani_AE6.model.Response;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;

public class Camera_helper {
    // Context aplikasi Android
    private final Context context;

    // Komponen UI untuk kamera dan scanner QR
    private PreviewView previewView;
    private Button qrCodeBtn;
    private RelativeLayout textOri;
    private ImageView imgScanner;
    private TextView textingOri;

    // Future untuk camera provider
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    // Variabel untuk menyimpan hasil scan QR
    private String qrCodeS;
    private boolean qrFound = false;

    // Constructor dengan parameter Context
    public Camera_helper(Context context) {
        this.context = context;
    }

    // Method untuk meminta permission kamera
    public void requestCamera() {
        if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();  // Jika permission sudah diberikan, mulai kamera
        } else {
            // Jika belum, request permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    // Method untuk memulai kamera
    public void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);  // Bind preview kamera
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, "Gagal Memulai Kamera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    // Method untuk bind komponen UI
    private void bindViews() {
        previewView = ((Activity) context).findViewById(R.id.activity_camera_scanner);
        qrCodeBtn = ((Activity) context).findViewById(R.id.activity_main_qrCodeFoundButton);
        textOri = ((Activity) context).findViewById(R.id.textOri);
        textingOri = ((Activity) context).findViewById(R.id.textingOri);
        imgScanner = ((Activity) context).findViewById(R.id.imageView);
        qrCodeBtn.setVisibility(View.INVISIBLE);  // Sembunyikan tombol QR code awal
    }

    // Method untuk bind preview kamera dan analisis QR
    public void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        bindViews();

        // Konfigurasi preview kamera
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)  // Gunakan kamera belakang
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Konfigurasi image analysis untuk scanner QR
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640, 480))  // Resolusi target
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)  // Strategi buffer
                .build();

        // Set analyzer untuk QR code
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCodeS = _qrCode;
                qrFound = true;
                qrCodeBtn.setVisibility(View.VISIBLE);  // Tampilkan tombol QR code
                decryptData(qrCodeS);  // Proses dekripsi data QR
            }

            @Override
            public void qrCodeNotFound() {
                qrFound = false;
                // Sembunyikan tombol setelah 6 detik jika tidak ada QR
                new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> qrCodeBtn.setVisibility(View.INVISIBLE), 6000);
            }
        }));

        // Bind kamera ke lifecycle
        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis, preview);
    }

    // Method untuk dekripsi data QR
    public void decryptData(String enc) {
        ResponseAPI res = APIServer.konekRetrofit().create(ResponseAPI.class);
        Call<Response> process = res.getData(enc);
        process.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().equals("Data Original")) {
                        textOri.setVisibility(View.VISIBLE);
                        // Buat intent untuk menampilkan hasil produk
                        Intent intent = new Intent(context, ResultProduct.class);
                        intent.putExtra("produk", response.body().getData().get(0).getNama());
                        intent.putExtra("komposisi", response.body().getData().get(0).getKomposisi());
                        intent.putExtra("foto", response.body().getData().get(0).getFoto());
                        intent.putExtra("no_pirt", response.body().getData().get(0).getNo_pirt());
                        intent.putExtra("produksi", response.body().getData().get(0).getProdusen());
                        intent.putExtra("tgl_produksi", response.body().getData().get(0).getTgl_produksi());
                        intent.putExtra("tgl_expire", response.body().getData().get(0).getTgl_expire());

                        ((Activity) context).startActivity(intent);
                    }
                } else {
                    // Tampilkan pesan jika bukan produk original
                    textOri.setVisibility(View.VISIBLE);
                    textingOri.setText("Bukan Produk Original");
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> textOri.setVisibility(GONE), 5000);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(context, "Message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
