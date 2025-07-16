package application.dtos;

public class QuoteResultDTO {
    private double totalCost;
    private double distanceCost;
    private double weightCost;
    private double volumeCost;
    private double urgencyCost;
    private int estimatedDays;
    private double baseDistance;
    private int availableDrivers;
    private String validUntil;

    public QuoteResultDTO() {}

    // Getters y setters
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public double getDistanceCost() { return distanceCost; }
    public void setDistanceCost(double distanceCost) { this.distanceCost = distanceCost; }
    public double getWeightCost() { return weightCost; }
    public void setWeightCost(double weightCost) { this.weightCost = weightCost; }
    public double getVolumeCost() { return volumeCost; }
    public void setVolumeCost(double volumeCost) { this.volumeCost = volumeCost; }
    public double getUrgencyCost() { return urgencyCost; }
    public void setUrgencyCost(double urgencyCost) { this.urgencyCost = urgencyCost; }
    public int getEstimatedDays() { return estimatedDays; }
    public void setEstimatedDays(int estimatedDays) { this.estimatedDays = estimatedDays; }
    public double getBaseDistance() { return baseDistance; }
    public void setBaseDistance(double baseDistance) { this.baseDistance = baseDistance; }
    public int getAvailableDrivers() { return availableDrivers; }
    public void setAvailableDrivers(int availableDrivers) { this.availableDrivers = availableDrivers; }
    public String getValidUntil() { return validUntil; }
    public void setValidUntil(String validUntil) { this.validUntil = validUntil; }
} 