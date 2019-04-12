package com.example.popularmoviesstage1;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "movieRecord")
public class MovieRecord {

    @PrimaryKey()
    @NonNull()
    private String movie_id;
    private String movie_name;

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public MovieRecord(String movie_id, String movie_name) {
        this.movie_id = movie_id;
        this.movie_name = movie_name;
    }
}
