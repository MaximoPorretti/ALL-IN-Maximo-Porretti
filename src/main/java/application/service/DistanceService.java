package application.service;

import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DistanceService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.api.key}")
    private String apiKey;

    public double getDistanceInKm(String origin, String destination) throws Exception {
        String encodedOrigin = URLEncoder.encode(origin, StandardCharsets.UTF_8);
        String encodedDestination = URLEncoder.encode(destination, StandardCharsets.UTF_8);
        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
            encodedOrigin,
            encodedDestination,
            apiKey
        );

        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        JSONArray routes = json.getJSONArray("routes");
        if (routes.length() == 0) {
            throw new Exception("No se encontró ruta entre los puntos.");
        }
        JSONObject route = routes.getJSONObject(0);
        JSONArray legs = route.getJSONArray("legs");
        JSONObject leg = legs.getJSONObject(0);
        JSONObject distance = leg.getJSONObject("distance");

        // Devuelve la distancia en kilómetros
        return distance.getDouble("value") / 1000.0;
    }

}
