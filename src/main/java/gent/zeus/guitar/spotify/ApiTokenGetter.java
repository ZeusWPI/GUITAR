package gent.zeus.guitar.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class ApiTokenGetter {
    // xhs post
    // https://accounts.spotify.com/api/token
    // content-type:application/x-www-form-urlencoded
    // --raw "grant_type=client_credentials&client_id=$SPOTI_ID&client_secret=$SPOTI_SECRET

    // TODO implement caching

    private static final RestClient REST_CLIENT = RestClient.create();

    public String getToken() throws SpotifyApiException {
        SpotifyAccessToken accessToken = REST_CLIENT.get()
                .uri("https://accounts.spotify.com/api/token")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(SpotifyAccessToken.class);

        if (accessToken == null) {
            throw new SpotifyApiException("response body was empty");
        }
        return accessToken.token();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record SpotifyAccessToken(
            @JsonProperty("access_token") String token,
            String tokenType,
            int expiresIn
    ) {
    }
}
