package domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
@DiscriminatorValue("KM")
public class TarifaPorKm extends Tarifa {
    @Column
    private double tarifa;
    @Column
    private double distancia;

    public TarifaPorKm() {}

    public TarifaPorKm(double tarifa, double distancia) {
        this.tarifa = tarifa;
        this.distancia = distancia;
    }

    public double calcularTarifaKm() {
        return this.tarifa * this.distancia;
    }

    public double getTarifa() { return tarifa; }
    public void setTarifa(double tarifa) { this.tarifa = tarifa; }
    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }
} 