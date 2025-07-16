package application.controller;

import application.repository.TarifaRepository;
import domain.entities.Tarifa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    @Autowired
    private TarifaRepository tarifaRepository;

    @GetMapping
    public List<Tarifa> getAll() {
        return tarifaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Tarifa> getById(@PathVariable Long id) {
        return tarifaRepository.findById(id);
    }

    @PostMapping
    public Tarifa create(@RequestBody Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    @PutMapping("/{id}")
    public Tarifa update(@PathVariable Long id, @RequestBody Tarifa tarifa) {
        tarifa.setId(id);
        return tarifaRepository.save(tarifa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tarifaRepository.deleteById(id);
    }
} 