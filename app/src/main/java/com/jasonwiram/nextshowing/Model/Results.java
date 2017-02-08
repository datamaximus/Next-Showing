package com.jasonwiram.nextshowing.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Results {
    private List<Movie> mMovies = new ArrayList<>();
    private Genre mGenres = new Genre();

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies (String jsonData) throws JSONException {
        JSONObject resultsData = new JSONObject(jsonData);
        JSONArray results = resultsData.getJSONArray("results");

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

            mMovies.add(movie);
        }
    }
}
