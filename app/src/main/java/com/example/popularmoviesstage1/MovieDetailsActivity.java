package com.example.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MovieDetailsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Context context;
    private String id;
    private String titleR;
    private int counter = 0;
    private ImageView star;
    private static ArrayList<TrailerInfo> videoList;
    private static ArrayList<ReviewInfo> reviewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        context = this;

        if (savedInstanceState == null) {

            defaultSets();

            URL url = null;
            try {

                ((RadioButton) findViewById(R.id.trailers)).setChecked(true);

                url = new URL("https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US");
                new TrailersAsync().execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            imageListeners();

        }

    }

    class TrailersAsync extends AsyncTask<URL, Void, ArrayList<TrailerInfo>>{

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String connectionResponse = "";

        @Override
        protected ArrayList<TrailerInfo> doInBackground(URL... urls) {

            URL queryURL = urls[0];

            try {
                httpURLConnection = (HttpURLConnection) queryURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputSream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputSream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    connectionResponse = connectionResponse + line;
                }

                if(connectionResponse.length() == 0){
                    Log.i("status", "Failed");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try{

                JSONObject jsonObject = new JSONObject(connectionResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                videoList = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject video = jsonArray.getJSONObject(i);
                    String key = video.getString("key");
                    String name = video.getString("name");

                    videoList.add(new TrailerInfo(key, name));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return videoList;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerInfo> strings) {
            super.onPostExecute(strings);

            trailerRecyclerSet();

        }
    }

    class ReviewsAsync extends AsyncTask<URL, Void, ArrayList<ReviewInfo>>{

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String connectionResponse = "";

        @Override
        protected ArrayList<ReviewInfo> doInBackground(URL... urls) {

            URL queryURL = urls[0];

            try {
                httpURLConnection = (HttpURLConnection) queryURL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputSream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputSream));

                String line;

                while((line = bufferedReader.readLine()) != null){
                    connectionResponse = connectionResponse + line;
                }

                if(connectionResponse.length() == 0){
                    Log.i("status", "Failed");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            try{

                JSONObject jsonObject = new JSONObject(connectionResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                reviewList = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject review = jsonArray.getJSONObject(i);
                    String author = review.getString("author");
                    String content = review.getString("content");

                    reviewList.add(new ReviewInfo(author, content));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewList;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewInfo> reviewInfos) {
            super.onPostExecute(reviewInfos);

            reviewRecyclerSet();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(counter == 1){
            outState.putString("state", "checked");
        }else{
            outState.putString("state", "unchecked");
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){

            radioGroup = findViewById(R.id.radioGroup);
            star = findViewById(R.id.starImage);

            if(savedInstanceState.getString("state").equals("checked")){
                star.setImageResource(R.drawable.ic_star_yellow_24dp);
                counter = 1;
            }else{
                star.setImageResource(R.drawable.ic_star_black_24dp);
                counter = 0;
            }

            defaultSets();

            trailerRecyclerSet();

            imageListeners();

        }
        savedInstanceState.clear();
    }

    public void defaultSets(){
        radioGroup = findViewById(R.id.radioGroup);
        star = findViewById(R.id.starImage);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        titleR = title;
        final String rating = intent.getStringExtra("rating");
        String releaseDate = intent.getStringExtra("release_date");
        String description = "\n" + intent.getStringExtra("description");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmap");
        id = intent.getStringExtra("id");

        TextView titleText = findViewById(R.id.title);
        TextView ratingText = findViewById(R.id.rating);
        TextView releaseText = findViewById(R.id.releaseDate);
        TextView descriptionText = findViewById(R.id.description);
        ImageView imageView = findViewById(R.id.imageView2);

        titleText.append(title);
        ratingText.append(rating);
        releaseText.append(releaseDate);
        descriptionText.append(description);
        imageView.setImageBitmap(bitmap);

        Database mDatabase =  Database.getInstance(getApplicationContext());
        List<MovieRecord> x = mDatabase.recordDao().loadAllRecords();

        if(x.size() > 0) {
            for (int i = 0; i < x.size(); i++) {
                if(x.get(i).getMovie_id().equals(id)){
                    star.setImageResource(R.drawable.ic_star_yellow_24dp);
                    counter = 1;
                    break;
                }
            }
        }else{
            Log.i("test", "NO ITEMS");
        }
    }

    public void trailerRecyclerSet(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DetailsRecyclerAdapter recyclerViewAdapter = new DetailsRecyclerAdapter(getApplicationContext(), videoList, null,"trailer");

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void reviewRecyclerSet(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DetailsRecyclerAdapter recyclerViewAdapter = new DetailsRecyclerAdapter(getApplicationContext(), null, reviewList,"review");

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void imageListeners(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                URL url;
                if (i == 2131230923) {
                    try {
                        url = new URL("https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US");
                        new TrailersAsync().execute(url);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        url = new URL("https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US");
                        new ReviewsAsync().execute(url);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 1) {
                    counter = 0;
                    star.setImageResource(R.drawable.ic_star_black_24dp);
                    roomRemove(id);
                } else {
                    counter++;
                    star.setImageResource(R.drawable.ic_star_yellow_24dp);
                    roomAdd(id, titleR);
                }
            }
        });
    }

    public void roomRemove(final String id){
        DatabaseExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                Database mDatabase =  Database.getInstance(getApplicationContext());
                List<MovieRecord> x = mDatabase.recordDao().loadAllRecords();

                for(int i = 0; i < x.size(); i++){
                    if(x.get(i).getMovie_id().equals(id)){
                        mDatabase.recordDao().deleteRecord(x.get(i));
                        break;
                    }
                }

                Log.i("test", "COMPLETED REMOVAL");
            }
        });
    }

    public void roomAdd(final String id, final String title){
        DatabaseExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                Database mDatabase =  Database.getInstance(getApplicationContext());
                MovieRecord movieRecord = new MovieRecord(id, title);
                mDatabase.recordDao().insertRecord(movieRecord);

                Log.i("test", "COMPLETED ADDITION");
            }
        });
    }
}
