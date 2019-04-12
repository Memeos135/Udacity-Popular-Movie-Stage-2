package com.example.popularmoviesstage1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class MovieProperties implements Parcelable {
    private Bitmap bitmap;
    private String rating;
    private String releaseDate;
    private String description;
    private String title;
    private String id;

    MovieProperties(Bitmap bitmap, String rating, String releaseDate, String description, String title, String id){
        this.bitmap = bitmap;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.description = description;
        this.title = title;
        this.id = id;
    }


    protected MovieProperties(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        rating = in.readString();
        releaseDate = in.readString();
        description = in.readString();
        title = in.readString();
        id = in.readString();
    }

    public static final Creator<MovieProperties> CREATOR = new Creator<MovieProperties>() {
        @Override
        public MovieProperties createFromParcel(Parcel in) {
            return new MovieProperties(in);
        }

        @Override
        public MovieProperties[] newArray(int size) {
            return new MovieProperties[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public String getRating(){
        return rating;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getDescription(){
        return description;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(bitmap, i);
        parcel.writeString(rating);
        parcel.writeString(releaseDate);
        parcel.writeString(description);
        parcel.writeString(title);
        parcel.writeString(id);
    }
}
