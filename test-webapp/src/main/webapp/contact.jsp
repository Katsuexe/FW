<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire de contact</title>
    <style>
        body { font-family: sans-serif; max-width: 500px; margin: 40px auto; }
        .error { color: red; font-size: 0.9em; }
        .success { color: green; font-weight: bold; margin-bottom: 1em; }
        input { display: block; width: 100%; padding: 6px; margin: 4px 0 12px; box-sizing: border-box; }
        button { padding: 8px 20px; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Formulaire de contact</h1>

    <%-- Affichage du message de succès si le binding a réussi --%>
    <% if (request.getAttribute("success") != null) { %>
        <p class="success">
            Bonjour <strong><%= request.getAttribute("nom") %></strong>
            <%= request.getAttribute("prenom") %> !
        </p>
    <% } %>

    <%-- Affichage du formulaire avec réaffichage des valeurs saisies --%>
    <form action="contact" method="POST">

        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom"
               value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>">
        <% if (request.getAttribute("erreur_nom") != null) { %>
            <span class="error"><%= request.getAttribute("erreur_nom") %></span>
        <% } %>

        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom"
               value="<%= request.getAttribute("prenom") != null ? request.getAttribute("prenom") : "" %>">
        <% if (request.getAttribute("erreur_prenom") != null) { %>
            <span class="error"><%= request.getAttribute("erreur_prenom") %></span>
        <% } %>

        <button type="submit">Envoyer</button>
    </form>
</body>
</html>
