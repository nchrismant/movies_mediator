package entity;

import java.util.ArrayList;

public class Movie {

    public String releaseDate;
    public String title;
    public String genre;
    public String distributor;
    public String budget;
    public String domesticGross;
    public String worldwideGross;

    public String resume;
    public ArrayList<String> actors;
    public ArrayList<String> producers;
    public ArrayList<String> directors;


    public Movie(String releaseDate, String title, String genre, String distributor, String budget, String domesticGross, String worldwideGross, String resume, ArrayList<String> actors, ArrayList<String> producers, ArrayList<String> directors) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.genre = genre;
        this.distributor = distributor;
        this.budget = budget;
        this.domesticGross = domesticGross;
        this.worldwideGross = worldwideGross;
        this.resume = resume;
        this.actors = actors;
        this.producers = producers;
        this.directors = directors;
    }

    public Movie(String releaseDate, String title, String genre, String distributor, ArrayList<String> producers, ArrayList<String> directors) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.genre = genre;
        this.distributor = distributor;
        this.producers = producers;
        this.directors = directors;
    }

    public Movie(String releaseDate, String title, ArrayList<String> actors, ArrayList<String> producers, ArrayList<String> directors) {
        this.releaseDate = releaseDate;
        this.title = title;
        this.actors = actors;
        this.producers = producers;
        this.directors = directors;
    }

    public Movie() {
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDomesticGross() {
        return domesticGross;
    }

    public void setDomesticGross(String domesticGross) {
        this.domesticGross = domesticGross;
    }

    public String getWorldwideGross() {
        return worldwideGross;
    }

    public void setWorldwideGross(String worldwideGross) {
        this.worldwideGross = worldwideGross;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getProducers() {
        return producers;
    }

    public void setProducers(ArrayList<String> producers) {
        this.producers = producers;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    public String toStringMovie() {
        return "Film : \n" +
                "Titre : '" + title + '\'' + "\n" +
                "Date de sortie : '" + releaseDate + '\'' + "\n" +
                "Genre : '" + genre + '\'' + "\n" +
                "Distributeur : '" + distributor + '\'' + "\n" +
                "Budget : '" + budget + '\'' + "\n" +
                "US Box Office : '" + domesticGross + '\'' + "\n" +
                "World Box Office : '" + worldwideGross + '\'' + "\n" +
                "Acteurs : " + actors + "\n" +
                "Réalisateur(s) : " + directors + "\n" +
                "Résumé : '" + resume + '\'' + "\n" +
                "\n";
    }

    public String toStringActor() {
        return "Film : \n" +
                "Titre : '" + title + '\'' + "\n" +
                "Date de sortie : '" + releaseDate + '\'' + "\n" +
                "Genre : '" + genre + '\'' + "\n" +
                "Distributeur : '" + distributor + '\'' + "\n" +
                "Producteur(s) : " + producers + "\n" +
                "Réalisateur(s) : " + directors + "\n";
    }
}