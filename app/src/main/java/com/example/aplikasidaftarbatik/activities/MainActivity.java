package com.example.aplikasidaftarbatik.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.example.aplikasidaftarbatik.api.ApiBatik;
import com.example.aplikasidaftarbatik.model.Batik;
import com.example.aplikasidaftarbatik.model.BatikSlide;
import com.example.aplikasidaftarbatik.R;
import com.example.aplikasidaftarbatik.adapters.BatikAdapter;
import com.example.aplikasidaftarbatik.adapters.BatikSliderAdapter;
import com.example.aplikasidaftarbatik.roomdatabase.BatikViewModel;
import com.example.aplikasidaftarbatik.utilities.CheckInternet;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "com.example.aplikasidaftarbatik.extra.MESSAGE" ;
    //bahan logout
    SharedPreferences sharedpreferences;
    Intent intent;
    // The order message, displayed in the Toast and sent to the new Activity.
    private String mOrderMessage;


    //recyclerView
    BatikAdapter batikAdapter;
    RecyclerView idRecyclerView;

    //Slider layout
    SliderView sliderView;
    BatikSliderAdapter SlideAdapter;

    // Refresh layout
    TextView labelNoInternet;
    Button refreshButton;

    //Search layout
    SearchView searchView;

    //ViewModel
    BatikViewModel mBatikViewModel;

    //InternetConnection
    CheckInternet checkInternet;
    ApiBatik apiBatik;

    //Untuk menampilkan durasi keluar aplikasi
    TextView textWaktu;
    SimpleDateFormat dateFormat;


    private SharedPreferences mPreferences;

    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idRecyclerView = findViewById(R.id.idRecyclerView);
        sliderView = findViewById(R.id.imageSlider);
        labelNoInternet = findViewById(R.id.label_no_internet);
        refreshButton = findViewById(R.id.refresh_button);
        searchView = findViewById(R.id.kolomcari);
        textWaktu = findViewById(R.id.label_waktu);

        //RecycleView Batik
        batikAdapter = new BatikAdapter(this);
        idRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        idRecyclerView.setAdapter(batikAdapter);

        //Slider Batik
        SlideAdapter = new BatikSliderAdapter(this);
        sliderView.setSliderAdapter(SlideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();

        //Inisialisasi Fast Android Networking Library untuk panggil api
        AndroidNetworking.initialize(getApplicationContext());

        //Check Internet
        checkInternet = new CheckInternet(getApplicationContext());

        //inisialisasi untuk ambil waktu
        dateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss" , Locale.getDefault());

        //inisialisasi SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        //Inisialisasi view model
        mBatikViewModel = new ViewModelProvider(this).get(BatikViewModel.class);

        //Inisialisasi api
        apiBatik = new ApiBatik(mBatikViewModel);

        //Tombol Refresh
        refreshButton.setOnClickListener(view -> {
            hideRefresh();
            checkData();
        });

        //Periksa Data dari api
        checkData();

        //Ambil data dari database untuk RecyclerView
        mBatikViewModel.getAllBatik().observe(this, new Observer<List<Batik>>() {
            @Override
            public void onChanged(List<Batik> batiks) {
                batikAdapter.setBatikList(batiks);
            }
        });

        //Ambil data dari database untuk Image Slider
        mBatikViewModel.getAllBatikPopular().observe(this, new Observer<List<BatikSlide>>() {
            @Override
            public void onChanged(List<BatikSlide> batikSlides) {
                SlideAdapter.setBatikList(batikSlides);
            }
        });


        //set Pencarian
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class)
                        .putExtra("query_search", query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

    }


    //Periksa data di api
    public void checkData() {
        if (checkInternet.isConnected()) {
            Toast.makeText(MainActivity.this, "Sedang Memuat ...", Toast.LENGTH_SHORT).show();

            //Beri waktu aplikasi membaca cache
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    //jika tidak ada cache, maka ambil data dari internet
                    if (batikAdapter.getItemCount() < 1) {
                        apiBatik.getData();
                    }
                }
            }, 2000);

        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection ...", Toast.LENGTH_SHORT).show();

            //Beri waktu aplikasi membaca cache
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (batikAdapter.getItemCount() < 1) {
                        displayRefresh();
                    }
                }
            }, 2000);
        }
    }


    public void hideRefresh() {
        labelNoInternet.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
    }

    public void displayRefresh() {
        labelNoInternet.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    //bagian menu opsi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Do nothing
        if (item.getItemId() == R.id.action_logout) {
            sharedpreferences = getSharedPreferences(
                    LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(LoginActivity.session_status, false);
            editor.putString(LoginActivity.TAG_ID, null);
            editor.putString(LoginActivity.TAG_EMAIL, null);
            editor.putString(LoginActivity.TAG_NAME, null);
            editor.putString(LoginActivity.TAG_PHONE, null);
            editor.apply();
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
