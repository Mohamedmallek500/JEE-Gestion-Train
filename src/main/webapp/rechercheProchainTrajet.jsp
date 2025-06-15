<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Rechercher un prochain trajet</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; }
        input, button { padding: 5px; }
    </style>
</head>
<body>
    <h2>Rechercher un prochain trajet</h2>
    <p><strong>Ville de départ :</strong> ${villeDepart}</p>
    <form action="rechercheProchain" method="post">
        <input type="hidden" name="villeDepart" value="${villeDepart}">
        <div class="form-group">
            <label for="villeDestination">Destination :</label>
            <input type="text" id="villeDestination" name="villeDestination" required>
        </div>
        <div class="form-group">
            <label for="date">Date :</label>
            <input type="date" id="date" name="date" required>
        </div>
        <button type="submit">Rechercher</button>
    </form>
</body>
</html>