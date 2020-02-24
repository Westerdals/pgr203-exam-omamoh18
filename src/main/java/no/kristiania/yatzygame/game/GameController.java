package no.kristiania.yatzygame.game;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpServer;
import no.kristiania.yatzygame.category.CategoryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameController implements HttpController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final GameDao gameDao;

    public GameController(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {
        try {
            if (requestAction.equals("POST")) {

                query = HttpServer.parseQueryString(requestBody);

                Game game = new Game();
                String dicelist = query.get("diceList");
                //Regex used to allow input with comma and whitespace
                List<Integer> lst = Arrays.stream(dicelist.split("[\\\\s, ]+")).map(Integer::parseInt).collect(Collectors.toList());
                for (int n:
                     lst) {
                    System.out.println(n);
                }

                //Used for calculate score parameter
                String fetchCategory = query.get("categoryName");
                game.setPlayerName(query.get("playerName"));
                game.setCategory(query.get("categoryName"));
                game.setDiceSequence(query.get("diceList"));
                game.setScore(game.calculateScore(fetchCategory, lst));

                gameDao.insert(game);

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
        return gameDao.listAll().stream()
                .map(p -> String.format("<option id='%s'>%s</option>", p.getId(), p.getDescription()))
                .collect(Collectors.joining(""));
    }
}
