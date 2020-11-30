package com.example.aplikasidaftarbatik.api;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.aplikasidaftarbatik.model.Batik;
import com.example.aplikasidaftarbatik.model.BatikSlide;
import com.example.aplikasidaftarbatik.roomdatabase.BatikViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiBatik {

    BatikViewModel mBatikViewModel;

    public ApiBatik(BatikViewModel mBatikViewModel) {
        this.mBatikViewModel = mBatikViewModel;
    }

    public void getData() {
        getAllBatik();
        getAllPopularBatik();
    }

    public void getAllBatik() {


        List<Batik> dataBatik = new ArrayList<>();
        AndroidNetworking.get("https://api.rawg.io/api/games")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    Batik item = new Batik(
                                            data.getInt("id"),
                                            data.getString("name"),
                                            data.getString("released"),
                                            data.getString("slug"),
                                            data.getInt("rating_top"),
                                            data.getInt("ratings_count"),
                                            data.getInt("reviews_text_count"),
                                            data.getString("background_image"));
                                    dataBatik.add(item);
                                }
                                mBatikViewModel.insert(dataBatik);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }



    public void getAllPopularBatik() {
        List<BatikSlide> dataBatikPopular = new ArrayList<>();
        AndroidNetworking.get("https://api.rawg.io/api/games")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("results");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    BatikSlide item = new BatikSlide(
                                            data.getInt("id"),
                                            data.getString("name"),
                                            data.getString("background_image"));
                                    dataBatikPopular.add(item);
                                }

                                mBatikViewModel.insertPopular(dataBatikPopular);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
