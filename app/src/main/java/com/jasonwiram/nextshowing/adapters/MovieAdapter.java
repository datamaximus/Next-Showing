package com.jasonwiram.nextshowing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonwiram.nextshowing.Model.Movie;
import com.jasonwiram.nextshowing.R;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private Movie[] mMovies;

    public MovieAdapter(Context context, Movie[] movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public int getCount() {
        return mMovies.length;
    }

    @Override
    public Object getItem(int i) {
        return mMovies[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, null);
            holder = new ViewHolder();
            holder.titleTextView = (TextView)view.findViewById(R.id.titleTextView);
//            holder.overviewTextView = (TextView)view.findViewById(R.id.overviewTextView);
//            holder.releaseDateTextView = (TextView)view.findViewById(R.id.releaseDateTextView);
//            holder.ratingTextView = (TextView)view.findViewById(R.id.ratingTextView);
            holder.posterImageView = (ImageView)view.findViewById(R.id.posterImageView);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Movie movie = mMovies[i];

        holder.titleTextView.setText(movie.getTitle());
//        holder.overviewTextView.setText(movie.getOverview());
//        holder.releaseDateTextView.setText(movie.getReleaseDate());
//        holder.ratingTextView.setText(movie.getRating());
        Picasso.with(mContext).load(movie.getPoster()).into(holder.posterImageView);

        return view;
    }

    private static class ViewHolder {
        TextView titleTextView;
//        TextView overviewTextView;
//        TextView releaseDateTextView;
//        TextView ratingTextView;
        ImageView posterImageView;
    }
}
