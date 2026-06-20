package com.example.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class Utilitaire {
    public static List<String> scanControllers(String packageToScan) {
        List<String> controllers = new ArrayList<>();
        ClassGraph cg = new ClassGraph().enableClassInfo().enableAnnotationInfo();
        if (packageToScan != null && !packageToScan.trim().isEmpty()) {
            cg.acceptPackages(packageToScan);
        }
        try (ScanResult scanResult = cg.scan()) {
            controllers = scanResult.getClassesWithAnnotation("com.example.servlet.Controller").getNames();
        }
        return controllers;
    }
}