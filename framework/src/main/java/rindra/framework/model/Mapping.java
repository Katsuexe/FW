package rindra.framework.model;

/**
 * Représente l'association entre une route HTTP et la méthode qui la traite.
 * Il contient le nom du contrôleur, le nom de la méthode cible et un indicateur
 * pour savoir si la réponse doit être renvoyée en JSON.
 */
public class Mapping {
    private String className;
    private String method;
    private boolean jsonResponse;

    public Mapping(String className, String method) {
        this(className, method, false);
    }

    public Mapping(String className, String method, boolean jsonResponse) {
        this.className = className;
        this.method = method;
        this.jsonResponse = jsonResponse;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(boolean jsonResponse) {
        this.jsonResponse = jsonResponse;
    }
}
