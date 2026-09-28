package rindra.framework.model;

import java.util.Objects;

/**
 * Clé de recherche utilisée dans la map des routes.
 * La combinaison URL + méthode HTTP permet d'identifier une route unique.
 */
public class UrlKey {
    private String url;
    private String httpMethod;

    public UrlKey(String url, String httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod != null ? httpMethod.toUpperCase() : null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod != null ? httpMethod.toUpperCase() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlKey urlKey = (UrlKey) o;
        return Objects.equals(url, urlKey.url) &&
               Objects.equals(httpMethod, urlKey.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod);
    }
}