import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.CoreMatchers.not;
import org.hamcrest.CoreMatchers;

public class APITests {
    private static String searchOne = "12345";
    private static String searchPersonal = "Personal";
    private static String notFound = "No results were found for the query:";
    private String userName = "anikina.katerina@gmail.com";

    @Test
    public void LoginValidUser() throws URISyntaxException, IOException, InterruptedException {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(HomePage.loginUrl))
                .timeout(Duration.of(5, SECONDS))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("uid=jsmith&passw=DEMO1234&btnSubmit=Login"))
                .build();

        // act
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        int responseStatusCode = response.statusCode();

        System.out.println("httpPostResponse: " + responseBody);
        System.out.println("httpPostResponse status code: " + responseStatusCode);

        // assert
        Assert.assertEquals(200, responseStatusCode);

    }

    @Test
    public void SearchOne() throws URISyntaxException, IOException, InterruptedException {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(HomePage.baseUrl + "search.jsp?query=" + this.searchOne))
                .timeout(Duration.of(5, SECONDS))
                .GET()
                .build();

        // act
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        int responseStatusCode = response.statusCode();

        System.out.println("httpGetResponse: " + responseBody);
        System.out.println("httpGetResponse status code: " + responseStatusCode);

        // assert
        Assert.assertEquals(200, responseStatusCode);
        Assert.assertThat(responseBody, CoreMatchers.containsString(this.searchOne));
        Assert.assertThat(responseBody, CoreMatchers.containsString(this.notFound));
    }

    @Test
    public void SearchPersonal() throws URISyntaxException, IOException, InterruptedException {
        // given
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(HomePage.baseUrl + "search.jsp?query=" + this.searchPersonal))
                .timeout(Duration.of(5, SECONDS))
                .GET()
                .build();

        // act
        HttpResponse<String> response = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        int responseStatusCode = response.statusCode();

        System.out.println("httpGetResponse: " + responseBody);
        System.out.println("httpGetResponse status code: " + responseStatusCode);

        // assert
        Assert.assertEquals(200, responseStatusCode);
        Assert.assertThat(responseBody, CoreMatchers.containsString(this.searchPersonal));
        Assert.assertThat(responseBody, not(CoreMatchers.containsString(this.notFound)));
    }

    @Test
    public void LoginCzechitas() throws URISyntaxException, IOException, InterruptedException {
        CookieManager cm = new CookieManager();
        CookieHandler.setDefault(cm);
        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        // given
        HttpRequest requestLogin = HttpRequest.newBuilder()
                .uri(new URI(HomePage.czechitasUrl + "logon.html"))
                .timeout(Duration.of(5, SECONDS))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("userName=" + this.userName + "&password=katok&storeCode=DEFAULT"))
                .build();

        // act
        HttpResponse<String> responseLogin = HttpClient
                .newBuilder()
                .cookieHandler(cm)
                .proxy(ProxySelector.getDefault())
                .build()
                .send(requestLogin, HttpResponse.BodyHandlers.ofString());

        int responseStatusCode = responseLogin.statusCode();
        String responseBody = responseLogin.body();
        HttpHeaders responseHeaders = responseLogin.headers();
        List<String> cookies = responseHeaders.allValues("set-cookie");
        //String globalSessionID = cookies.get(0);
        //String sessionUser = cookies.get(1);

        System.out.println("httpPostResponse: " + responseBody);
        System.out.println("httpPostResponse cookies: " + cookies);
        //System.out.println("session data: " + globalSessionID);
        //System.out.println("session user data: " + sessionUser);
        System.out.println("httpPostResponse status code: " + responseStatusCode);

        // assert
        Assert.assertEquals(200, responseStatusCode);
        Assert.assertThat(responseBody, CoreMatchers.containsString(userName));


        HttpRequest requestOrders = HttpRequest.newBuilder()
                .uri(new URI(HomePage.czechitasUrl + "orders.html"))
                .timeout(Duration.of(5, SECONDS))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();
        // act
        HttpResponse<String> responseOrders = HttpClient
                .newBuilder()
                .cookieHandler(cm)
                .proxy(ProxySelector.getDefault())
                .build()
                .send(requestOrders, HttpResponse.BodyHandlers.ofString());

        int responseOrdersStatusCode = responseOrders.statusCode();
        String responseOrdersBody = responseOrders.body();
        HttpHeaders responseOrdersHeaders = responseOrders.headers();

        System.out.println("httpGetResponse cookies: " + responseOrdersHeaders.toString());
        System.out.println("httpGetResponse status code: " + responseOrdersStatusCode);
        System.out.println("httpGetResponse: " + responseOrdersBody);

        // assert
        Assert.assertEquals(200, responseOrdersStatusCode);
        Assert.assertThat(responseBody, CoreMatchers.containsString("List of orders"));
    }
}
