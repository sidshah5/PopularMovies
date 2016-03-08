package in.siddharthshah.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MovieListGridAdapter movieListGridAdapter;
    ArrayList<MovieRecord> movieList;
    boolean loaded=false;
    String sortby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handling rotation of screen which would call the onsavedinstance method
        //getting movielist from the savedinstance to prevent repeated loading of data and images
        if(savedInstanceState!=null && savedInstanceState.containsKey("movieList")){
            movieList = savedInstanceState.getParcelableArrayList("movieList");
        }else{
            movieList = null;
        }
        //getting the user set sortby settings from the shared preferences
        sortby = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.sortby_preferencekey),getString(R.string.sortby_defaultvalue));


        //setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        //using gridlayout with 2 columns to display movie posters
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //settings onscrolllistener on the recyclerview to implement infinite scrolling on the recyclerview
        recyclerView.setOnScrollListener(new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                loadMovies(current_page);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //saving the movielist arraylist which will be reused on rotation like activities
        outState.putParcelableArrayList("movieList", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SORT", sortby);

        //getting the new sortby option from the default shared preferences
        String newsortby = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.sortby_preferencekey),getString(R.string.sortby_defaultvalue));
        Log.d("NEW SORT", newsortby);

        //if the new sortby option differs from the previously loaded list, we need to reload the complete list
        //according to the new sorting option
        if(!sortby.equals(newsortby)){
            sortby = newsortby;
            loadMovies(1);
        }else {
            //if movie list is null i.e. there is no saved instance
            //in this case we need to load data from the internet
            if (movieList == null) {
                loadMovies(1);
            } else {
                //setting a boolean to check whether the data is previously loaded or not
                //if the data is previously loaded and the view is already saved and retrived from the stack
                //we don't need to reload or set the adapter
                if(!loaded) {
                    movieListGridAdapter = new MovieListGridAdapter(this, movieList);
                    recyclerView.setAdapter(movieListGridAdapter);
                }
            }
        }
    }

    public void loadMovies(final int pageno){

        //intialize movielist on the first page loading
        if(pageno==1) {
            movieList = new ArrayList<>();
        }


        //async task to load the movie details list
        new AsyncTask<Void,Void,Void>(){

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Loading...");
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    //api key for connecting to the movie db api
                    String api_key = getString(R.string.api_key);
                    if(api_key.isEmpty()){
                        Log.e("POPULAR MOVIES","Please enter your api key in strings.xml");
                    }

                    //constructing url with base url, sortby preference, api key and page no
                    String url = getString(R.string.movieapi)+"?sort_by="+sortby+"&api_key="+api_key+"&page="+pageno;
                    Log.d("URL",url);

                    //Generic GET request code
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    //parsing String obtained
                    JSONObject jsonObject = new JSONObject(response.toString());

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonMovieObject = jsonArray.getJSONObject(i);
                        MovieRecord movieRecord = new MovieRecord(
                                jsonMovieObject.getString("original_title"),
                                jsonMovieObject.getString("poster_path"),
                                jsonMovieObject.getString("overview"),
                                jsonMovieObject.getString("release_date"),
                                jsonMovieObject.getString("backdrop_path"),
                                jsonMovieObject.getDouble("vote_average")
                        );
                        //adding each movierecord to the movie list
                        movieList.add(movieRecord);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                //setting adapter in first page initialization
                if(pageno==1) {
                    movieListGridAdapter = new MovieListGridAdapter(MainActivity.this, movieList);
                    recyclerView.setAdapter(movieListGridAdapter);
                    recyclerView.scheduleLayoutAnimation();
                    loaded = true;
                }else{
                    //for pages from 2 onwards, we just need to notify the adapter about the change in the dataset
                    movieListGridAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
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
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            //calling settings activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
