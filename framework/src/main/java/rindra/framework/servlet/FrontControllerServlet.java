package rindra.framework.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rindra.framework.model.Mapping;
import rindra.framework.model.UrlKey;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {

    private Map<UrlKey, Mapping> urlList = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        urlList = (Map<UrlKey, Mapping>) config.getServletContext().getAttribute("urlList");
        if (urlList == null) {
            System.err.println("Erreur: urlList est null dans le FrontControllerServlet. Le InitListener a-t-il bien ├⌐t├⌐ ex├⌐cut├⌐ ?");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        if (request.getPathInfo() != null) {
            pathInfo += request.getPathInfo();
        }

        String httpMethod = request.getMethod();
        UrlKey requestKey = new UrlKey(pathInfo, httpMethod);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>FrontController</title></head>");
            out.println("<body>");

            if (urlList.containsKey(requestKey)) {
                Mapping mapping = urlList.get(requestKey);
                try {
                    Class<?> clazz = Class.forName(mapping.getClassName());
                    Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                    Method targetMethod = clazz.getDeclaredMethod(mapping.getMethod());
                    Object result = targetMethod.invoke(controllerInstance);

                    out.println("<h1>Ex├⌐cution r├⌐ussie : " + pathInfo + " (" + httpMethod + ")</h1>");
                    if (result != null) {
                        out.println("<p>R├⌐sultat : " + result.toString() + "</p>");
                    }
                } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    out.println("<h1 style='color:red;'>Erreur serveur (500)</h1>");
                    out.println("<pre>");
                    e.printStackTrace(out);
                    out.println("</pre>");
                }
            } else {
                out.println("<h1 style='color:red;'>Erreur (404) : URL ou Verbe non support├⌐</h1>");
                out.println("<h2>Suggestions :</h2>");
                boolean foundSuggestion = false;
                out.println("<ul>");
                for (UrlKey key : urlList.keySet()) {
                    if (key.getUrl().startsWith(pathInfo) || pathInfo.startsWith(key.getUrl())) {
                        out.println("<li>" + key.getUrl() + " (" + key.getHttpMethod() + ")</li>");
                        foundSuggestion = true;
                    }
                }
                out.println("</ul>");
                if (!foundSuggestion) {
                    out.println("<p>Aucune suggestion. Voici la liste compl├¿te des routes :</p>");
                    out.println("<ul>");
                    for (UrlKey key : urlList.keySet()) {
                        out.println("<li>" + key.getUrl() + " (" + key.getHttpMethod() + ")</li>");
                    }
                    out.println("</ul>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        }
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
}