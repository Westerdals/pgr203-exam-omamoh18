package no.kristiania.yatzygame.category;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryController implements HttpController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryDao categoryDao;

    public CategoryController(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {
        try {
            if (requestAction.equals("POST")) {
                query = HttpServer.parseQueryString(requestBody);
                String number = query.get("diceList");
                //Regex used to allow input with comma and whitespace
                List<Integer> lst = Arrays.stream(number.split("[\\\\s, ]+")).map(Integer::parseInt).collect(Collectors.toList());
                for (int n:lst) {
                    System.out.println(n);
                }

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080/\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
                return;
            }

            int statusCode = 200;
            String body = getBody();
            int contentLength = body.length();
            String contentType = "text/html";

            outputStream.write(("HTTP/1.0 " + statusCode + " OK\r\n" +
                    "Content-type: " + contentType + "\r\n" +
                    "Content-length: " + contentLength + "\r\n" +
                    "Connection: close \r\n" +
                    "\r\n" +
                    body).getBytes());
        } catch (SQLException e) {
            int statusCode = 500;
            logger.error("While handling request {}", requestPath, e);
            String body = e.toString();
            outputStream.write(("HTTP/1.0 " + statusCode + " OK\r\n" +
                    "Content-type: " + "text/html" + "\r\n" +
                    "Content-length: " + body.length() + "\r\n" +
                    "Connection: close \r\n" +
                    "\r\n" +
                    body).getBytes());

            e.printStackTrace();
        }

    }

    public String getBody() throws SQLException {
        return categoryDao.listAll().stream()
                .map(p -> String.format("<option id='%s'>%s</option>", p.getId(), p.getName()))
                .collect(Collectors.joining(""));
    }
}
