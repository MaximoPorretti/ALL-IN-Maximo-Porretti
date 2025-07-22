package com.miapp.controller;


import com.miapp.model.cotizacion.Cotizacion;
import com.miapp.repository.CotizacionRepository;
import com.miapp.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.miapp.service.DistanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {
    @Autowired
    private CotizacionRepository cotizacionRepository;
    @Autowired
    private CotizacionService cotizacionService;
    @Autowired
    private DistanceService distanceService;

    @GetMapping
    public List<Cotizacion> getAll() {
        return cotizacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cotizacion> getById(@PathVariable Long id) {
        return cotizacionRepository.findById(id);
    }

    @PostMapping
    public Cotizacion create(@RequestBody Cotizacion cotizacion) {
        return cotizacionRepository.save(cotizacion);
    }

    @PutMapping("/{id}")
    public Cotizacion update(@PathVariable Long id, @RequestBody Cotizacion cotizacion) {
        cotizacion.setId(id);
        return cotizacionRepository.save(cotizacion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cotizacionRepository.deleteById(id);
    }

    @PostMapping("/calcular")
    public Cotizacion calcularCotizacion(@RequestBody Map<String, Object> datos) throws Exception {
        String origen = (String) datos.get("origin");
        String destino = (String) datos.get("destination");
        double peso = Double.parseDouble(datos.get("weight").toString());
        double largo = Double.parseDouble(datos.get("length").toString());
        double ancho = Double.parseDouble(datos.get("width").toString());
        double alto = Double.parseDouble(datos.get("height").toString());
        boolean urgente = datos.get("urgency") != null && Boolean.parseBoolean(datos.get("urgency").toString());
        return cotizacionService.calcularYCotizar(origen, destino, peso, largo, ancho, alto, urgente);
    }

    @GetMapping("/api/places/autocomplete")
    public ResponseEntity<?> autocompletePlaces(@RequestParam("input") String input) {
        try {
            JSONArray predictions = distanceService.getPlaceSuggestions(input);
            return ResponseEntity.ok(predictions.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener sugerencias: " + e.getMessage());
        }
    }
} 