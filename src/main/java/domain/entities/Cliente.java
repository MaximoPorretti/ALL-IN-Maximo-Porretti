package domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Cliente() {
        super();
    }

    public Cliente(String nombre, String apellido, String email, String telefono, Integer dni) {
        super(nombre, apellido, email, telefono, dni);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
