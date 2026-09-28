package rindra.framework.helper;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface de stratégie pour le rendu d'une vue.
 * Selon l'extension de la vue (.jsp, .html, etc.), le framework choisit
 * un renderer spécifique pour produire la réponse HTTP.
 */
public interface ViewRenderer {
    void render(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException;
}
