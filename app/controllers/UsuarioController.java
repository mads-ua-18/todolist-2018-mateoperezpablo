package controllers;

import models.Usuario;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.ActionAuthenticator;
import services.UsuarioService;
import play.Logger;

// Es necesario importar las vistas que se van a usar
import views.html.detalleUsuario;
import views.html.formLogin;
import views.html.formRegistro;
import views.html.saludo;

import javax.inject.Inject;

public class UsuarioController extends Controller {

    @Inject
    FormFactory formFactory;

    // Play injecta un usuarioService junto con todas las dependencias necesarias:
    // UsuarioRepository y JPAApi
    @Inject
    UsuarioService usuarioService;

    public Result saludo(String mensaje) {
        return ok(saludo.render("El mensaje que he recibido es: " + mensaje));
    }

    public Result formularioRegistro() {
        boolean ap = usuarioService.existeAdministrador();
        return ok(formRegistro.render(formFactory.form(Registro.class), "", ap));
    }

    public Result registroUsuario() {
        Form<Registro> form = formFactory.form(Registro.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(formRegistro.render(form, "Hay errores en el formulario", true));
        }
        Registro datosRegistro = form.get();

        if (usuarioService.findUsuarioPorLogin(datosRegistro.username) != null) {
            return badRequest(formRegistro.render(form, "Login ya existente: escoge otro", true));
        }

        if (!datosRegistro.password.equals(datosRegistro.confirmacion)) {
            return badRequest(formRegistro.render(form, "No coinciden la contraseña y la confirmación", true));
        }

        if(datosRegistro.admin){
            Usuario usuario = usuarioService.creaUsuario(datosRegistro.username, datosRegistro.email, datosRegistro.password, true);
            Logger.debug("Entra en if");
            return redirect(controllers.routes.UsuarioController.formularioLogin());
        }
        else{
            Usuario usuario = usuarioService.creaUsuario(datosRegistro.username, datosRegistro.email, datosRegistro.password);
            Logger.debug("Entra en else");
            return redirect(controllers.routes.UsuarioController.formularioLogin());
        }
    }

    public Result formularioLogin() {
        return ok(formLogin.render(formFactory.form(Login.class), ""));
    }

    public Result loginUsuario() {
        Form<Login> form = formFactory.form(Login.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(formLogin.render(form, "Hay errores en el formulario"));
        }
        Login login = form.get();
        Usuario usuario = usuarioService.login(login.username, login.password);
        if (usuario == null) {
            return notFound(formLogin.render(form, "Login y contraseña no existentes"));
        } else {
            // Añadimos el id del usuario a la clave `connected` de
            // la sesión de Play
            // https://www.playframework.com/documentation/2.5.x/JavaSessionFlash
            // Esa clave es la usada en la autenticación
            session("connected", usuario.getId().toString());
            return redirect(controllers.routes.TareasController.listaTareas(usuario.getId()));
        }
    }

    // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
    // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
    @Security.Authenticated(ActionAuthenticator.class)
    public Result logout() {
        session().remove("connected");
        return redirect(controllers.routes.UsuarioController.loginUsuario());
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result detalleUsuario(Long id) {
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        if (connectedUser != id) {
            return unauthorized("Lo siento, no estás autorizado");
        } else {
            Usuario usuario = usuarioService.findUsuarioPorId(id);
            if (usuario == null) {
                return notFound("Usuario no encontrado");
            } else {
                Logger.debug("Encontrado usuario " + usuario.getId() + ": " + usuario.getLogin());
                return ok(detalleUsuario.render(usuario));
            }
        }
    }
}
