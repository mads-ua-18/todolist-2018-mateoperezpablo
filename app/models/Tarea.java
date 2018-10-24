package models;

import javax.persistence.*;
import java.util.Objects;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    // Relación muchos-a-uno entre tareas y usuario
    @ManyToOne
    // Nombre de la columna en la BD que guarda físicamente
    // el ID del usuario con el que está asociado una tarea
    @JoinColumn(name = "usuarioId")
    public Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Etiqueta_Tarea",
        joinColumns = { @JoinColumn(name = "fk_tarea") },
        inverseJoinColumns = {@JoinColumn(name = "fk_etiqueta")})
    private Set<Etiqueta> etiquetas = new HashSet<>();

    public Tarea() {
    }

    // No puede haber tareas sin usuarios, obligamos
    // a que el constructor de una tarea tenga un usuario
    public Tarea(Usuario usuario, String titulo) {
        this.usuario = usuario;
        this.titulo = titulo;
    }

    // Getters y setters necesarios para JPA

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Set<Etiqueta> getEtiquetas(){
        return etiquetas;
    }

    // Intercambia el usuario de una tarea. Actualiza también
    // la lista de tareas en memoria de los usuarios que intercambian
    // las tareas.
    public void setUsuario(Usuario usuario) {
        this.usuario.getTareas().remove(this);
        this.usuario = usuario;
        usuario.getTareas().add(this);
    }

    public String toString() {
        return String.format("Tarea id: %s titulo: %s usuario: %s",
                id, titulo, usuario.toString());
    }

    @Override
    public int hashCode() {
        // Devolvemos el hash de los campos obligatorios
        return Objects.hash(usuario, titulo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Tarea other = (Tarea) obj;
        // Si tenemos los ID, comparamos por ID
        if (id != null && other.id != null)
            return ((long) id == (long) other.id);
            // sino comparamos por campos obligatorios
        else {
            if (titulo == null) {
                if (other.titulo != null) return false;
            } else if (!titulo.equals(other.titulo)) return false;
            if (usuario == null) {
                if (other.usuario != null) return false;
                else if (!usuario.equals(other.usuario)) return false;
            }
        }
        return true;
    }


}
