package domain.entities;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Usuario {
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String telefono;
    protected Integer dni;

    public Usuario() {}

    public Usuario(String nombre, String apellido, String email, String telefono, Integer dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Integer getDni() { return dni; }
    public void setDni(Integer dni) { this.dni = dni; }
}
