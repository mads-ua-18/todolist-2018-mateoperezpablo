package controllers;

import java.util.ArrayList;

import models.Equipo;
import models.Usuario;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.ActionAuthenticator;
import services.EquipoService;
import services.UsuarioService;

import play.Logger;

// Es necesario importar las vistas que se van a usar
import services.EquipoServiceException;
import views.html.formNuevoEquipo;
import views.html.listaEquipos;
import views.html.formEquipoUsuario;
import views.html.listaEquiposUsuario;

import javax.inject.Inject;
import java.util.List;

public class EquipoController extends Controller {
    @Inject
    FormFactory formFactory;
    @Inject
    EquipoService equipoService;
    @Inject UsuarioService usuarioService;

    @Security.Authenticated(ActionAuthenticator.class)
    public Result formularioNuevoEquipo() {
        return ok(formNuevoEquipo.render(""));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result creaNuevoEquipo() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String nombre = requestData.get("nombre");
        if (nombre == null || nombre.equals("")) {
            return badRequest(formNuevoEquipo.render("Debes rellenar el nombre"));
        }
        equipoService.addEquipo(nombre);
        return ok("Equipo " + nombre + " añadido correctamente");
    }

    public Result listaEquipos() {
        List<Equipo> equipos = equipoService.allEquipos();
        return ok(listaEquipos.render(equipos));
    }

    public Result listaEquiposUsuario(Long id) {
        String connectedUserStr = session("connected");
        Long connectedUser =  Long.valueOf(connectedUserStr);
        if (!connectedUser.equals(id)) {
            return unauthorized("Lo siento, no estás autorizado");
        } else {
            Usuario usuario = usuarioService.findUsuarioPorId(id);
            List<Equipo> equipos = new ArrayList<Equipo>(usuario.getEquipos());
            return ok(listaEquiposUsuario.render(equipos, usuario));
        }
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result detallesEquipo(Long id){
        String connectedUserStr = session("connected");
        if(connectedUserStr==null) return unauthorized("Lo siento, no estás autorizado");
        Long connectedUser =  Long.valueOf(connectedUserStr);
        Usuario usuario = usuarioService.findUsuarioPorId(connectedUser);
        List<Equipo> equipos = new ArrayList<Equipo>(usuario.getEquipos());
        for(int i=0;i<equipos.size();i++){
            Equipo p = equipos.get(i);
            Logger.debug("Iterando" + p.getId() + " " + id);
            if(p.getId().equals(id)){
                Logger.debug("FOUND" + p.getId() + " " + id);
                return unauthorized(p.getId() + " " + p.getNombre());
            }
        }
        return unauthorized(""+id);
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result formularioAddUsuarioEquipo() {
        return ok(formEquipoUsuario.render());
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addUsuarioEquipo() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String equipo = requestData.get("equipo");
        String usuario = requestData.get("usuario");
        try {
            equipoService.addUsuarioEquipo(usuario, equipo);
            return ok("Usuario " + usuario + " añadido al equipo " + equipo);
        } catch (EquipoServiceException exception) {
            return notFound("No existe usuario / equipo");
        }
    }
}
