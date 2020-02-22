package no.kristiania.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class HttpServerTest {

    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpServer(0);
        server.start();
    }

    @Test
    void shouldReturnStatusCode200() throws IOException {
        HttpClientResponse response = executeLocalRequest("/echo");
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    void shouldReturnStatusCode401() throws IOException {
        HttpClientResponse response = executeLocalRequest("/echo?status=401");
        assertThat(response.getStatusCode()).isEqualTo(401);
    }

    @Test
    void shouldReturnHeaders() throws IOException {
        HttpClientResponse response = executeLocalRequest("/echo?status=302&location=http://www.example.com");
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getHeader("location")).isEqualTo("http://www.example.com");
    }

    @Test
    void shouldReturnBody() throws IOException {
        HttpClientResponse response = executeLocalRequest("/echo?body=HelloWorld");
        assertThat(response.getBody()).isEqualTo("HelloWorld");
    }

    @Test
    void shouldReturnUnknownPath() throws IOException {
        HttpClientResponse response = executeLocalRequest("/no/files/here");
        assertThat(response.getStatusCode()).isEqualTo(404);
    }


    @Test
    void shouldParseMultipleParameters() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?content-type=text/html&body=foobar");
        HttpClientResponse response = client.execute("GET");
        assertThat(response.getHeader("content-type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }

    @Test
    void shouldParsePostParameters() throws IOException {
        //+ should be decoded to whitespace
        String formBody = "content-type=text/html&body=foo+bar";
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        client.setRequestHeader("content-type", "application/x-www-form-urlencoded");
        client.setBody(formBody);
        HttpClientResponse response = client.execute("POST");
        assertThat(response.getHeader("content-type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foo bar");
    }

    @Test
    void shouldDefaultToIndexHtml() throws IOException {
        server.setFileLocation("target/");
        String fileContent = "<body>Some random string " + System.currentTimeMillis() + "</body>";
        Files.writeString(Paths.get("target", "index.html"), fileContent);

        HttpClientResponse response = executeLocalRequest("/");
        assertThat(response.getBody()).isEqualTo(fileContent);
    }

    public HttpClientResponse executeLocalRequest(String s) throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), s);
        return client.execute("GET");
    }

}