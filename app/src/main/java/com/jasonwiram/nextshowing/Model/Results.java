package com.jasonwiram.nextshowing.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Results {
    private List<Movie> mMovies = new ArrayList<>();
    private Genre mGenres = new Genre();
    public List<String> mFilterGenres = new ArrayList<>();

//    public Results() {
//        for (Integer key : mGenres.genreIds.keySet()) {
//            mFilterGenres.add(Integer.toString(key));
//        }
//    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies (String jsonData, String tvOrMovie) throws JSONException {

        JSONObject resultsData = new JSONObject(jsonData);
        JSONArray results = resultsData.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject jsonMovie = results.getJSONObject(i);
            Movie movie = new Movie();

            if (tvOrMovie == "Movie") {
                movie.setTitle(jsonMovie.getString("title"));
                movie.setReleaseDate(jsonMovie.getString("release_date"));
            } else {
                movie.setTitle(jsonMovie.getString("name"));
                movie.setReleaseDate(jsonMovie.getString("first_air_date"));
            }

            movie.setOverview(jsonMovie.getString("overview"));
            movie.setPoster(jsonMovie.getString("poster_path"));
            movie.setRating(jsonMovie.getDouble("vote_average"));
            movie.setPopularity(jsonMovie.getDouble("popularity"));
            movie.setId(jsonMovie.getInt("id"));

            JSONArray genresData = jsonMovie.getJSONArray("genre_ids");
            String[] genres = new String[genresData.length()];
            for (int j = 0; j < genresData.length(); j++) {
                genres[j] = mGenres.genreIds.get(genresData.getInt(j));
            }
            movie.setGenres(genres);

            mMovies.add(movie);
        }
    }

    public void clearResults() {
        mMovies.clear();
    }

    public String filterGenresAsString() {
        String[] stringArray = new String[mFilterGenres.size()];
        mFilterGenres.toArray(stringArray);
        String genreString = Arrays.toString(stringArray);
        return genreString.substring(1, genreString.length()-1).replaceAll("\\s+","");
    }
}
