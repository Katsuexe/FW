package rindra.framework;

import javax.servlet.http.HttpServletRequest;

import rindra.framework.annotation.Controller;
import rindra.framework.annotation.ResponseBody;
import rindra.framework.annotation.UrlMapping;
import rindra.framework.model.ModelAndView;
import rindra.framework.util.FormBinder;
import rindra.framework.util.FormBinder.BindingResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    @UrlMapping(value = "/hello", method = {"GET"})
    public String hello() {
        return "Hello World GET!";
    }

    @UrlMapping(value = "/hello", method = {"POST"})
    public String helloPost() {
        return "Hello World POST!";
    }

    @UrlMapping(value = "/fling", method = {"POST"})
    public String fling() {
        return "Fling balls!";
    }

    @UrlMapping(value = "/error", method = {"GET"})
    public String throwError() {
        throw new RuntimeException("This is a deliberate error for testing!");
    }

    @UrlMapping(value = "/api/eleve", method = {"GET"})
    @ResponseBody
    public Map<String, Object> eleve() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", "Hello");
        data.put("nom", "Rakoto");
        data.put("notes", List.of(12, 15, 18));
        return data;
    }

    @UrlMapping(value = "/api/message", method = {"GET"})
    @ResponseBody
    public String apiMessage() {
        return "Secretaire/profil_eleve";
    }

    // -------------------------------------------------------------------------
    // Sprint 7 : démo du binding de formulaire via FormBinder
    // -------------------------------------------------------------------------

    /**
     * GET /contact : affiche le formulaire vierge.
     * Aucune donnée à binder, on renvoie simplement la vue.
     */
    @UrlMapping(value = "/contact", method = {"GET"})
    public ModelAndView contactForm() {
        // Affichage initial : formulaire vide, pas d'erreur, pas de succès.
        return new ModelAndView("contact.jsp");
    }

    /**
     * POST /contact : reçoit les données du formulaire et les valide via FormBinder.
     *
     * Flux :
     *   1. FormBinder.bind() lit les champs "nom" et "prenom" depuis la requête.
     *   2. Si le binding est valide, on affiche la vue avec un message de succès.
     *   3. Si des erreurs existent, on réaffiche le formulaire avec les erreurs
     *      et les valeurs déjà saisies (pour ne pas perdre la saisie de l'utilisateur).
     */
    @UrlMapping(value = "/contact", method = {"POST"})
    public ModelAndView contactSubmit(HttpServletRequest request) {
        // Étape 1 : délégation du binding à FormBinder.
        // On déclare ici les champs attendus ; FormBinder se charge du reste.
        BindingResult binding = FormBinder.bind(request, "nom", "prenom");

        ModelAndView mv = new ModelAndView("contact.jsp");

        if (binding.isValid()) {
            // Étape 2 : données valides → on passe les valeurs et un flag de succès à la vue.
            mv.setAttribute("success", true);
            mv.setAttribute("nom", binding.getValue("nom"));
            mv.setAttribute("prenom", binding.getValue("prenom"));
        } else {
            // Étape 3 : erreurs → on réaffiche les valeurs saisies et les messages d'erreur.
            mv.setAttribute("nom", binding.getValue("nom"));
            mv.setAttribute("prenom", binding.getValue("prenom"));
            mv.setAttribute("erreur_nom", binding.getError("nom"));
            mv.setAttribute("erreur_prenom", binding.getError("prenom"));
        }

        return mv;
    }
}
