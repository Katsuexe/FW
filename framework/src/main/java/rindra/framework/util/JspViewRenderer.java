package rindra.framework.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rindra.framework.helper.ViewRenderer;

/**
 * Rend une vue JSP en transférant la requête vers le dispatcher du conteneur.
 * Cela permet de laisser le moteur JSP générer la page finale.
 */
public class JspViewRenderer implements ViewRenderer {
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }
}
