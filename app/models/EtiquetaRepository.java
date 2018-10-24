package models;

public interface EtiquetaRepository {
    Etiqueta add(Etiqueta etiqueta);
    Etiqueta findById(Long id);
}

