package entity;

import java.util.ArrayList;

public class Actor {
    private String actorName;
    private ArrayList<Movie> movies = new ArrayList<>();
    public Actor(String actorName, ArrayList<Movie> movies){
        this.actorName = actorName;
        this.movies = movies;
    }

    public Actor(String actorName){
        this.actorName = actorName;
    };

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Movie movie : movies) {
            // Concaténer les attributs du film dans la chaîne de caractères
            result.append(movie.toStringActor()).append(" \n");
        }

        return "Acteur : " + actorName + " \n" +
                "Films : \n\n" + result;
    }
}
