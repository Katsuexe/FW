package rindra.framework.util;

import java.lang.reflect.Method;
import java.util.Map;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import rindra.framework.annotation.Controller;
import rindra.framework.annotation.ResponseBody;
import rindra.framework.annotation.UrlMapping;
import rindra.framework.model.Mapping;
import rindra.framework.model.UrlKey;

/**
 * Outil de découverte des contrôleurs et de leurs routes.
 * Il analyse les classes annotées avec @Controller puis enregistre toutes les
 * méthodes annotées avec @UrlMapping dans une map de routage.
 */
public class Utilitaire {
    public static void scanPaths(String packageToScan, Map<UrlKey, Mapping> map) {
        // Étape 1 : préparation du scan de classes avec ClassGraph.
        ClassGraph cg = new ClassGraph().enableClassInfo().enableAnnotationInfo();
        if (packageToScan != null && !packageToScan.trim().isEmpty()) {
            cg.acceptPackages(packageToScan);
        }

        try (ScanResult scanResult = cg.scan()) {
            // Étape 2 : parcours des classes marquées comme contrôleurs.
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Controller.class.getName())) {
                Class<?> clazz = classInfo.loadClass();

                // Étape 3 : parcours des méthodes du contrôleur pour trouver les routes.
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(UrlMapping.class)) {
                        UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                        String url = urlMapping.value();
                        String[] httpMethods = urlMapping.method();
                        boolean jsonResponse = method.isAnnotationPresent(ResponseBody.class);

                        // Étape 4 : enregistrement de chaque URL + méthode HTTP dans la map.
                        for (String httpMethod : httpMethods) {
                            UrlKey key = new UrlKey(url, httpMethod);
                            if (map.containsKey(key)) {
                                Mapping existing = map.get(key);
                                throw new IllegalStateException("Doublon de route detecte pour l'URL " + url + " avec le verbe " + key.getHttpMethod() + ". Methodes en conflit : " + existing.getClassName() + "." + existing.getMethod() + "() et " + clazz.getName() + "." + method.getName() + "()");
                            }
                            map.put(key, new Mapping(clazz.getName(), method.getName(), jsonResponse));
                        }
                    }
                }
            }
        }
    }
}
