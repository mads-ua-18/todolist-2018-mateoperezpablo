package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import javax.persistence.EntityManager;

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

    public Etiqueta findById(Long id){
        return jpaApi.withTransaction(entityManager -> {
            return entityManager.find(Etiqueta.class, id);
        });
    }

    public Etiqueta findEtiquetaPorTextoUsuario(String texto, Usuario usuario){

        

        return jpaApi.withTransaction(entityManager -> {
            TypedQuery<Etiqueta> query = entityManager.createQuery(
                    "select e from Etiqueta e join e.tareas t where e.texto = :texto and t.usuario.id = :usuarioId", Etiqueta.class);
            try {
                Etiqueta etiqueta = query.setParameter("texto", texto).setParameter("usuarioId", usuario.getId()).getSingleResult();
                return etiqueta;
            } catch (NoResultException ex) {
                return null;
            }
        });
    }

    public void addEtiquetaTarea(Etiqueta etiqueta, Tarea tarea){
        jpaApi.withTransaction( () -> {
            EntityManager entityManager = jpaApi.em();
            Etiqueta etiquetaBD = entityManager.find(Etiqueta.class, etiqueta.getId());
            Tarea tareaBD = entityManager.find(Tarea.class, tarea.getId());
            // El método addTarea de Etiqueta actualiza los campos y el
            // cambio se actualiza automáticamente a la base de datos
            etiquetaBD.addTarea(tareaBD);
        });
    }

    public void deleteEtiquetaTarea(Etiqueta etiqueta, Tarea tarea){
        jpaApi.withTransaction( () -> {
            EntityManager entityManager = jpaApi.em();
            Etiqueta etiquetaBD = entityManager.find(Etiqueta.class, etiqueta.getId());
            Tarea tareaBD = entityManager.find(Tarea.class, tarea.getId());
            etiquetaBD.deleteTarea(tareaBD);
        });
    }

    public void delete(Etiqueta etiqueta){
        jpaApi.withTransaction( () -> {
            EntityManager entityManager = jpaApi.em();
            Etiqueta etiquetaBD = entityManager.getReference(Etiqueta.class, etiqueta.getId());
            entityManager.remove(etiquetaBD);
        });
    }
}
