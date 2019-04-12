package com.example.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT * FROM movieRecord")
    List<MovieRecord> loadAllRecords();

    @Query("SELECT * FROM movieRecord")
    LiveData<List<MovieRecord>> loadAllLiveRecords();

    @Insert
    void insertRecord(MovieRecord movieRecord);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecord(MovieRecord movieRecord);

    @Delete
    void deleteRecord(MovieRecord movieRecord);
}
