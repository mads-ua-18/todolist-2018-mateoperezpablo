package controllers;

import models.Tarea;
import models.Usuario;
import models.Etiqueta;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.ActionAuthenticator;
import services.TareaService;
import services.EtiquetaService;
import services.UsuarioService;
import play.Logger;
import java.util.ArrayList;
import java.util.List;


// Es necesario importar las vistas que se van a usar
import views.html.formModificacionTarea;
import views.html.formNuevaTarea;
import views.html.listaTareas;

import javax.inject.Inject;
import java.util.List;

public class TareasController extends Controller {

   @Inject FormFactory formFactory;
   @Inject UsuarioService usuarioService;
   @Inject TareaService tareaService;
   @Inject EtiquetaService etiquetaService;

   // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
   // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (!connectedUser.equals(idUsuario)) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         return ok(formNuevaTarea.render(usuario, formFactory.form(Tarea.class),""));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result creaNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (!connectedUser.equals(idUsuario)) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
         if (tareaForm.hasErrors()) {
            Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
            return badRequest(formNuevaTarea.render(usuario, formFactory.form(Tarea.class), "Hay errores en el formulario"));
         }
         Tarea tarea = tareaForm.get();
         Tarea t = tareaService.nuevaTarea(idUsuario, tarea.getTitulo());
         //Etiquetas
         DynamicForm requestData = formFactory.form().bindFromRequest();
         String setiquetas = requestData.get("aux");

         Logger.debug("ID tarea " + t.getId());

         ArrayList<Etiqueta> etiquetas = Etiqueta.separarTextoEnEtiquetas(setiquetas);
         for(int i=0;i<etiquetas.size();i++){
             etiquetaService.addEtiqueta(etiquetas.get(i).getTexto(), connectedUser, t.getId());
         }

         Logger.debug(setiquetas);
         flash("aviso", "La tarea se ha grabado correctamente");
         return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result listaTareas(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (!connectedUser.equals(idUsuario)) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         String aviso = flash("aviso");
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);
         return ok(listaTareas.render(tareas, usuario, aviso));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioEditaTarea(Long idTarea) {
      Tarea tarea = tareaService.obtenerTarea(idTarea);
      if (tarea == null) {
         return notFound("Tarea no encontrada");
      } else {
         String connectedUserStr = session("connected");
         Long connectedUser =  Long.valueOf(connectedUserStr);
         if (!connectedUser.equals(tarea.getUsuario().getId())) {
            return unauthorized("Lo siento, no estás autorizado");
         } else {
            List<Etiqueta> etiquetas = new ArrayList<>(tarea.getEtiquetas());
            String aux = "";
            for(int i=0;i<etiquetas.size();i++){
                aux = aux + etiquetas.get(i).getTexto() + ", ";
            }
            if(aux.length()>=2)aux = aux.substring(0, aux.length()-2);
            return ok(formModificacionTarea.render(
            tarea.getId(),
            tarea.getTitulo(),
            "",tarea.getUsuario()
            , aux));
         }
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result grabaTareaModificada(Long idTarea) {
      DynamicForm requestData = formFactory.form().bindFromRequest();
      String nuevoTitulo = requestData.get("titulo");
      String aux = requestData.get("aux");
      //quitar tareas
      Tarea old = tareaService.obtenerTarea(idTarea);
      List<Etiqueta> etiquetas = new ArrayList<>(old.getEtiquetas());
      for(int i = 0;i<etiquetas.size();i++){
          etiquetaService.deleteEtiqueta(etiquetas.get(i).getId(), idTarea);
      }
      //añadir taraes
      ArrayList<Etiqueta> et = Etiqueta.separarTextoEnEtiquetas(aux);
      Long idUsuario = old.getUsuario().getId();
      for(int i=0;i<et.size();i++){
        etiquetaService.addEtiqueta(et.get(i).getTexto(), idUsuario, old.getId());
      }

      Tarea tarea = tareaService.modificaTarea(idTarea, nuevoTitulo);
      return redirect(controllers.routes.TareasController.listaTareas(tarea.getUsuario().getId()));
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result borraTarea(Long idTarea) {
      tareaService.borraTarea(idTarea);
      flash("aviso", "Tarea borrada correctamente");
      return ok();
   }
}
