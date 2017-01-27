package com.jasonwiram.nextshowing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonwiram.nextshowing.Model.Movie;
import com.jasonwiram.nextshowing.R;
import com.squareup.picasso.Picasso;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private Context mContext;
    private Movie[] mMovies;

    public MovieRecyclerViewAdapter(Context context, Movie[] movies) {
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
        holder.bindMovie(mMovies[position]);
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

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
        }

        public void bindMovie(Movie movie) {
            titleTextView.setText(movie.getTitle());
            overviewTextView.setText(movie.getOverview());
            releaseDateTextView.setText(movie.getReleaseDate());
            ratingTextView.setText(movie.getRating());
            genreTextView.setText(movie.getGenresString());
            Picasso.with(mContext).load(movie.getPoster()).into(posterImageView);
        }
    }
}
