package application.dtos;

public class QuoteRequestDTO {
    private String origin;
    private String destination;
    private double weight;
    private double height;
    private double width;
    private double length;
    private boolean urgency;
    private String scheduledDate;

    public QuoteRequestDTO() {}

    // Getters y setters
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }
    public boolean isUrgency() { return urgency; }
    public void setUrgency(boolean urgency) { this.urgency = urgency; }
    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }
} 