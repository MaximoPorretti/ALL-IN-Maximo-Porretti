package com.miapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.model.tarifas.*;
import com.miapp.repository.CotizacionRepository;
import com.miapp.repository.TarifaRepository;

@Service
public class CotizacionService {
    
    @Autowired
    private DistanceService distanceService;
    @Autowired
    private CotizacionRepository cotizacionRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    
    // Tarifas base
    private static final double TARIFA_POR_KM = 0.80; // $0.80 por km
    private static final double TARIFA_POR_KG = 0.50; // $0.50 por kg
    private static final double TARIFA_POR_M3 = 0.80; // $0.80 por m³
    private static final double CARGO_URGENCIA = 0.15; // 15% adicional
    
    public Cotizacion calcularYCotizar(String origen, String destino,
                                       double peso, double largo, double ancho, double alto,
                                       boolean urgente) throws Exception {
        double distancia = distanceService.getDistanceInKm(origen, destino);
        double volumen = (largo * ancho * alto) / 1000000; // cm³ a m³

        // Crear tarifas
        TarifaPorKg tarifaKg = new TarifaPorKg(0.50, peso);
        tarifaKg.setValor(tarifaKg.calcularTarifaKg());
        TarifaPorEspacio tarifaEspacio = new TarifaPorEspacio(0.80, new com.miapp.model.cargas.Carga(largo, ancho, alto, peso));
        tarifaEspacio.setValor(tarifaEspacio.calcularTarifaEspacio());
        TarifaPorKm tarifaKm = new TarifaPorKm(0.80, distancia);
        tarifaKm.setValor(tarifaKm.calcularTarifaKm());

        // Calcular costos
        double costoDistancia = tarifaKm.calcularTarifaKm();
        double costoPeso = tarifaKg.calcularTarifaKg();
        double costoVolumen = tarifaEspacio.calcularTarifaEspacio();
        double costoCarga = costoPeso + costoVolumen; // Sumar ambos
        double subtotal = costoDistancia + costoCarga;
        double cargoUrgencia = urgente ? subtotal * 0.15 : 0;
        double total = subtotal + cargoUrgencia;

        // Crear cotización y asociar tarifas
        Cotizacion cotizacion = new Cotizacion();
        tarifaKg.setCotizacion(cotizacion);
        tarifaEspacio.setCotizacion(cotizacion);
        tarifaKm.setCotizacion(cotizacion);
        cotizacion.getTarifas().add(tarifaKg);
        cotizacion.getTarifas().add(tarifaEspacio);
        cotizacion.getTarifas().add(tarifaKm);
        cotizacion.setDistancia(distancia);
        // Persistir solo la cotización (cascade se encarga de las tarifas)
        cotizacionRepository.save(cotizacion);
        return cotizacion;
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