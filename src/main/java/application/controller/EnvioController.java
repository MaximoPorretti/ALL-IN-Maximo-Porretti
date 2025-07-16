package application.controller;

import application.repository.EnvioRepository;
import domain.entities.Envio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/envios")
public class EnvioController {
    @Autowired
    private EnvioRepository envioRepository;

    @GetMapping
    public List<Envio> getAll() {
        return envioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Envio> getById(@PathVariable Long id) {
        return envioRepository.findById(id);
    }

    @PostMapping
    public Envio create(@RequestBody Envio envio) {
        return envioRepository.save(envio);
    }

    @PutMapping("/{id}")
    public Envio update(@PathVariable Long id, @RequestBody Envio envio) {
        envio.setId(id);
        return envioRepository.save(envio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        envioRepository.deleteById(id);
    }
} 