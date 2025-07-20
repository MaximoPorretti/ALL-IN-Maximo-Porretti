package com.miapp.model.envio;

public enum EstadoEnvio {
    ACEPTADO("Aceptado - El transportista ah aceptado el viaje"),
    EN_CAMINO_A("En camino - El Transportista "),
    ENTREGADO("Entregado - Viaje completado");

    private final String descripcion;

    EstadoEnvio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
} 