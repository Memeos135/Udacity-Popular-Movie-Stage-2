package com.example.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private boolean flag = true;
    private static ArrayList<MovieProperties> movieProperties;
    private boolean favoriteFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

                try {
                    URL url = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US&page=1");
                    new MovieDbQuery().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Fetching Information... Please wait.", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            flag = false;
            favoriteFlag = false;
            URL url = null;

            try {

                url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US&page=1");
                new MovieDbQuery().execute(url);
                Toast.makeText(this, "Fetching Information... Please wait.", Toast.LENGTH_SHORT).show();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return true;
        }else if (id == R.id.action_top){
            if(!flag){
                favoriteFlag = false;
                URL url = null;

                try {

                    url = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US&page=1");
                    new MovieDbQuery().execute(url);
                    Toast.makeText(this, "Fetching Information... Please wait.", Toast.LENGTH_SHORT).show();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }else{
            favoriteFlag = true;
            // FAVORITE ASYNC
            new FavoriteAsync().execute();
            Toast.makeText(this, "Fetching Information... Please wait.", Toast.LENGTH_SHORT).show();

            return true;
        }
    }

    public class FavoriteAsync extends AsyncTask<Void, Void, ArrayList<MovieProperties>>{

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String connectionResponse = "";

        @Override
        protected ArrayList<MovieProperties> doInBackground(Void... voids) {

            Database mDatabase = Database.getInstance(getApplicationContext());
            List<MovieRecord> x = mDatabase.recordDao().loadAllRecords();

            movieProperties  = new ArrayList<>();

            for(int i = 0; i < x.size(); i++){
                URL queryURL;
                try {

                    connectionResponse = "";

                    queryURL = new URL("https://api.themoviedb.org/3/movie/" + x.get(i).getMovie_id() + "?api_key=33e67c12c5e6da9bcd792db53c0a28d4&language=en-US");

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

                try {

                    JSONObject jsonObject = new JSONObject(connectionResponse);

                    URL url = new URL("http://image.tmdb.org/t/p/w185"+jsonObject.getString("poster_path"));
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    String rating = jsonObject.getString("vote_average");
                    String releaseDate = jsonObject.getString("release_date");
                    String description = jsonObject.getString("overview");
                    String title = jsonObject.getString("title");
                    String id = jsonObject.getString("id");

                    movieProperties.add(new MovieProperties(image, rating, releaseDate, description, title, id));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           return movieProperties;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieProperties> movieProperties) {
            super.onPostExecute(movieProperties);

            setRecyclerAdapter();
        }
    }

    public class MovieDbQuery extends AsyncTask<URL, Void, ArrayList<MovieProperties>> {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String connectionResponse = "";

        @Override
        protected ArrayList<MovieProperties> doInBackground(URL... urls) {
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

            try {
                JSONObject jsonObject = new JSONObject(connectionResponse);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                movieProperties  = new ArrayList<>();

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject poster = jsonArray.getJSONObject(i);

                    URL url = new URL("http://image.tmdb.org/t/p/w185"+poster.getString("poster_path"));
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    String rating = poster.getString("vote_average");
                    String releaseDate = poster.getString("release_date");
                    String description = poster.getString("overview");
                    String title = poster.getString("title");
                    String id = poster.getString("id");

                    movieProperties.add(new MovieProperties(image, rating, releaseDate, description, title, id));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            startLiveData();
            return movieProperties;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieProperties> imagePaths) {
            super.onPostExecute(imagePaths);

            setRecyclerAdapter();

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){

            setRecyclerAdapter();

        }
        savedInstanceState.clear();
    }

    public void setRecyclerAdapter(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), movieProperties);

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // FUNCTION FOR LIVE DATA OBSERVER THING
    public void startLiveData() {
        Database mDatabase = Database.getInstance(getApplicationContext());
        LiveData<List<MovieRecord>> x = mDatabase.recordDao().loadAllLiveRecords();
        x.observe(this, new Observer<List<MovieRecord>>() {
            @Override
            public void onChanged(@Nullable List<MovieRecord> movieRecords) {

                if (favoriteFlag) {
                    for (int i = 0; i < movieProperties.size(); i++) {

                        boolean flag = false;
                        for (int j = 0; j < movieRecords.size(); j++) {

                            if (movieRecords.get(j).getMovie_id().equals(movieProperties.get(i).getId())) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            movieProperties.remove(i);
                        }
                    }
                    setRecyclerAdapter();
                }
            }
        });
    }
}
