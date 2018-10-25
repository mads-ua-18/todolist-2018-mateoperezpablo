package services;

import models.Etiqueta;
import models.EtiquetaRepository;
import models.Usuario;
import models.UsuarioRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EtiquetaService {
    UsuarioRepository usuarioRepository;
    EtiquetaRepository etiquetaRepository;

    @Inject
    public EtiquetaService(UsuarioRepository usuarioRepository, EtiquetaRepository etiquetaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.etiquetaRepository = etiquetaRepository;
    }

    public Etiqueta obtenerEtiqueta(Long id){
        return etiquetaRepository.findById(id);
    }
}
