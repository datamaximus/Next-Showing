package com.jasonwiram.nextshowing.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jasonwiram.nextshowing.Model.Results;
import com.jasonwiram.nextshowing.R;
import com.jasonwiram.nextshowing.adapters.MovieRecyclerViewAdapter;
import org.json.JSONException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.jasonwiram.nextshowing.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private int mRatingThreshold = 8;
    private int mMinimumRatings = 1;
    private int mGteReleaseDate = 1950;
    private int mLteReleaseDate = 2017;
    private String mSortChoice = "popularity";
    private String mSortBy = "desc";
    private int mPage = 1;

    private Results mResults = new Results();
    private String discoverUrl;
    private MovieRecyclerViewAdapter adapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        mContext = getBaseContext();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ButterKnife.bind(this);

        setDiscoverUrl();
        fetchResults();

        adapter = new MovieRecyclerViewAdapter(this, mResults.getMovies());
        mRecyclerView.setAdapter(adapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    Log.d(TAG, "Scrolling");
                    mPage += 1;
                    setDiscoverUrl();
                    fetchResults();
                }
            }
        });
    }

    private void setDiscoverUrl() {
        discoverUrl = "https://api.themoviedb.org/3/discover/movie" +
                "?api_key=6aac5e90ac5e7f81f8db31c9e5252f2d" +
                "&language=en-US" +
                "&sort_by=" + mSortChoice + "." + mSortBy +
                "&include_adult=false" +
                "&include_video=false" +
                "&page=" + mPage +
                "&primary_release_date.gte=" + mGteReleaseDate +
                "&primary_release_date.lte=" + mLteReleaseDate +
                "&vote_count.gte=" + mMinimumRatings +
                "&vote_average.gte=" + mRatingThreshold +
                "&without_genres=" + 0;
    }

    private void fetchResults() {
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
                            mResults.setMovies(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {

        case R.id.sort:

            final String tempUrl = discoverUrl;
            MaterialDialog sortDialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.sort_fragment, true)
                    .positiveText("Search")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPage = 1;
                            mResults.clearResults();
                            setDiscoverUrl();
                            fetchResults();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            discoverUrl = tempUrl;
                            dialog.dismiss();
                        }
                    }).show();

            final View sortView = sortDialog.getCustomView();

            RadioGroup choiceGroup = (RadioGroup) sortView.findViewById(R.id.choiceRadioGroup);
            mSortChoice = "popularity";
            choiceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.popularityRadioButton) {
                        mSortChoice = "popularity";
                    }
                    else if (i == R.id.releaseDateRadioButton) {
                        mSortChoice = "primary_release_date";
                    }
                    else if (i == R.id.revenueRadioButton) {
                        mSortChoice = "revenue";
                    }
                    else if (i == R.id.averageVoteRadioButton) {
                        mSortChoice = "vote_average";
                    }
                    else if (i == R.id.numberVotesRadioButton) {
                        mSortChoice = "vote_count";
                    }
                }
            });

            RadioGroup sortByGroup = (RadioGroup) sortView.findViewById(R.id.sortByRadioGroup);
            mSortBy = "asc";
            sortByGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.ascendingRadioButton) {
                        mSortBy = "asc";
                    } else if (i == R.id.descendingRadioButton) {
                        mSortBy = "desc";
                    }
                }
            });

            return(true);

        case R.id.options:

            final String optionsUrl = discoverUrl;
            MaterialDialog searchDialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.search_fragment, true)
                    .positiveText("Search")
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mPage = 1;
                            mResults.clearResults();
                            setDiscoverUrl();
                            fetchResults();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            discoverUrl = optionsUrl;
                            dialog.dismiss();
                        }
                    }).show();

            View searchView = searchDialog.getCustomView();

            com.shawnlin.numberpicker.NumberPicker ratingThresholdPicker = (com.shawnlin.numberpicker.NumberPicker)searchView.findViewById(R.id.ratingNumberPicker);
            ratingThresholdPicker.setValue(mRatingThreshold);
            ratingThresholdPicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                        mRatingThreshold = newVal;
                }
            });

            com.shawnlin.numberpicker.NumberPicker minimumRatingsPicker = (com.shawnlin.numberpicker.NumberPicker)searchView.findViewById(R.id.minimumRatingsNumberPicker);
            minimumRatingsPicker.setValue(mMinimumRatings);
            minimumRatingsPicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                    mMinimumRatings = newVal;
                }
            });

            com.shawnlin.numberpicker.NumberPicker startReleaseDatePicker = (com.shawnlin.numberpicker.NumberPicker)searchView.findViewById(R.id.startYearNumberPicker);
            startReleaseDatePicker.setValue(mGteReleaseDate);
            startReleaseDatePicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                    mGteReleaseDate = newVal;
                }
            });

            com.shawnlin.numberpicker.NumberPicker endReleaseDatePicker = (com.shawnlin.numberpicker.NumberPicker)searchView.findViewById(R.id.endYearNumberPicker);
            endReleaseDatePicker.setValue(mLteReleaseDate);
            endReleaseDatePicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                    mLteReleaseDate = newVal;
                }
            });

            return(true);
    }
        return(super.onOptionsItemSelected(item));
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
