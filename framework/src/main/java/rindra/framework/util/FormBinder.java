package rindra.framework.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Sprint 7 : Classe utilitaire chargée du binding entre un formulaire HTML et le contrôleur.
 *
 * Responsabilité unique : lire les paramètres texte d'une requête HTTP,
 * les valider et les retourner sous forme d'un Map prêt à être injecté
 * dans le ModelAndView ou passé au contrôleur.
 *
 * FrontControllerServlet n'a plus qu'à appeler FormBinder.bind() — toute
 * la logique de lecture et de vérification vit ici.
 */
public class FormBinder {

    /**
     * Lit et valide les paramètres texte de la requête.
     *
     * Pour chaque nom de champ attendu (fieldNames), on :
     *   1. lit la valeur brute avec request.getParameter(),
     *   2. on la nettoie (trim),
     *   3. on vérifie qu'elle n'est pas vide,
     *   4. on stocke la valeur dans "values" ou l'erreur dans "errors".
     *
     * @param request    la requête HTTP entrante
     * @param fieldNames les noms des champs à lire (doivent correspondre aux name= du formulaire HTML)
     * @return un BindingResult qui contient les valeurs validées et les erreurs éventuelles
     */
    public static BindingResult bind(HttpServletRequest request, String... fieldNames) {
        // Table des valeurs valides (nom → valeur nettoyée)
        Map<String, String> values = new LinkedHashMap<>();
        // Table des erreurs (nom → message d'erreur)
        Map<String, String> errors = new LinkedHashMap<>();

        for (String fieldName : fieldNames) {
            // Étape 1 : lecture brute depuis la requête HTTP.
            String raw = request.getParameter(fieldName);

            // Étape 2 : si le champ est absent de la requête, c'est une erreur.
            if (raw == null) {
                errors.put(fieldName, "Le champ '" + fieldName + "' est manquant dans la requête.");
                values.put(fieldName, "");
                continue;
            }

            // Étape 3 : nettoyage des espaces inutiles (trim).
            String trimmed = raw.trim();

            // Étape 4 : si le champ est vide après nettoyage, c'est une erreur de validation.
            if (trimmed.isEmpty()) {
                errors.put(fieldName, "Le champ '" + fieldName + "' ne peut pas être vide.");
                values.put(fieldName, "");
            } else {
                // Étape 5 : valeur valide, on l'enregistre.
                values.put(fieldName, trimmed);
            }
        }

        return new BindingResult(values, errors);
    }

    // -------------------------------------------------------------------------

    /**
     * Résultat du binding d'un formulaire.
     *
     * Contient deux maps :
     *   - values : les valeurs nettoyées (présentes même si vides, pour réafficher le formulaire)
     *   - errors : les messages d'erreur par champ (vide si tout est valide)
     */
    public static class BindingResult {
        private final Map<String, String> values;
        private final Map<String, String> errors;

        public BindingResult(Map<String, String> values, Map<String, String> errors) {
            this.values = values;
            this.errors = errors;
        }

        /** Retourne vrai si tous les champs sont valides (aucune erreur). */
        public boolean isValid() {
            return this.errors.isEmpty();
        }

        /** Retourne la valeur nettoyée d'un champ, ou une chaîne vide si absente. */
        public String getValue(String fieldName) {
            return this.values.getOrDefault(fieldName, "");
        }

        /** Retourne le message d'erreur d'un champ, ou null si le champ est valide. */
        public String getError(String fieldName) {
            return this.errors.get(fieldName);
        }

        /**
         * Expose toutes les valeurs dans un Map standard.
         * Utile pour injecter en bloc dans un ModelAndView.
         */
        public Map<String, String> getValues() {
            return this.values;
        }

        /**
         * Expose toutes les erreurs dans un Map standard.
         * Utile pour injecter en bloc dans un ModelAndView.
         */
        public Map<String, String> getErrors() {
            return this.errors;
        }
    }
}
