package com.example.servlet;

import com.example.util.Utilitaire;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listControllers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String packageToScan = config.getInitParameter("packageToScan");
        listControllers = Utilitaire.scanControllers(packageToScan);
        System.out.println("Nombre de contr├┤leurs trouv├⌐s : " + listControllers.size());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        System.out.println("URL demand├⌐e : " + requestURL);

        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("URL: " + requestURL);
            out.println("Liste des contr├┤leurs :");
            for (String controller : listControllers) {
                out.println("- " + controller);
            }
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