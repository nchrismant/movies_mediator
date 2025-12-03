package HTTPClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;

import entity.Movie;
import org.xml.sax.InputSource;
import javax.xml.xpath.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class HTTPClient {

    private static final String API_KEY = "52a7bcec";
    private static final String API_URL = "http://www.omdbapi.com/?apikey=" + API_KEY;

    private Movie movie;

    public HTTPClient(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void readResume() throws IOException {
        if (movie.getReleaseDate() != null) {
            String year = movie.getReleaseDate().substring(movie.getReleaseDate().lastIndexOf("/") + 1);
            String apiUrlWithQuery = API_URL + "&t=" + movie.getTitle() + "&y=" + year + "&plot=full" + "&r=xml";

            URL url = new URL(apiUrlWithQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder apiResponseBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                apiResponseBuilder.append(output);
            }
            br.close();
            conn.disconnect();

            String uri = apiResponseBuilder.toString();
            String request = "/root/movie/@plot";
            String summary = (String) XPath(uri, request, XPathConstants.STRING);

            movie.setResume(summary);
        }
    }
    private static Object XPath(String uri, String requete, QName typeRetour){
        //Le dernier param�tre indique le type de r�sultat souhait�
        //XPathConstants.STRING: cha�ne de caract�res (String)
        //XPathConstants.NODESET: ensemble de noeuds DOM (NodeList)
        //XPathConstants.NODE: noeud DOM (Node) - le premier de la liste
        //XPathConstants.BOOLEAN: bool�en (Boolean) - vrai si la liste n'est pas vide
        //XPathConstants.NUMBER: num�rique (Double) - le contenu du noeud s�lectionn� transform� en Double

        try{
            //Transformation en document DOM du contenu XML
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            DocumentBuilder parseur = fabrique.newDocumentBuilder();
            Document document = parseur.parse(new InputSource(new StringReader(uri)));

            //cr�ation de l'objet XPath
            XPathFactory xfabrique = XPathFactory.newInstance();
            XPath xpath = xfabrique.newXPath();

            //�valuation de l'expression XPath
            XPathExpression exp = xpath.compile(requete);
            return exp.evaluate(document, typeRetour);

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
