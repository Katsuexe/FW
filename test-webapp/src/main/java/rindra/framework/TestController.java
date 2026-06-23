package rindra.framework;

import rindra.framework.annotation.Controller;
import rindra.framework.annotation.UrlMapping;

@Controller
public class TestController {

    @UrlMapping("/hello")
    public void hello() {
        System.out.println("Hello called!");
    }
}