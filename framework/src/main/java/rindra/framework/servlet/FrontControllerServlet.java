package rindra.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import rindra.framework.annotation.ResponseBody;
import rindra.framework.helper.ViewRenderer;
import rindra.framework.model.Mapping;
import rindra.framework.model.ModelAndView;
import rindra.framework.model.UrlKey;
import rindra.framework.util.HtmlViewRenderer;
import rindra.framework.util.JspViewRenderer;

/**
 * Contrôleur central qui reçoit toutes les requêtes HTTP.
 * Il se charge de :
 * 1. récupérer la route demandée,
 * 2. trouver le contrôleur associé,
 * 3. invoquer la méthode correspondante,
 * 4. renvoyer une réponse HTML, JSON ou une vue JSP/HTML.
 */
public class FrontControllerServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<UrlKey, Mapping> urlList = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Le listener a préalablement chargé la map des routes dans le contexte.
        urlList = (Map<UrlKey, Mapping>) config.getServletContext().getAttribute("urlList");
        if (urlList == null) {
            System.err.println("Erreur: urlList est null dans le FrontControllerServlet.");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Étape 1 : reconstruire le chemin complet de la requête.
        String pathInfo = request.getServletPath();
        if (request.getPathInfo() != null) {
            pathInfo += request.getPathInfo();
        }

        String httpMethod = request.getMethod();
        UrlKey requestKey = new UrlKey(pathInfo, httpMethod);

        StringBuilder buf = new StringBuilder();
        buf.append("<html>");
        buf.append("<head><title>FrontController</title></head>");
        buf.append("<body>");

        // Étape 2 : vérifier si la route demandée existe dans la map.
        if (urlList.containsKey(requestKey)) {
            Mapping mapping = urlList.get(requestKey);
            try {
                // Étape 3 : chargement dynamique du contrôleur et de sa méthode.
                Class<?> clazz = Class.forName(mapping.getClassName());
                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                Method targetMethod = clazz.getDeclaredMethod(mapping.getMethod());

                // Une méthode est traitée comme API JSON si elle est annotée @ResponseBody
                // ou si la route a été enregistrée comme réponse JSON.
                boolean apiMethod = targetMethod.isAnnotationPresent(ResponseBody.class)
                        || mapping.isJsonResponse();
                Object result = targetMethod.invoke(controllerInstance);

                // Étape 4 : si la méthode retourne un objet métier et doit être sérialisé en JSON.
                if (apiMethod && !(result instanceof String)) {
                    response.setContentType("application/json;charset=UTF-8");
                    objectMapper.writeValue(response.getWriter(), result);
                    return;
                }

                // Étape 5 : si la méthode retourne une chaîne, on affiche un message de succès.
                if (result instanceof String textResult) {
                    buf.append("<h1>Exécution réussie : ").append(pathInfo).append(" (").append(httpMethod).append(")</h1>");
                    buf.append("<p>Résultat : ").append(textResult).append("</p>");
                } else if (result instanceof ModelAndView modelAndView) {
                    // On injecte les attributs du modèle dans la requête HttpServletRequest.
                    for (Map.Entry<String, Object> entry : modelAndView.getModel().entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }

                    String viewName = modelAndView.getView();
                    ViewRenderer renderer = null;

                    // Étape 6 : choisir le bon renderer selon l'extension du fichier de vue.
                    if (viewName.endsWith(".jsp")) {
                        renderer = new JspViewRenderer();
                    } else if (viewName.endsWith(".html")) {
                        renderer = new HtmlViewRenderer();
                    }

                    if (renderer != null) {
                        renderer.render(request, response, viewName);
                        return;
                    } else {
                        buf.append("<p style='color:red;'>Aucun ViewRenderer trouvé pour la vue : ").append(viewName).append("</p>");
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                buf.append("<h1 style='color:red;'>Erreur serveur (500)</h1>");
                buf.append("<pre>");
                e.printStackTrace(new PrintWriter(new StringBuilderWriter(buf)));
                buf.append("</pre>");
            }
        } else {
            // Étape 7 : gérer les erreurs 404 avec une liste de routes possibles.
            buf.append("<h1 style='color:red;'>Erreur (404) : URL ou Verbe non supporté</h1>");
            buf.append("<table border='1' style='border-collapse: collapse; width: 100%; text-align: left;'>");
            buf.append("<tr style='background-color: #f3f4f6;'>");
            buf.append("<th>Méthode HTTP</th><th>URL</th><th>Controller</th><th>Méthode Class</th>");
            buf.append("</tr>");

            boolean foundSuggestion = false;
            for (Map.Entry<UrlKey, Mapping> entry : urlList.entrySet()) {
                String routeDisponible = entry.getKey().getUrl();
                if (routeDisponible.startsWith(pathInfo)) {
                    foundSuggestion = true;
                    buf.append("<tr>");
                    buf.append("<td><span style='background: #e5e7eb; padding: 3px 8px; border-radius: 4px; font-weight: bold;'>").append(entry.getKey().getHttpMethod()).append("</span></td>");
                    buf.append("<td><code>").append(routeDisponible).append("</code></td>");
                    buf.append("<td>").append(entry.getValue().getClassName()).append("</td>");
                    buf.append("<td>").append(entry.getValue().getMethod()).append("()</td>");
                    buf.append("</tr>");
                }
            }

            if (!foundSuggestion) {
                buf.append("<tr><td colspan='4' style='text-align:center; color: gray;'>");
                buf.append("Aucune sous-route trouvée. Voici toutes les configurations de l'application :");
                buf.append("</td></tr>");

                for (Map.Entry<UrlKey, Mapping> entry : urlList.entrySet()) {
                    buf.append("<tr>");
                    buf.append("<td><span style='background: #e5e7eb; padding: 3px 8px; border-radius: 4px; font-weight: bold;'>").append(entry.getKey().getHttpMethod()).append("</span></td>");
                    buf.append("<td><code>").append(entry.getKey().getUrl()).append("</code></td>");
                    buf.append("<td>").append(entry.getValue().getClassName()).append("</td>");
                    buf.append("<td>").append(entry.getValue().getMethod()).append("()</td>");
                    buf.append("</tr>");
                }
            }
            buf.append("</table>");
        }

        buf.append("</body>");
        buf.append("</html>");

        // Réponse HTML finale pour les cas de rendu standard.
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(buf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private static class StringBuilderWriter extends java.io.Writer {
        private final StringBuilder sb;
        public StringBuilderWriter(StringBuilder sb) { this.sb = sb; }
        @Override public void write(char[] cbuf, int off, int len) { sb.append(cbuf, off, len); }
        @Override public void flush() {}
        @Override public void close() {}
    }
}
