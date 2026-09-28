package rindra.framework.listener;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import rindra.framework.model.Mapping;
import rindra.framework.model.UrlKey;
import rindra.framework.util.Utilitaire;

/**
 * Écouteur du démarrage du contexte servlet.
 * À l'initialisation, il scanne les contrôleurs du package configuré et
 * place les routes dans le contexte pour que le FrontController puisse les utiliser.
 */
@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String packageToScan = context.getInitParameter("packageToScan");
        Map<UrlKey, Mapping> urlList = new HashMap<>();

        // Étape 1 : découvrir toutes les routes déclarées dans les contrôleurs.
        Utilitaire.scanPaths(packageToScan, urlList);

        // Étape 2 : enregistrer la map des routes dans le contexte Servlet.
        context.setAttribute("urlList", urlList);

        System.out.println("InitListener: Nombre de routes chargées = " + urlList.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nettoyage du contexte au moment de l'arrêt du serveur.
        sce.getServletContext().removeAttribute("urlList");
        System.out.println("InitListener: urlList supprimée du contexte");
    }
}