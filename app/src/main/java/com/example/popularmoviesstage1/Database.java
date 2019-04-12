package com.example.popularmoviesstage1;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@android.arch.persistence.room.Database(entities = {MovieRecord.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final String LOG_TAG = Database.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieRecordList";
    private static Database sInstance;

    public static Database getInstance(Context context){

        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        Database.class, Database.DATABASE_NAME)
                        // DELETE WHEN FINISHED TESTING
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecordDao recordDao();
}
