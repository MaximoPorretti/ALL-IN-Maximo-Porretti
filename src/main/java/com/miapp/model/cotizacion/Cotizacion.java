package com.miapp.model.cotizacion;

import com.miapp.model.tarifas.Tarifa;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarifa> tarifas = new ArrayList<>();

    // Puedes agregar otros campos como fecha, total, etc.

    public Cotizacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Tarifa> getTarifas() { return tarifas; }
    public void setTarifas(List<Tarifa> tarifas) { this.tarifas = tarifas; }
} 