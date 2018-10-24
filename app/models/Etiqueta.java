package models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String texto;

    public Etiqueta(String texto) {
        this.texto=texto;
    }

    public String getTexto(){
        return texto;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        Etiqueta other = (Etiqueta) obj;
        // Si tenemos los ID, comparamos por ID
        if (id != null && other.id != null)
            return (id.equals(other.id));
        // sino comparamos por campos obligatorios
        return texto.equals(other.texto);
    }

    @Override
    public int hashCode() {
        // Devolvemos el hash de los campos obligatorios
        return Objects.hash(texto);
    }
}