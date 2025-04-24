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
    private final Context context;

    private PreviewView previewView;
    private Button qrCodeBtn;
    private RelativeLayout textOri;
    private ImageView imgScanner;
    private TextView textingOri;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private String qrCodeS;
    private boolean qrFound = false;

    public Camera_helper(Context context) {
        this.context = context;
    }

    public void requestCamera() {
        if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    public void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, "Gagal Memulai Kamera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindViews() {
        previewView = ((Activity) context).findViewById(R.id.activity_camera_scanner);
        qrCodeBtn = ((Activity) context).findViewById(R.id.activity_main_qrCodeFoundButton);
        textOri = ((Activity) context).findViewById(R.id.textOri);
        textingOri = ((Activity) context).findViewById(R.id.textingOri);
        imgScanner = ((Activity) context).findViewById(R.id.imageView);
        qrCodeBtn.setVisibility(View.INVISIBLE);
    }

    public void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        bindViews();

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                qrCodeS = _qrCode;
                qrFound = true;
                qrCodeBtn.setVisibility(View.VISIBLE);
                decryptData(qrCodeS);
            }

            @Override
            public void qrCodeNotFound() {
                qrFound = false;
                new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> qrCodeBtn.setVisibility(View.INVISIBLE), 6000);
            }
        }));

        cameraProvider.bindToLifecycle((LifecycleOwner) context, cameraSelector, imageAnalysis, preview);
    }

    public void decryptData(String enc) {
        ResponseAPI res = APIServer.konekRetrofit().create(ResponseAPI.class);
        Call<Response> process = res.getData(enc);
        process.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().equals("Data Original")) {
                        textOri.setVisibility(View.VISIBLE);
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
