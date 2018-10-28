package services;

import models.Etiqueta;
import models.EtiquetaRepository;
import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EtiquetaService {
    UsuarioRepository usuarioRepository;
    EtiquetaRepository etiquetaRepository;
    TareaRepository tareaRepository;

    @Inject
    public EtiquetaService(UsuarioRepository usuarioRepository, EtiquetaRepository etiquetaRepository, TareaRepository tareaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.etiquetaRepository = etiquetaRepository;
        this.tareaRepository = tareaRepository;
    }

    public Etiqueta obtenerEtiqueta(Long id){
        return etiquetaRepository.findById(id);
    }

    public Etiqueta addEtiqueta(String texto, Long idUsuario, Long idTarea){
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Tarea tarea = tareaRepository.findById(idTarea);
        Etiqueta etiqueta = etiquetaRepository.findEtiquetaPorTextoUsuario(texto, usuario);
        if(etiqueta==null){
            etiqueta = new Etiqueta(texto);
            etiqueta = etiquetaRepository.add(etiqueta);
        }
        etiquetaRepository.addEtiquetaTarea(etiqueta, tarea);
        return etiqueta;
    }

    public void deleteEtiqueta(Long idEtiqueta, Long idTarea){
        Tarea tarea = tareaRepository.findById(idTarea);
        Etiqueta etiqueta = etiquetaRepository.findById(idEtiqueta);

        etiquetaRepository.deleteEtiquetaTarea(etiqueta, tarea);

        etiqueta = etiquetaRepository.findById(idEtiqueta);

        if(etiqueta.getTareas().size()<=0) etiquetaRepository.delete(etiqueta);
    }
}
