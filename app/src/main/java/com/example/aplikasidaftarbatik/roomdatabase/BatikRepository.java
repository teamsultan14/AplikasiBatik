package com.example.aplikasidaftarbatik.roomdatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.aplikasidaftarbatik.model.Batik;
import com.example.aplikasidaftarbatik.model.BatikSlide;

import java.util.List;

public class BatikRepository {

    private BatikU mBatikU;
    private LiveData<List<Batik>> mAllBatik;
    private LiveData<List<BatikSlide>> mAllBatikPopular;
    private LiveData<List<Batik>> mSearchBatik;

    public BatikRepository(Application application) {
        BatikRoomDatabase db = BatikRoomDatabase.getDatabase(application);
        mBatikU = db.batikDao();
        mAllBatik = mBatikU.getAllBatik();
        mAllBatikPopular = mBatikU.getAllBatikPopular();
    }

    LiveData<List<Batik>> getAllBatik() {
        return mAllBatik;
    }

    public void insert(List<Batik> batiks) {
        BatikRoomDatabase.databaseWriteExecutor.execute(() -> {
            mBatikU.insertAllBatik(batiks);
        });
    }

    LiveData<List<BatikSlide>> getAllBatikPopular() {
        return mAllBatikPopular;
    }

    public void insertPopular(List<BatikSlide> batikslide) {
        BatikRoomDatabase.databaseWriteExecutor.execute(() -> {
            mBatikU.insertAllBatikPopular(batikslide);
        });
    }

    LiveData<List<Batik>> searchBatik(String search) {
        return mBatikU.searchBatik(search);
    }


}
