package rindra.framework.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Contient les données à transmettre à une vue ainsi que le nom de cette vue.
 * C'est le conteneur utilisé pour le rendu des pages HTML/JSP.
 */
public class ModelAndView {
    private String view;
    private Map<String, Object> model;

    public ModelAndView(String view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView() {
        this.model = new HashMap<>();
    }

    public String getView() {
        return this.view;
    }

    public Map<String, Object> getModel() {
        return this.model;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setAttribute(String key, Object value) {
        this.model.put(key, value);
    }
}
