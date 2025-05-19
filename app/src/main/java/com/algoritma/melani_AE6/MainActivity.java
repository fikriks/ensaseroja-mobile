package com.algoritma.melani_AE6;  // Deklarasi package untuk aplikasi

// Import library yang diperlukan
import android.content.Intent;  // Untuk intent (navigasi antar activity)
import android.os.Bundle;  // Untuk menyimpan state activity
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;  // Untuk menampilkan gambar
import android.widget.LinearLayout;  // Layout container linear
import android.widget.TextView;  // Untuk menampilkan teks

import androidx.activity.EdgeToEdge;  // Untuk edge-to-edge display
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;  // Base class untuk activity
import androidx.core.graphics.Insets;  // Untuk mengatur insets (padding sistem)
import androidx.core.view.ViewCompat;  // Kompatibilitas tampilan
import androidx.core.view.WindowInsetsCompat;  // Untuk window insets

import com.algoritma.melani_AE6.activity.Instruction;  // Activity untuk instruksi
import com.algoritma.melani_AE6.activity.ScannerQR;  // Activity untuk scanner QR
import com.algoritma.melani_AE6.databinding.ActivityMainBinding;  // View binding
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    // Deklarasi variabel binding untuk view binding
    private ActivityMainBinding binding;
    // Deklarasi komponen UI
    private LinearLayout btnScanner, instruction;  // Tombol scanner dan instruksi
    private ImageView profilePerusahaan;  // Gambar profil perusahaan
    private TextView descMain, judulPerusahaan;  // Deskripsi dan judul perusahaan
    private final String url = "https://ensaseroja.my.id/foto_profile_perusahaan/";  // URL gambar profil

    private ViewPager2 viewPager;
    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;
    private static final long AUTO_SCROLL_DELAY = 2000; // 2 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Mengaktifkan edge-to-edge display
        binding = ActivityMainBinding.inflate(getLayoutInflater());  // Inflate layout dengan view binding
        setContentView(binding.getRoot());  // Set content view dari root binding

        // Bind views dari layout
        btnScanner = binding.btnScanner;  // Tombol scanner QR
        instruction = binding.instruction;  // Tombol instruksi

        // Mengatur edge-to-edge compatibility
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mengatur aksi tombol scanner
        btnScanner.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ScannerQR.class));  // Buka activity scanner QR
            finish();  // Tutup activity saat ini
        });

        // Mengatur aksi tombol instruksi
        instruction.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Instruction.class));  // Buka activity instruksi
            finish();  // Tutup activity saat ini
        });

        // Setup ViewPager2 untuk slider gambar
        setupImageSlider();
        // Tambahkan interpolator untuk animasi lebih halus
        viewPager.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(
                this, R.anim.layout_animation_fall_down));
        startAutoScroll();
    }

    private void setupImageSlider() {
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.rumah_produksi);
        images.add(R.drawable.foto_produk_1);
        images.add(R.drawable.rumah_produksi);

        viewPager.setAdapter(new ImageSliderAdapter(images));

        // Pilih salah satu efek animasi:
        viewPager.setPageTransformer(new ZoomOutPageTransformer()); // Efek zoom
        // atau
        // viewPager.setPageTransformer(new DepthPageTransformer()); // Efek 3D

        // Tambahkan margin antar item
        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Set custom view untuk setiap tab
            View tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tab.setCustomView(tabView);

            // Atur indikator awal
            if (position == 0) {
                tab.view.setSelected(true); // Tab pertama aktif
            }
        }).attach();

        // Tambahkan listener untuk update indikator saat scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        boolean isSelected = (i == position);
                        tab.view.setSelected(isSelected);

                        // Animasi perubahan ukuran
                        View tabView = tab.getCustomView();
                        if (tabView != null) {
                            View indicator = tabView.findViewById(R.id.tabIndicator);
                            if (indicator != null) {
                                float scale = isSelected ? 1.2f : 1.0f;
                                indicator.animate()
                                        .scaleX(scale)
                                        .scaleY(scale)
                                        .setDuration(200)
                                        .start();
                            }
                        }
                    }
                }
            }
        });
    }

    private void startAutoScroll() {
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = viewPager.getAdapter().getItemCount();

                if (currentItem == totalItems - 1) {
                    viewPager.setCurrentItem(0, true); // Dengan animasi
                } else {
                    viewPager.setCurrentItem(currentItem + 1, true); // Dengan animasi
                }

                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    // Animasi Zoom Out
    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0f);
            } else if (position <= 1) { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                view.setAlpha(0f);
            }
        }
    }

    // Animasi Depth (Efek 3D)
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 0) {
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0f);
            }
        }
    }

    // MarginPageTransformer untuk memberikan jarak antar item
    public class MarginPageTransformer implements ViewPager2.PageTransformer {
        private final int marginPx;

        public MarginPageTransformer(int marginPx) {
            this.marginPx = marginPx;
        }

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            float offset = position * -(2 * marginPx);

            if (viewPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                page.setTranslationX(offset);
            } else {
                page.setTranslationY(offset);
            }
        }
    }

    // Adapter untuk ViewPager2
    private static class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

        private List<Integer> imageList;

        public ImageSliderAdapter(List<Integer> imageList) {
            this.imageList = imageList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_slider, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.imageView.setImageResource(imageList.get(position));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();  // Panggil implementasi parent
        finish();  // Tutup activity saat back button ditekan
    }
}
