package com.jasonwiram.nextshowing.ui;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonwiram.nextshowing.Model.Genre;
import com.jasonwiram.nextshowing.Model.Movie;
import com.jasonwiram.nextshowing.Model.Results;
import com.jasonwiram.nextshowing.R;
import com.jasonwiram.nextshowing.adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends ListActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private double mRatingThreshold = 7.5;
    private int mMinimumRatings = 10;
    private int mGteReleaseDate = 1950;
    private int mLteReleaseDate = 2017;

    private Genre mGenres = new Genre();
    private Movie[] mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String discoverUrl = "https://api.themoviedb.org/3/discover/movie" +
                "?api_key=6aac5e90ac5e7f81f8db31c9e5252f2d" +
                "&language=en-US" +
                "&sort_by=popularity.desc" +
                "&include_adult=false" +
                "&include_video=false" +
                "&page=1" +
                "&primary_release_date.gte=" + mGteReleaseDate +
                "&primary_release_date.lte=" + mLteReleaseDate +
                "&vote_count.gte=" + mMinimumRatings +
                "&vote_average.gte=" + mRatingThreshold;

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(discoverUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            mMovies = getMovies(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateResults();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "IOException caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "JSONException caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private void updateResults() {
        MovieAdapter adapter = new MovieAdapter(this,
                mMovies);
        setListAdapter(adapter);
    }

    private Movie[] getMovies(String jsonData) throws JSONException {
        JSONObject resultsData = new JSONObject(jsonData);
        JSONArray results = resultsData.getJSONArray("results");

        Movie[] movies = new Movie[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonMovie = results.getJSONObject(i);
            Movie movie = new Movie();

            movie.setTitle(jsonMovie.getString("title"));
            movie.setOverview(jsonMovie.getString("overview"));
            movie.setPoster(jsonMovie.getString("poster_path"));
            movie.setRating(jsonMovie.getDouble("vote_average"));
            movie.setPopularity(jsonMovie.getDouble("popularity"));
            movie.setId(jsonMovie.getInt("id"));
            movie.setReleaseDate(jsonMovie.getString("release_date"));

            JSONArray genresData = jsonMovie.getJSONArray("genre_ids");
            String[] genres = new String[genresData.length()];
            for (int j = 0; j < genresData.length(); j++) {
                genres[j] = mGenres.genreIds.get(genresData.getInt(j));
            }
            movie.setGenres(genres);

            movies[i] = movie;
        }

        return movies;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
