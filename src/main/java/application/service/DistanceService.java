package application.service;

import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class DistanceService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "AIzaSyDvT66VQ2YuyfZhp1ymuf8-H_3eo6vT9oU";

    public double getDistanceInKm(String origin, String destination) throws Exception {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
            origin.replace(" ", "+"),
            destination.replace(" ", "+"),
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

    public static void main(String[] args) {
        DistanceService service = new DistanceService();
        try {
            double distancia = service.getDistanceInKm("Buenos Aires", "Cordoba");
            System.out.println("Distancia entre Buenos Aires y Córdoba: " + distancia + " km");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}