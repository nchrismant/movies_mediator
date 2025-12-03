package mediator;

import HTTPClient.HTTPClient;
import databaseClient.DatabaseClient;
import entity.Actor;
import entity.Movie;
import sparQL.SparqlClient;

import java.io.IOException;
import java.util.Scanner;

public class Mediator {
    private String userInput;
    private int intention;

    public Mediator(String userInput, int intention) {
        this.userInput = userInput;
        this.intention = intention;
    }

    public void filmMediator(Scanner scanner) throws IOException {
        DatabaseClient databaseClient = new DatabaseClient(userInput);
        int nb_movies = databaseClient.countMovie();
        String year = "";
        if(nb_movies > 1) {
            String years = databaseClient.readMovieYears();
            System.out.println("Il existe plusieurs films nommés : " + userInput);
            System.out.println("Renseignez son année de sortie parmi (" + years + ") :");
            year = scanner.nextLine();
            databaseClient.setYear(year);
        }

        // Request Database for infos on movie
        Movie movie = databaseClient.readMovie();
        HTTPClient httpClient = new HTTPClient(movie);
        httpClient.readResume();

        // Request Sparql for additional infos on the movie
        SparqlClient sparqlClient =  new SparqlClient(userInput, year, intention);
        sparqlClient.request();
        sparqlClient.generateInfosFilms(movie);

        // Display result
        System.out.println(movie.toStringMovie());
    }

    public void actorMediator(){
        //Requete SparQL pour récupérer la liste des films de l'acteur
        SparqlClient sparqlClient =  new SparqlClient(userInput, intention);
        sparqlClient.request();
        Actor actor = sparqlClient.generateInfosActor();

        //Requete avec JDBC pour récupérer les infos de chaque film de l'acteur.
        DatabaseClient databaseClient = new DatabaseClient(actor);
        databaseClient.readMovieActor();

        // Display result
        System.out.println(actor.toString());
    }
}
