package models;

public interface EtiquetaRepository {
    Etiqueta add(Etiqueta etiqueta);
    void delete(Etiqueta etiqueta);
    Etiqueta findById(Long id);
    Etiqueta findEtiquetaPorTextoUsuario(String texto, Usuario usuario);
    void addEtiquetaTarea(Etiqueta etiqueta, Tarea tarea);
    void deleteEtiquetaTarea(Etiqueta etiqueta, Tarea tarea);
}

