package com.algoritma.melani_AE6.activity;

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.nio.ByteBuffer;

public class QRCodeImageAnalyzer implements ImageAnalysis.Analyzer {
    // Listener untuk menangani hasil scan QR code
    private final QRCodeFoundListener listener;

    // Constructor untuk menginisialisasi listener
    public QRCodeImageAnalyzer(QRCodeFoundListener listener) {
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // Cek format image yang didukung (YUV formats)
        if (image.getFormat() == YUV_420_888 || image.getFormat() == YUV_422_888 || image.getFormat() == YUV_444_888) {
            // Ambil data image dari buffer
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[byteBuffer.capacity()];
            byteBuffer.get(imageData);

            // Buat LuminanceSource dari data image
            LuminanceSource source = new PlanarYUVLuminanceSource(
                    imageData,
                    image.getWidth(),  // Lebar image
                    image.getHeight(), // Tinggi image
                    0, 0,             // Koordinat crop (tidak di-crop)
                    image.getWidth(),  // Lebar output
                    image.getHeight(), // Tinggi output
                    false             // Tidak reverse
            );

            // Buat BinaryBitmap untuk proses decoding
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                // Decode QR code menggunakan QRCodeMultiReader
                Result result = new QRCodeMultiReader().decode(binaryBitmap);
                // Panggil listener jika QR code ditemukan
                listener.onQRCodeFound(result.getText());
            } catch (NotFoundException | ChecksumException | FormatException e) {
                // Panggil listener jika QR code tidak ditemukan
                listener.qrCodeNotFound();
            }
        }
        // Tutup image setelah diproses
        image.close();
    }
}
