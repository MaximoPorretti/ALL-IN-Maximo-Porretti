package application.controller;

import application.dtos.QuoteRequestDTO;
import application.dtos.QuoteResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {

    @PostMapping("/calculate")
    public ResponseEntity<QuoteResultDTO> calculateQuote(@RequestBody QuoteRequestDTO request) {
        // Lógica de ejemplo para calcular la cotización
        double distance = 400; // Simulado, deberías calcularlo con una API real
        double distanceCost = distance * 50; // $50 por km
        double weightCost = request.getWeight() * 0.5; // $0.5 por kg
        double volume = (request.getHeight() * request.getWidth() * request.getLength()) / 1000000.0; // m3
        double volumeCost = volume > 0 ? volume * 0.8 : 0; // $0.8 por m3
        double urgencyCost = request.isUrgency() ? (distanceCost + weightCost + volumeCost) * 0.15 : 0;
        double totalCost = distanceCost + weightCost + volumeCost + urgencyCost;

        QuoteResultDTO result = new QuoteResultDTO();
        result.setTotalCost(totalCost);
        result.setDistanceCost(distanceCost);
        result.setWeightCost(weightCost);
        result.setVolumeCost(volumeCost);
        result.setUrgencyCost(urgencyCost);
        result.setEstimatedDays(2); // Simulado
        result.setBaseDistance(distance);
        result.setAvailableDrivers(5); // Simulado
        result.setValidUntil(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptQuote(@RequestBody QuoteRequestDTO request) {
        // Aquí deberías guardar la cotización y generar un código de seguimiento
        String trackingCode = "TRK" + System.currentTimeMillis();
        return ResponseEntity.ok(trackingCode);
    }
} 