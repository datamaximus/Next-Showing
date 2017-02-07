package com.jasonwiram.nextshowing.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jasonwiram.nextshowing.Model.Movie;
import com.jasonwiram.nextshowing.R;
import com.jasonwiram.nextshowing.ui.MainActivity;
import com.jasonwiram.nextshowing.ui.OverviewFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovies = new ArrayList<>();

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTextView;
        TextView overviewTextView;
        TextView releaseDateTextView;
        TextView ratingTextView;
        TextView genreTextView;
        ImageView posterImageView;


        public MovieViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView)itemView.findViewById(R.id.titleTextView);
            overviewTextView = (TextView)itemView.findViewById(R.id.overviewTextView);
            releaseDateTextView = (TextView)itemView.findViewById(R.id.releaseDateTextView);
            ratingTextView = (TextView)itemView.findViewById(R.id.ratingTextView);
            genreTextView = (TextView)itemView.findViewById(R.id.genreTextView);
            posterImageView = (ImageView)itemView.findViewById(R.id.posterImageView);

            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getOverview());
            releaseDateTextView.setText(movie.getReleaseDate());
            ratingTextView.setText(movie.getRating());
            genreTextView.setText(movie.getGenresString());
            Picasso.with(mContext).load(movie.getPoster()).into(posterImageView);
        }

        @Override
        public void onClick(View view) {
            displayMovieOverview();
        }

        private void displayMovieOverview() {

            new MaterialDialog.Builder(mContext)
                    .title("Overview")
                    .content(overviewTextView.getText().toString())
                    .show();
        }
    }
}
