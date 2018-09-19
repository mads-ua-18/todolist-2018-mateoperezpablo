package controllers;

import models.Usuario;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http;
import security.ActionAuthenticator;
import services.UsuarioService;

import views.html.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    @Inject
    UsuarioService usuarioService;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result about() {
        Http.Session op = session();
        if(op.size()<=0){
            Usuario usuario = new Usuario();
            return ok(about.render(null));
        }
        String connectedUserStr = session("connected");
        Long connectedUser = Long.valueOf(connectedUserStr);
        Usuario usuario = usuarioService.findUsuarioPorId(connectedUser);
        if (usuario == null) {
            return ok(about.render(null));
        } else {
            Logger.debug("Encontrado usuario para about " + usuario.getId() + ": " + usuario.getLogin());
            return ok(about.render(usuario));
        }
    }

}
