# Guide du Sprint 7 : Binding de formulaire (Vue → Contrôleur)

## Objectif
Lier un formulaire HTML à une méthode de contrôleur en passant par une classe dédiée `FormBinder`.
Champs texte uniquement. Aucune gestion de fichier.

## Flux complet

```
Navigateur
  │  POST /contact  (nom=Alice, prenom=Dupont)
  ▼
FrontControllerServlet
  │  injecte HttpServletRequest dans la méthode du contrôleur
  ▼
TestController.contactSubmit(HttpServletRequest request)
  │  appelle FormBinder.bind(request, "nom", "prenom")
  ▼
FormBinder
  │  lit, trim, vérifie → retourne un BindingResult
  ▼
TestController (suite)
  │  isValid() ? → construit le ModelAndView avec les données OU les erreurs
  ▼
FrontControllerServlet
  │  injecte les attributs du ModelAndView dans la request
  ▼
contact.jsp
  │  affiche le message de succès OU le formulaire avec erreurs + valeurs
  ▼
Navigateur
```

## 1. `FormBinder` (nouveau fichier — framework)

**Fichier :** `framework/src/main/java/rindra/framework/util/FormBinder.java`

**Pourquoi :** centraliser toute la logique de lecture et validation des paramètres HTTP dans un seul endroit. `FrontControllerServlet` reste neutre ; le contrôleur lui-même reste déclaratif.

**Ce que fait `FormBinder.bind(request, "nom", "prenom")` :**
1. Pour chaque nom de champ fourni, lit `request.getParameter(nom)`.
2. Vérifie que la valeur n'est pas `null` (champ absent de la requête).
3. Fait un `trim()` et vérifie que la valeur n'est pas vide.
4. Stocke le résultat dans un `BindingResult` (valeurs validées + erreurs).

**`BindingResult` expose :**
- `isValid()` → `true` si aucune erreur
- `getValue("nom")` → valeur nettoyée
- `getError("nom")` → message d'erreur ou `null`

## 2. `FrontControllerServlet` (modification minimale)

**Fichier :** `framework/src/main/java/rindra/framework/servlet/FrontControllerServlet.java`

**Pourquoi :** les méthodes de contrôleur peuvent maintenant prendre un `HttpServletRequest` en paramètre pour accéder à `FormBinder`. Le dispatcher doit donc l'injecter automatiquement.

**Ce qui a changé :**
- Recherche de la méthode par nom (boucle) au lieu de `getDeclaredMethod(nom)` — car on ne connaît pas la signature exacte à l'avance.
- Construction d'un tableau `args[]` : si un paramètre est de type `HttpServletRequest`, on l'y met ; sinon `null`.
- Appel `targetMethod.invoke(controllerInstance, args)` au lieu de `invoke(controllerInstance)`.

## 3. `TestController` (démo)

Deux routes dans `test-webapp/src/main/java/rindra/framework/TestController.java` :

| Verbe | URL | Rôle |
|-------|-----|------|
| `GET` | `/contact` | Affiche le formulaire vide |
| `POST` | `/contact` | Reçoit, valide, et réaffiche |

## 4. `contact.jsp` (vue de démo)

**Fichier :** `test-webapp/src/main/webapp/contact.jsp`

La vue se charge de :
- afficher le formulaire avec les valeurs déjà saisies (attributs `nom`, `prenom`)
- afficher les erreurs par champ si le flag `erreur_nom` / `erreur_prenom` est présent
- afficher un message de succès si l'attribut `success` est présent

## 5. Étapes de test

```bash
# 1. Déployer
./deploy.sh

# 2. Accéder au formulaire
http://localhost:8080/test-webapp/contact

# 3. Cas à tester manuellement :
#    a. Soumettre le formulaire avec nom et prénom remplis → message de succès
#    b. Soumettre avec un champ vide → erreur sous le champ vide, valeur de l'autre champ conservée
#    c. Soumettre avec les deux champs vides → deux erreurs affichées
```
