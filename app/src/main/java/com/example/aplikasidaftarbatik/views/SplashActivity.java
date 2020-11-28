package com.example.aplikasidaftarbatik.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasidaftarbatik.LoginActivity;
import com.example.aplikasidaftarbatik.R;

public class SplashActivity extends AppCompatActivity {
    ImageView imglogo;
    TextView teksLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //inisialisasi objek
        imglogo = findViewById(R.id.splash_imglogo);
        teksLogo = findViewById(R.id.splash_imglogo_teks);

        // sumber animasi
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadeout);

        //implementasikan animasi/ mulai animasi
        imglogo.startAnimation(animation);
        teksLogo.startAnimation(animation);

        //mejalankan splash activities beberapa detik
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //memanggil main activity
                Intent panggil = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(panggil);
                //splash activity hilang
                finish();

            }
        }, 5000);
    }
}