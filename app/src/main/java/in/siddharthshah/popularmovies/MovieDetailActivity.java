package in.siddharthshah.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

/**
 * Created by siddharth on 27-02-2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    TextView movietitle,synopsis,vote_average,releasedate;
    ImageView moviedetailposter,moviedetailtoolbarimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);

        movietitle = (TextView)findViewById(R.id.detail_movietitle);
        synopsis = (TextView)findViewById(R.id.synopsis);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        vote_average = (TextView)findViewById(R.id.ratingtv);
        releasedate = (TextView)findViewById(R.id.releasedatetv);
        moviedetailposter = (ImageView)findViewById(R.id.moviedetailposter);
        moviedetailtoolbarimage = (ImageView)findViewById(R.id.toolbarimage);


        //settings collapsing toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        //obtaining parceable movie record object
        MovieRecord movieRecord = getIntent().getParcelableExtra("movierecord");

        //setting display of the movie detail activity
        movietitle.setText(movieRecord.getOriginal_title());
        synopsis.setText(movieRecord.getPlot_synopsis());
        ratingBar.setRating((float) movieRecord.getUser_rating());
        vote_average.setText("Rating : " + movieRecord.getUser_rating() + " / 10");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("d MMM, yyyy");


        //setting the toolbar with title and back button
        getSupportActionBar().setTitle(movieRecord.getOriginal_title());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try{
            //setting release date
            releasedate.setText("Release Date: "+sdf2.format(sdf1.parse(movieRecord.getRelease_date())));
        }catch (Exception e){
            e.printStackTrace();
        }

        Picasso.with(this).load(getString(R.string.image_baseurl) + movieRecord.getBackdrop_path()).into(moviedetailtoolbarimage, new Callback() {
            @Override
            public void onSuccess() {
                //hiding the progressbar
                findViewById(R.id.detail_progressbar1).setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                //hiding the progress bar and displaying generic image on error
                findViewById(R.id.detail_progressbar1).setVisibility(View.GONE);
                moviedetailtoolbarimage.setImageResource(R.drawable.poster2);
            }
        });

        Picasso.with(this).load(getString(R.string.image_baseurl) + movieRecord.getPoster_path()).into(moviedetailposter, new Callback() {
            @Override
            public void onSuccess() {
                //hiding the progressbar
                findViewById(R.id.detail_progressbar2).setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                //hiding the progress bar and displaying generic image on error
                findViewById(R.id.detail_progressbar2).setVisibility(View.GONE);
                moviedetailposter.setImageResource(R.drawable.poster1);

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
