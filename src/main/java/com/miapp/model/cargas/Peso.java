package com.miapp.model.cargas;

public class Peso {
    private double valor;
    private double tarifa;

    public Peso(double valor, double tarifa) {
        this.valor = valor;
        this.tarifa = tarifa;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}
