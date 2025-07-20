package com.miapp.model.envio;

import com.miapp.model.Usuarios.Chofer;
import com.miapp.model.Usuarios.Cliente;
import com.miapp.model.cargas.Carga;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envio")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Chofer chofer;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Carga carga;

    @Column(unique = true, nullable = false)
    private String codigoSeguimiento;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private Double costoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    private LocalDateTime actualizadoEn;
    private String fechaProgramada;
    private Boolean urgencia;
    private String metodoPago;

    public Envio() {
        this.creadoEn = LocalDateTime.now();
        this.actualizadoEn = LocalDateTime.now();
        this.estado = EstadoEnvio.ACEPTADO;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Chofer getChofer() { return chofer; }
    public void setChofer(Chofer chofer) { this.chofer = chofer; }

    public Carga getCarga() { return carga; }
    public void setCarga(Carga carga) { this.carga = carga; }

    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public void setCodigoSeguimiento(String codigoSeguimiento) { this.codigoSeguimiento = codigoSeguimiento; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Double costoTotal) { this.costoTotal = costoTotal; }

    public EstadoEnvio getEstado() { return estado; }
    public void setEstado(EstadoEnvio estado) { this.estado = estado; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public String getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(String fechaProgramada) { this.fechaProgramada = fechaProgramada; }

    public Boolean getUrgencia() { return urgencia; }
    public void setUrgencia(Boolean urgencia) { this.urgencia = urgencia; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    @PreUpdate
    public void preActualizar() {
        this.actualizadoEn = LocalDateTime.now();
    }
} 