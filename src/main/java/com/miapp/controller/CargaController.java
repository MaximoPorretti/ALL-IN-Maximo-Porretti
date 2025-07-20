package com.miapp.controller;

import com.miapp.model.cargas.Carga;
import com.miapp.repository.CargaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cargas")
public class CargaController {
    @Autowired
    private CargaRepository cargaRepository;

    @GetMapping
    public List<Carga> getAll() {
        return cargaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Carga> getById(@PathVariable Long id) {
        return cargaRepository.findById(id);
    }

    @PostMapping
    public Carga create(@RequestBody Carga carga) {
        return cargaRepository.save(carga);
    }

    @PutMapping("/{id}")
    public Carga update(@PathVariable Long id, @RequestBody Carga carga) {
        carga.setId(id);
        return cargaRepository.save(carga);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cargaRepository.deleteById(id);
    }
} 