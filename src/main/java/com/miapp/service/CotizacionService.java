package com.miapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CotizacionService {
    
    @Autowired
    private DistanceService distanceService;
    
    // Tarifas base
    private static final double TARIFA_POR_KM = 0.80; // $0.80 por km
    private static final double TARIFA_POR_KG = 0.50; // $0.50 por kg
    private static final double TARIFA_POR_M3 = 0.80; // $0.80 por m³
    private static final double CARGO_URGENCIA = 0.15; // 15% adicional
    
    public Map<String, Object> calcularCotizacion(String origen, String destino, 
                                                 double peso, double largo, double ancho, double alto, 
                                                 boolean urgente) {
        try {
            // Calcular distancia
            double distancia = distanceService.getDistanceInKm(origen, destino);
            
            // Calcular costos base
            double costoDistancia = distancia * TARIFA_POR_KM;
            double costopeso = peso * TARIFA_POR_KG;
            
            // Calcular volumen y costo por volumen
            double volumen = (largo * ancho * alto) / 1000000; // cm³ a m³
            double costoVolumen = volumen * TARIFA_POR_M3;
            
            // Usar el mayor entre costo por peso y costo por volumen
            double costoCarga = Math.max(costopeso, costoVolumen);
            
            // Calcular subtotal
            double subtotal = costoDistancia + costoCarga;
            
            // Aplicar cargo por urgencia si es necesario
            double cargoUrgencia = urgente ? subtotal * CARGO_URGENCIA : 0;
            
            // Calcular total
            double total = subtotal + cargoUrgencia;
            
            // Calcular días estimados (basado en distancia)
            int diasEstimados = calcularDiasEstimados(distancia);
            
            // Generar transportistas disponibles (simulado)
            int transportistasDisponibles = generarTransportistasDisponibles();
            
            // Fecha de validez (24 horas)
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime validez = ahora.plusHours(24);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("totalCost", Math.round(total * 100.0) / 100.0);
            resultado.put("baseDistance", distancia);
            resultado.put("distanceCost", Math.round(costoDistancia * 100.0) / 100.0);
            resultado.put("weightCost", Math.round(costopeso * 100.0) / 100.0);
            resultado.put("volumeCost", Math.round(costoVolumen * 100.0) / 100.0);
            resultado.put("urgencyCost", Math.round(cargoUrgencia * 100.0) / 100.0);
            resultado.put("estimatedDays", diasEstimados);
            resultado.put("availableDrivers", transportistasDisponibles);
            resultado.put("validUntil", validez.format(formatter));
            resultado.put("volume", Math.round(volumen * 1000.0) / 1000.0);
            
            return resultado;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al calcular la cotización: " + e.getMessage());
        }
    }
    
    private int calcularDiasEstimados(double distancia) {
        if (distancia <= 100) return 1;
        if (distancia <= 300) return 2;
        if (distancia <= 600) return 3;
        if (distancia <= 1000) return 4;
        return 5;
    }
    
    private int generarTransportistasDisponibles() {
        Random random = new Random();
        return random.nextInt(15) + 5; // Entre 5 y 20 transportistas
    }
} 