package rindra.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indique qu'une méthode est associée à une URL et à un ou plusieurs verbes HTTP.
 * Exemple : @UrlMapping(value = "/hello", method = {"GET"})
 */


// @retention est une annotation qui indique que l'annotation 
// @ResponseBody sera disponible à l'exécution (runtime) et pourra être interrogée via la réflexion. 
// Cela signifie que les informations de l'annotation seront conservées dans le bytecode et pourront être utilisées par le framework 
// Pour déterminer comment traiter la réponse de la méthode annotée.
@Retention(RetentionPolicy.RUNTIME)

// @Target est une annotation qui indique que l'annotation @ResponseBody peut être appliquée uniquement aux méthodes.
@Target(ElementType.METHOD)

public @interface UrlMapping {
    String value();
    String[] method() default {"GET", "POST"};
}