package application.service;

import application.dtos.QuoteRequestDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrackingService {
    public static class TrackingInfo {
        private String origin;
        private String destination;
        private String status;

        public TrackingInfo(String origin, String destination, String status) {
            this.origin = origin;
            this.destination = destination;
            this.status = status;
        }

        public String getOrigin() { return origin; }
        public String getDestination() { return destination; }
        public String getStatus() { return status; }
    }

    private final Map<String, TrackingInfo> data = new ConcurrentHashMap<>();

    public void addTracking(String code, QuoteRequestDTO request) {
        data.put(code, new TrackingInfo(
                request.getOrigin(),
                request.getDestination(),
                "Aceptado - Asignando transportista"
        ));
    }

    public TrackingInfo getTracking(String code) {
        return data.get(code);
    }
}
