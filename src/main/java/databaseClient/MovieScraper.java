package databaseClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieScraper {

    private static final String[] GENRES = { "Adventure", "Comedy", "Drama", "Action", "Thriller-or-Suspense",
            "Romantic-Comedy" };
    private static final String BASE_URL = "http://www.the-numbers.com";
    private static final int START_YEAR = 2000;
    private static final int END_YEAR = 2015;

    public static void main(String[] args) throws IOException {

        for (String genre : GENRES) {
            String filename = genre + ".csv";
            try (FileWriter writer = new FileWriter(filename)) {
                StringBuilder sb = new StringBuilder();
                sb.append("title;genre;distributor;year\n");

                for (int i = START_YEAR; i <= END_YEAR; i++) {
                    String url = BASE_URL + "/market/" + i + "/genre/" + genre;

                    Document doc = Jsoup.connect(url).get();
                    Elements rows = doc.select("table > tbody > tr:not(:first-child):not(:last-child)");

                    for (Element row : rows) {
                        String title = row.select("td > b > a[href*=/movie/]").text().replace("’", "'");
                        title = title.replaceAll("\\s*\\(\\d{4}\\)$", "").trim();
                        String movieUrl = BASE_URL + row.select("td > b > a[href*=/movie/]").attr("href");
                        if (title.length() > 20) {
                            Document movieDoc = Jsoup.connect(movieUrl).get();
                            String fullTitle = movieDoc.select("h1").first().text().replace("’", "'");
                            title = fullTitle.replaceAll("\\s*\\(\\d{4}\\)$", "").trim();
                        }
                        String distributor = row.select("td > a[href*=/market/distributor/]").text();
                        if (distributor.length() > 15) {
                            Document movieDoc = Jsoup.connect(movieUrl).get();
                            distributor = movieDoc.select("a[href*=/market/distributor/]").get(1).text();
                        }
                        if (!title.isEmpty()) {
                            String dateString = row.select("td > a[href*=/box-office-chart/daily/]").text();
                            SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy");
                            String formattedDate = "";
                            try {
                                Date date = inputFormat.parse(dateString);
                                formattedDate = outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            sb.append(title).append(";").append(genre).append(";").append(distributor).append(";")
                                    .append(formattedDate).append("\n");
                        }
                    }
                }
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

