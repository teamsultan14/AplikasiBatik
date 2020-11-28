package com.example.aplikasidaftarbatik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "com.example.aplikasidaftarbatik.extra.MESSAGE" ;
    //bahan logout
    SharedPreferences sharedpreferences;
    Intent intent;
    

    // The order message, displayed in the Toast and sent to the new Activity.
    private String mOrderMessage;

    RecyclerView mRecyclerView;
    List<BatikU> dataBatik;
    BatikUAdapter batikUAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = findViewById(R.id.idRecycleView);
        dataBatik = new ArrayList<>();
        AndroidNetworking.initialize(getApplicationContext());

        getAllBatik();

        batikUAdapter = new BatikUAdapter(dataBatik, this);
        mRecyclerView.setAdapter(batikUAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getAllBatik() {
        AndroidNetworking.get("https://batikita.herokuapp.com/index.php/batik/all")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("hasil");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    BatikU item = new BatikU(
                                            data.getInt("id"),
                                            data.getString("nama_batik"),
                                            data.getString("daerah_batik"),
                                            data.getString("makna_batik"),
                                            data.getInt("harga_rendah"),
                                            data.getInt("harga_tinggi"),
                                            data.getInt("hitung_view"),
                                            data.getString("link_batik"));
                                    dataBatik.add(item);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal di Load!", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }

                        batikUAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Tidak dapat terhubung ke internet!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //bagian menu opsi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cari:
//                Intent intent = new Intent(HomeActivity.this,
//                        OrderActivity.class);
//                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
//                startActivity(intent);
                displayToast(getString(R.string.action_cari_message));
                return true;
            case R.id.action_akun:
                displayToast(getString(R.string.action_akun_message));
                return true;
            case R.id.action_contact:
                displayToast(getString(R.string.action_kontak_message));
                return true;
            case R.id.action_notif:
                displayToast(getString(R.string.action_notif_message));
                return true;
            case R.id.action_logout:
                sharedpreferences = getSharedPreferences(
                        LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginActivity.session_status, false);
                editor.putString(LoginActivity.TAG_ID, null);
                editor.putString(LoginActivity.TAG_EMAIL, null);
                editor.putString(LoginActivity.TAG_NAME, null);
                editor.putString(LoginActivity.TAG_PHONE, null);
                editor.apply();
                Intent intent = new Intent(HomeActivity.this,
                        LoginActivity.class);
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                finish();
                startActivity(intent);
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

//menampilkan toast
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    //batas akhir bagian menu
    

}