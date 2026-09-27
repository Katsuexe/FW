package rindra.framework;

import rindra.framework.annotation.Controller;
import rindra.framework.annotation.ResponseBody;
import rindra.framework.annotation.UrlMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    @UrlMapping(value = "/hello", method = {"GET"})
    public String hello() {
        return "Hello World GET!";
    }

    @UrlMapping(value = "/hello", method = {"POST"})
    public String helloPost() {
        return "Hello World POST!";
    }

    @UrlMapping(value = "/fling", method = {"POST"})
    public String fling() {
        return "Fling balls!";
    }

    @UrlMapping(value = "/error", method = {"GET"})
    public String throwError() {
        throw new RuntimeException("This is a deliberate error for testing!");
    }

    @UrlMapping(value = "/api/eleve", method = {"GET"})
    @ResponseBody
    public Map<String, Object> eleve() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", "Hello");
        data.put("nom", "Rakoto");
        data.put("notes", List.of(12, 15, 18));
        return data;
    }

    @UrlMapping(value = "/api/message", method = {"GET"})
    @ResponseBody
    public String apiMessage() {
        return "Secretaire/profil_eleve";
    }
}
