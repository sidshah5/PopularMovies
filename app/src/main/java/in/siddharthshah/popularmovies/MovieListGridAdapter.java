package in.siddharthshah.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by siddharth on 25-02-2016.
 */
public class MovieListGridAdapter extends RecyclerView.Adapter<MovieListGridAdapter.ViewHolder> {
    Context context;
    ArrayList<MovieRecord> movieRecords;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv;
    public ProgressBar pb;
    public TextView movietitle;

    public ViewHolder(View v) {
        super(v);
        iv = (ImageView)v.findViewById(R.id.moviePoster);
        pb = (ProgressBar)v.findViewById(R.id.progressBar);
        movietitle = (TextView)v.findViewById(R.id.movietitle);
    }
}
    public MovieListGridAdapter(Context context, ArrayList<MovieRecord> movieRecords) {
        this.context = context;
        this.movieRecords = movieRecords;
    }


    @Override
    public MovieListGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // creating a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MovieRecord movieRecord = movieRecords.get(position);
        holder.pb.setVisibility(View.VISIBLE);
        holder.movietitle.setText(movieRecord.getOriginal_title());
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+movieRecords.get(position).getPoster_path()).into(holder.iv, new Callback() {
            @Override
            public void onSuccess() {
                holder.pb.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.iv.setImageDrawable(context.getResources().getDrawable(R.drawable.poster1));
                holder.pb.setVisibility(View.GONE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MovieDetailActivity.class);
                i.putExtra("movierecord",movieRecord);
                context.startActivity(i);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieRecords.size();
    }
}