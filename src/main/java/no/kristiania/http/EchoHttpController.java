package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class EchoHttpController implements HttpController {

    public void handle(String requestAction, String requestPath, Map<String, String> query, String body, OutputStream outputStream) throws IOException {
        if(requestAction.equals("POST")){
            query = HttpServer.parseQueryString(body);
        }


        String statusCode = query.getOrDefault("status", "200");
        String contentType = query.getOrDefault("content-type", "text/plain");
        String location = query.get("location");
        String responseBody = query.getOrDefault("body", "Hello world!");
        int contentLength = responseBody.length();

        outputStream.write(("HTTP/1.0 " + statusCode + " OK\r\n" +
                "Content-type: " + contentType + "\r\n" +
                "Content-length: " + contentLength + "\r\n" +
                "Connection: close\r\n" +
                (location != null ? "Location: " + location + "\r\n" : "") +
                "\r\n" +
                responseBody).getBytes());
    }

}
