package models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String texto;

    @ManyToMany(mappedBy = "etiquetas", fetch = FetchType.EAGER)
    private Set<Tarea> tareas = new HashSet<>();


    public Etiqueta(){

    }

    public static ArrayList<Etiqueta> separarTextoEnEtiquetas(String cadena){
        ArrayList<Etiqueta> ret = new ArrayList<Etiqueta>();

        String[] trozos = cadena.split(", ");

        for(int i=0;i<trozos.length;i++){
            ret.add(new Etiqueta(trozos[i]));
        }

        return ret;
    }

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

    public Set<Tarea> getTareas(){
        return tareas;
    }

    public void addTarea(Tarea tarea){
        tareas.add(tarea);
        tarea.getEtiquetas().add(this);
    }

    public void deleteTarea(Tarea tarea){
        tareas.remove(tarea);
        tarea.getEtiquetas().remove(this);
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