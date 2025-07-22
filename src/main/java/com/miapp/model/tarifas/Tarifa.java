package com.miapp.model.tarifas;

import com.miapp.model.cotizacion.Cotizacion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_tarifa")
public abstract class  Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    @JsonBackReference
    private Cotizacion cotizacion;

    @Column(nullable = false)
    private Double valor;

    public Tarifa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cotizacion getCotizacion() { return cotizacion; }
    public void setCotizacion(Cotizacion cotizacion) { this.cotizacion = cotizacion; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
} 