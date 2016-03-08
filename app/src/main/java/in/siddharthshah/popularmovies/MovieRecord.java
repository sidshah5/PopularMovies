package in.siddharthshah.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by siddharth on 26-02-2016.
 */
public class MovieRecord implements Parcelable{

    private String original_title;
    private String poster_path;
    private String plot_synopsis;
    private String release_date;
    private String backdrop_path;
    private double user_rating;

    public MovieRecord(String original_title, String poster_path, String plot_synopsis, String release_date,String backdrop_path,double user_rating) {
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.plot_synopsis = plot_synopsis;
        this.release_date = release_date;
        this.backdrop_path = backdrop_path;
        this.user_rating = user_rating;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(double user_rating) {
        this.user_rating = user_rating;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(plot_synopsis);
        dest.writeString(release_date);
        dest.writeString(backdrop_path);
        dest.writeDouble(user_rating);
    }

    public static final Creator CREATOR = new Creator() {
        public MovieRecord createFromParcel(Parcel in) {
            return new MovieRecord(in);
        }
        public MovieRecord[] newArray(int size) {
            return new MovieRecord[size];
        }
    };

    private MovieRecord(Parcel in) {
        original_title = in.readString();
        poster_path = in.readString();
        plot_synopsis = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
        user_rating = in.readDouble();
    }
}
