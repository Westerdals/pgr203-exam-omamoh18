package no.kristiania.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static no.kristiania.http.HttpMessage.readHeaders;

public class HttpServer  {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static ServerSocket serverSocket;
    private String fileLocation;
    private HttpController defaultController = new FileHttpController(this);
    private Map<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        controllers.put("/echo", new EchoHttpController());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);
        server.setFileLocation("src/main/resources/");
        server.start();
    }

    public void start() {
        new Thread(this::run).start();
        logger.info("Started server on http://localhost:{}", getPort());

    }

    private void run() {
        while (true) {
            try (Socket socket = serverSocket.accept()) {

                String requestLine = HttpMessage.readLine(socket.getInputStream());
                if (requestLine.isBlank()) continue;
                logger.debug("Handling request: {}", requestLine);
                Map<String, String> headers = readHeaders(socket.getInputStream());
                String body = HttpMessage.readBody(headers, socket.getInputStream());


                String requestAction = requestLine.split(" ")[0];
                String requestTarget = requestLine.split(" ")[1];
                int questionPos = requestTarget.indexOf('?');
                Map<String, String> query = getQueryParameter(requestTarget);
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);

                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestAction, requestPath, query, body, socket.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static Map<String, String> getQueryParameter(String requestTarget) {

        int questionPos = requestTarget.indexOf('?');
        if (questionPos > 0) {
            String query = URLDecoder.decode(requestTarget.substring(questionPos + 1), StandardCharsets.UTF_8);
            return parseQueryString(query);
        }
        return new HashMap<>();
    }

    public static Map<String, String> parseQueryString(String query) {
        Map<String, String> requestParameter = new HashMap<>();
        for (String parameter : query.split("&")) {
            int equalPos = parameter.indexOf('=');
            String parameterValue = parameter.substring(equalPos + 1);
            String decodedParamValue = URLDecoder.decode(parameterValue, StandardCharsets.UTF_8);
            String parameterName = parameter.substring(0, equalPos);
            requestParameter.put(parameterName, decodedParamValue);
        }
        return requestParameter;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void addController(String requestPath, HttpController controller) {
        controllers.put(requestPath, controller);
    }

}
