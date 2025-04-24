package com.algoritma.melani_AE6.activity;


public interface QRCodeFoundListener {
    // Method yang dipanggil ketika QR code berhasil ditemukan
    // @param qrCode String hasil scan QR code
    void onQRCodeFound(String qrCode);
    
    // Method yang dipanggil ketika QR code tidak ditemukan
    void qrCodeNotFound();
}