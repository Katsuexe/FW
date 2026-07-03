package rindra.framework;

import rindra.framework.annotation.Controller;
import rindra.framework.annotation.UrlMapping;

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
}