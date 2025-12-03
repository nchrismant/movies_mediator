package databaseClient;

import entity.Actor;
import entity.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseClient {

    private String input;
    private String year;

    private Actor actor;

    public DatabaseClient(String input) {
        this.input = input;
    }

    public DatabaseClient(Actor actor) {
        this.actor = actor;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int countMovie() {
        int nbMovies = 0;
        try {
            String selectMovieQuery = "SELECT COUNT(*) AS nb_movies FROM movies m WHERE m.title = ?";

            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectMovieQuery);

            preparedStatement.setString(1, input);

            ResultSet result = preparedStatement.executeQuery();
            result.next();
            nbMovies = result.getInt("nb_movies");

            preparedStatement.close();

        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return nbMovies;
    }

    public String readMovieYears() {
        String year = null;
        try {
            String selectMovieQuery;
            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement;

            selectMovieQuery = "SELECT GROUP_CONCAT(YEAR(STR_TO_DATE(release_date, '%m/%d/%Y'))) as release_dates FROM movies m WHERE m.title = ?";
            preparedStatement = dbConnection.prepareStatement(selectMovieQuery);

            preparedStatement.setString(1, input);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                year = result.getString("release_dates");
            }

            preparedStatement.close();

        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return year;
    }

    public Movie readMovie() {
        Movie readMovie = new Movie();
        try {
            String selectMovieQuery;
            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement;

            if (year != null) {
                selectMovieQuery = "SELECT * FROM movies AS m WHERE m.title = ? AND YEAR(STR_TO_DATE(m.release_date, '%m/%d/%Y')) = ?";
                preparedStatement = dbConnection.prepareStatement(selectMovieQuery);
                preparedStatement.setString(2, year);
            } else {
                selectMovieQuery = "SELECT * FROM movies AS m WHERE m.title = ?";
                preparedStatement = dbConnection.prepareStatement(selectMovieQuery);
            }

            preparedStatement.setString(1, input);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                readMovie.setReleaseDate(result.getString("release_date"));
                readMovie.setTitle(result.getString("title"));
                readMovie.setGenre(result.getString("genre"));
                readMovie.setDistributor(result.getString("distributor"));
                readMovie.setBudget(result.getString("budget"));
                readMovie.setDomesticGross(result.getString("domestic_gross"));
                readMovie.setWorldwideGross(result.getString("worldwide_gross"));
            }

            preparedStatement.close();

        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }
        return readMovie;
    }

    public void readMovieActor() {
        ArrayList<Movie> movies = actor.getMovies();
        ArrayList<Movie> moviesToAdd = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for(Movie movie : movies) {
            titles.add(movie.getTitle());
        }
        String titleList = String.join(",", Collections.nCopies(titles.size(), "?"));
        try {
            String selectMovieQuery;
            Connection dbConnection = JdbcConnection.getConnection();
            PreparedStatement preparedStatement;

            selectMovieQuery = "SELECT release_date, title, genre, distributor FROM movies AS m WHERE m.title IN (" + titleList + ")";
            preparedStatement = dbConnection.prepareStatement(selectMovieQuery);

            for (int i = 0; i < titles.size(); i++) {
                preparedStatement.setString(i + 1, titles.get(i));
            }

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()){
                //update movie
                int titleIndex = indexOfTitle(actor.getMovies(), result.getString("title"));
                if (titleIndex != -1){
                    Movie movie = actor.getMovies().get(titleIndex);

                    // If it exists at least 2 movies with the same name, release date will be null. If it isn't null,
                    // we split the str to see if it matches, if yes we update the release date to right format
                    if (movie.getReleaseDate() == null){
                        movie.setReleaseDate(result.getString("release_date"));
                    }
                    else if (movie.getReleaseDate().equals(result.getString("release_date").substring(
                            movie.getReleaseDate().lastIndexOf("/") + 1))){
                        movie.setReleaseDate(result.getString("release_date"));
                    }

                    movie.setTitle(result.getString("title"));
                    movie.setGenre(result.getString("genre"));
                    movie.setDistributor(result.getString("distributor"));
                    actor.getMovies().set(titleIndex, movie);
                }
            }

            preparedStatement.close();

        } catch (SQLException se) {
            System.err.println(se.getMessage());
        }
    }

    public int indexOfTitle(ArrayList<Movie> movies, String title) {
        for(Movie movie : movies) {
            if(movie.getTitle().equals(title)) {
                int index = movies.indexOf(movie);
                return index;
            }
        }
        return -1;
    }
}
