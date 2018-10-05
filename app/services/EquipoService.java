package services;

import models.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquipoService {
    EquipoRepository equipoRepository;
    UsuarioRepository usuarioRepository;

    @Inject
    public EquipoService(EquipoRepository equipoRepository, UsuarioRepository usuarioRepository) {
        this.equipoRepository = equipoRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public Equipo addEquipo(String nombre) {
        if (equipoRepository.findByNombre(nombre) != null)
            throw new EquipoServiceException("Nombre de equipo ya existe: " + nombre);
        Equipo equipo = new Equipo(nombre);
        return equipoRepository.add(equipo);
    }

    public Equipo findById(Long id){
        return equipoRepository.findById(id);
    }

    public void delete(Equipo equipo){
        equipoRepository.delete(equipo);
    }

    // Devuelve la lista de equipos ordenadas por su id
    public List<Equipo> allEquipos() {
        List<Equipo> equipos = equipoRepository.findAll();
        Collections.sort(equipos, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return equipos;
    }

    public void addUsuarioEquipo(String login, String nombreEquipo) {
        Equipo equipo = equipoRepository.findByNombre(nombreEquipo);
        if (equipo == null) {
            throw new EquipoServiceException("No existe el equipo: " + nombreEquipo);
        }
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) {
            throw new EquipoServiceException("No existe el usuario con username: " + login);
        }
        equipoRepository.addUsuarioEquipo(usuario, equipo);
    }

    public void deleteUsuarioEquipo(String login, String nombreEquipo) {
        Equipo equipo = equipoRepository.findByNombre(nombreEquipo);
        if (equipo == null) {
            throw new EquipoServiceException("No existe el equipo: " + nombreEquipo);
        }
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) {
            throw new EquipoServiceException("No existe el usuario con username: " + login);
        }
        equipoRepository.deleteUsuarioEquipo(usuario, equipo);
    }

    public List<Usuario> findUsuariosEquipo(String nombreEquipo) {
        List<Usuario> usuarios = new ArrayList<>();
        Equipo equipo = equipoRepository.findByNombre(nombreEquipo);
        if (equipo != null) {
            usuarios = equipoRepository.findUsuariosEquipo(nombreEquipo);
        }
        return usuarios;
    }

    public List<Usuario> findUsuariosNoEquipo(String nombreEquipo) {
        List<Usuario> usuarios = new ArrayList<>();
        Equipo equipo = equipoRepository.findByNombre(nombreEquipo);
        if (equipo != null) {
            usuarios = equipoRepository.findUsuariosNoEquipo(equipo.getId());
        }
        return usuarios;
    }
}
