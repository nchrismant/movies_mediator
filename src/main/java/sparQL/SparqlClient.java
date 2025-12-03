package sparQL;

import entity.Actor;
import entity.Movie;
import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SparqlClient {
    private String input;
    private String inputYear;
    private int querySelection;
    private List<QuerySolution> resultSet;

    public SparqlClient(String input, String inputYear, int querySelection) {
        this.input = input;
        this.inputYear = inputYear;
        this.querySelection = querySelection;
    }

    public SparqlClient(String input, int querySelection) {
        this.input = input;
        this.querySelection = querySelection;
    }

    public void request(){
        String queryString;

        if (querySelection == 1) {
            // Récupérer la liste des acteurs, producteurs, réalisateurs pour un film donné
            queryString = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?name ?directorName ?label (GROUP_CONCAT(DISTINCT ?starringNames; separator=\", \") AS ?actors) (GROUP_CONCAT(DISTINCT ?producerName; separator=\", \") AS ?producers)\n" +
                    "WHERE {\n" +
                    "  ?film a dbo:Film ;\n" +
                    "        dbp:name \""+ input +"\"@en ;\n" +
                    "        dbp:director ?director ;\n" +
                    "        dbo:producer ?producer;\n" +
                    "        dbo:starring ?starring ;\n" +
                    "        rdfs:label ?label .\n" +
                    "  FILTER (langMatches(lang(?label), \"en\"))\n" +
                    "  ?film foaf:name ?name .\n" +
                    "  ?director foaf:name ?directorName .\n" +
                    "  ?producer foaf:name ?producerName .\n" +
                    "  ?starring foaf:name ?starringNames .\n" +
                    "}\n" +
                    "GROUP BY ?name ?directorName ?label\n";
        }
        else {
            // récupérer la liste des films pour un acteur donné
            queryString = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                    "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?label (GROUP_CONCAT(DISTINCT ?directorName; SEPARATOR=\", \") AS ?directors) (GROUP_CONCAT(DISTINCT ?producerName; SEPARATOR=\", \") AS ?producers)\n" +
                    "WHERE {\n" +
                    "  ?film a dbo:Film ;\n" +
                    "       dbo:starring ?actor;\n" +
                    "       dbp:director ?director;\n" +
                    "       dbo:producer ?producer;\n" +
                    "       rdfs:label ?label .\n" +
                    "  FILTER (langMatches(lang(?label), \"en\"))\n" +
                    "  ?director foaf:name ?directorName.\n" +
                    "  ?producer foaf:name ?producerName.\n" +
                    "  ?actor foaf:name \"" + input + "\"@en .\n" +
                    "} \n" +
                    "GROUP BY ?label";
        }

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query)) {
            ResultSet results = qexec.execSelect();
            //ResultSet to list pour mieux le transformer en objets.
            this.resultSet = ResultSetFormatter.toList(results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("SparQL Request Done");
    }

    public void generateInfosFilms(Movie movie){
        for (QuerySolution line : this.resultSet){
            // Si le film a déja été créé, ie qu'il existe dans notre BD :
            String lineYear = extractDate(splitStrToList(line.get("?label").toString(), "\"").get(0));
            if (movie.getReleaseDate() != null){
                String movieYear = movie.getReleaseDate().substring(movie.getReleaseDate().lastIndexOf("/") + 1);
                // si le film est unique
                if (lineYear == null){
                   updateMovie(line, movie);
                }
                // Si le film existe plusieurs fois, on prend soin de prendre le bon
                else if (lineYear.equals(movieYear)) {
                    updateMovie(line, movie);
                }
            }
            // Si le film n'était pas dans notre BD, et qu'il correspond à la date donnée par l'utilisateur
            else if (Objects.equals(lineYear, inputYear) && !Objects.equals(inputYear, ""))  {
                movie.setReleaseDate(lineYear);
                updateMovie(line, movie);
            }
        }
    }

    private void updateMovie(QuerySolution line, Movie movie){
        movie.setActors(splitStrToList(line.get("?actors").toString(), ","));
        movie.setProducers(splitStrToList(line.get("?producers").toString(), ","));
        movie.setDirectors(splitStrToList(line.get("?directorName").toString(), ","));
    }

    public Actor generateInfosActor(){
        Actor actor = new Actor(input);
        for (QuerySolution line : this.resultSet){
            String title = splitStrToList(line.get("?label").toString(), "\"").get(0);
            title = extractTitle(splitStrToList(title, "@").get(0));
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setDirectors(splitStrToList(line.get("?directors").toString(), ","));
            movie.setProducers(splitStrToList(line.get("?producers").toString(), ","));

            //Check if date in label, if yes then it's duplicate film
            String date = extractDate(splitStrToList(line.get("?label").toString(), "\"").get(0));
            if (date != null) {
                movie.setReleaseDate(date);
            }

            actor.getMovies().add(movie);

        }
        System.err.println("sparQL actor info done");
        return actor;
    }

    private ArrayList<String> splitStrToList(String nameString, String separator){
        ArrayList<String> nameList = new ArrayList<String>();
        String[] namesArray = nameString.split(separator);
        for (String name : namesArray) {
            nameList.add(name.trim());
        }
        return nameList;
    }

    public static String extractDate(String text) {
        Pattern pattern = Pattern.compile("\\((\\d{4})\\s*film\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String extractTitle(String title) {
        String regex = "(.*)\\s+\\((.*)(film)?\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return title;
        }
    }

}
