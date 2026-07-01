package rindra.framework.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rindra.framework.model.Mapping;
import rindra.framework.util.Utilitaire;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {

    private Map<String, Mapping> urlList = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String packageToScan = config.getInitParameter("packageToScan");
        urlList = Utilitaire.scanPaths(packageToScan);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getServletPath();
        if (request.getPathInfo() != null) {
            pathInfo += request.getPathInfo();
        }

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>FrontController</title></head>");
            out.println("<body>");
            out.println("<h1>URL demand├⌐e : " + pathInfo + "</h1>");
            
            if (urlList.containsKey(pathInfo)) {
                out.println("<p style='color: green;'>URL Support├⌐e !</p>");
            } else {
                out.println("<p style='color: red;'>URL Non Support├⌐e.</p>");
            }

            out.println("<h2>Liste des routes disponibles :</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>URL</th><th>Classe</th><th>M├⌐thode</th></tr>");
            for (Map.Entry<String, Mapping> entry : urlList.entrySet()) {
                Mapping mapping = entry.getValue();
                out.println("<tr>");
                out.println("<td>" + entry.getKey() + "</td>");
                out.println("<td>" + mapping.getClassName() + "</td>");
                out.println("<td>" + mapping.getMethod() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
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