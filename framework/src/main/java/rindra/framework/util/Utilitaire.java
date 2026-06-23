package rindra.framework.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import rindra.framework.annotation.Controller;
import rindra.framework.annotation.UrlMapping;
import rindra.framework.model.Mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Utilitaire {
    public static Map<String, Mapping> scanPaths(String packageToScan) {
        Map<String, Mapping> urlList = new HashMap<>();
        ClassGraph cg = new ClassGraph().enableClassInfo().enableAnnotationInfo();
        if (packageToScan != null && !packageToScan.trim().isEmpty()) {
            cg.acceptPackages(packageToScan);
        }
        try (ScanResult scanResult = cg.scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Controller.class.getName())) {
                Class<?> clazz = classInfo.loadClass();
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(UrlMapping.class)) {
                        UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);
                        String url = urlMapping.value();
                        urlList.put(url, new Mapping(clazz.getName(), method.getName()));
                    }
                }
            }
        }
        return urlList;
    }
}