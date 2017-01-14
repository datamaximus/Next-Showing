package com.jasonwiram.nextshowing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonwiram.nextshowing.Model.Movie;
import com.jasonwiram.nextshowing.Model.Results;

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

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Results mResults;
    private String queryString;
    private double mRatingThreshold = 9.0;
    private int mMinimumRatings = 0;
    private int mGteReleaseDate = 2015;
    private int mLteReleaseDate = 2016;

    @BindView(R.id.resultsTextView) TextView mResultsTextView;

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
                            mResults = getResults(jsonData);
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
        mResultsTextView.setText(queryString);
    }

    private Results getResults(String jsonData) throws JSONException {
        JSONObject resultsObject = new JSONObject(jsonData);
        JSONArray results = resultsObject.getJSONArray("results");
        Log.i(TAG, "Results data: " + results);
        queryString = "Results data: " + results;

        return new Results();
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
