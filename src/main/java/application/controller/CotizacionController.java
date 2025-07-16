package application.controller;

import application.repository.CotizacionRepository;
import domain.entities.Cotizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {
    @Autowired
    private CotizacionRepository cotizacionRepository;

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
} 