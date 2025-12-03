import HTTPClient.HTTPClient;
import databaseClient.DatabaseClient;
import entity.Actor;
import entity.Movie;
import mediator.Mediator;
import org.apache.jena.base.Sys;
import sparQL.SparqlClient;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleInterface {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---------------------UwU--------------------");
        System.out.println("---Bienvenudo dans votre médiator préféré---");
        System.out.println("---------------------UwU--------------------");

        System.out.println("Que voulez-vous faire ?");
        System.out.println("1. Rechercher un film");
        System.out.println("2. Rechercher un acteur");

        int intention = scanner.nextInt();
        scanner.nextLine();

        if (intention == 1) {
            System.out.println("Vous avez choisi de rechercher un film");
            System.out.println("Renseignez son titre :");

            // Code pour la recherche de film
            String userInput = scanner.nextLine();
            Mediator mediator = new Mediator(userInput, intention);
            mediator.filmMediator(scanner);

        } else if (intention == 2) {
            System.out.println("Vous avez choisi de rechercher un acteur");
            System.out.println("Renseignez son nom :");
            String userInput = scanner.nextLine();

            Mediator mediator = new Mediator(userInput, intention);
            mediator.actorMediator();

        } else {
            System.out.println("Choix invalide");
        }
    }
}

