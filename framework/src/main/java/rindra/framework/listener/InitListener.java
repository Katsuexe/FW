package rindra.framework.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import rindra.framework.model.Mapping;
import rindra.framework.model.UrlKey;
import rindra.framework.util.Utilitaire;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String packageToScan = context.getInitParameter("packageToScan");
        Map<UrlKey, Mapping> urlList = new HashMap<>();
        
        Utilitaire.scanPaths(packageToScan, urlList);
        context.setAttribute("urlList", urlList);
        
        System.out.println("InitListener: Nombre de routes charg├⌐es = " + urlList.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("urlList");
        System.out.println("InitListener: urlList supprim├⌐e du contexte");
    }
}