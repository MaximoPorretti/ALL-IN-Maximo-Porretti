package com.miapp.controller;

import com.miapp.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/quote")
@CrossOrigin(origins = "*")
public class QuoteController {
    
    @Autowired
    private CotizacionService cotizacionService;
    
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calcularCotizacion(@RequestBody Map<String, Object> request) {
        try {
            String origen = (String) request.get("origin");
            String destino = (String) request.get("destination");
            double peso = Double.parseDouble(request.get("weight").toString());
            double largo = request.get("length") != null ? Double.parseDouble(request.get("length").toString()) : 0;
            double ancho = request.get("width") != null ? Double.parseDouble(request.get("width").toString()) : 0;
            double alto = request.get("height") != null ? Double.parseDouble(request.get("height").toString()) : 0;
            boolean urgente = request.get("urgency") != null && (Boolean) request.get("urgency");
            
            Map<String, Object> resultado = cotizacionService.calcularCotizacion(
                origen, destino, peso, largo, ancho, alto, urgente
            );
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            Map<String, Object> error = Map.of(
                "error", "Error al calcular la cotización",
                "message", e.getMessage()
            );
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/accept")
    public ResponseEntity<Map<String, Object>> aceptarCotizacion(@RequestBody Map<String, Object> request) {
        try {
            // Generar código de seguimiento único
            String codigoSeguimiento = "ALI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            Map<String, Object> resultado = Map.of(
                "success", true,
                "message", "Cotización aceptada exitosamente",
                "codigoSeguimiento", codigoSeguimiento,
                "totalCost", request.get("totalCost"),
                "clienteId", request.get("clienteId"),
                "metodoPago", request.get("metodoPago")
            );
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            Map<String, Object> error = Map.of(
                "success", false,
                "error", "Error al aceptar la cotización",
                "message", e.getMessage()
            );
            return ResponseEntity.badRequest().body(error);
        }
    }
} 