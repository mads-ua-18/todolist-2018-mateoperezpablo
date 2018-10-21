package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class JPAEtiquetaRepository implements EtiquetaRepository {
    // Objeto definido por Play para acceder al API de JPA
    // https://www.playframework.com/documentation/2.5.x/JavaJPA#Using-play.db.jpa.JPAApi
    JPAApi jpaApi;

    // Para usar el JPAEtiquetaRepository hay que proporcionar una JPAApi.
    // La anotación Inject hace que Play proporcione el JPAApi cuando se lance
    // la aplicación.
    @Inject
    public JPAEtiquetaRepository(JPAApi api) {
        this.jpaApi = api;
    }

    public Etiqueta add(Etiqueta etiqueta) {
        return jpaApi.withTransaction(entityManager -> {
            entityManager.persist(etiqueta);
            // Hacemos un flush y un refresh para asegurarnos de que se realiza
            // la creación en la BD y se devuelve el id inicializado
            entityManager.flush();
            entityManager.refresh(etiqueta);
            return etiqueta;
        });
    }
}
