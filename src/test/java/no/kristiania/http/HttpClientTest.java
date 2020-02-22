package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

    @Test
    void shouldExecuteRequests() throws IOException {
        HttpClientResponse response = makeEchoRequest("/echo");
        assertEquals(response.getStatusCode(),200);

    }


    @Test
    void shouldReadStatusCode() throws IOException {
        HttpClientResponse response = makeEchoRequest("/echo?status=401");
        assertEquals(response.getStatusCode(), 401);
    }

    @Test
    void shouldReturnHeaders() throws IOException{
        HttpClientResponse response = makeEchoRequest("/echo?content-type=text/css");
        assertThat(response.getHeaders()).containsEntry("content-type", "text/css; charset=utf-8");
    }

    @Test
    void shouldReadBody() throws IOException {
        HttpClientResponse response = makeEchoRequest("/echo?body=hello+kont+helg");
        assertThat(response.getHeaders()).containsEntry("content-length", "15");
        assertThat(response.getContentLength()).isEqualTo(15);
        assertThat(response.getBody()).isEqualTo("hello kont helg");
    }


    private HttpClientResponse makeEchoRequest(String requestTarget) throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, requestTarget);
        return client.execute("GET");
    }
}
