<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sélection du Voyage</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 40px auto;
            max-width: 800px;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        h2, h3 {
            color: #0056b3;
            margin-top: 0;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 5px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            font-weight: 500;
            margin-right: 10px;
            display: inline-block;
            margin-bottom: 8px;
        }

        select, input[type="checkbox"] {
            padding: 8px;
            margin-right: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }

        select {
            min-width: 200px;
        }

        input[type="checkbox"] {
            transform: scale(1.2);
            vertical-align: middle;
        }

        button {
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3;
        }

        a {
            color: #007BFF;
            text-decoration: none;
            font-weight: 500;
        }

        a:hover {
            text-decoration: underline;
        }

        p {
            line-height: 1.6;
        }
    </style>
</head>
<body>
    <h2>Sélection du voyage</h2>
    <c:if test="${not empty voyage}">
        <h3>Détails du voyage</h3>
        <p><strong>Gare de départ :</strong> ${voyage.trajet.departStation.name} (${voyage.trajet.departStation.city})</p>
        <p><strong>Gare d'arrivée :</strong> ${voyage.trajet.arrivalStation.name} (${voyage.trajet.arrivalStation.city})</p>
        <p><strong>Heure de départ :</strong> ${voyage.heureDepartFormatted}</p>
        <p><strong>Heure d'arrivée :</strong> ${voyage.heureArriveeFormatted}</p>
        <p><strong>Durée :</strong> ${voyage.duree}</p>
        <p><strong>Prix :</strong> ${voyage.prix} €</p>
        <p><strong>Places disponibles :</strong> ${voyage.placesDisponibles}</p>

        <h3>Options de réservation</h3>
        <form action="selection" method="post">
            <div class="form-group">
                <label for="classe">Classe :</label>
                <select id="classe" name="classe" required>
                    <option value="Economique">Économique</option>
                    <option value="Deuxieme">2ème</option>
                    <option value="Premiere">1ère</option>
                </select>
            </div>
            <div class="form-group">
                <label>Préférences :</label>
                <input type="checkbox" id="fenetre" name="preferences" value="Fenêtre">
                <label for="fenetre">Place côté fenêtre</label>
                <input type="checkbox" id="famille" name="preferences" value="Espace famille">
                <label for="famille">Espace famille</label>
                <input type="checkbox" id="nonfumeur" name="preferences" value="Non-fumeur">
                <label for="nonfumeur">Wagon non-fumeur</label>
            </div>
            <div class="form-group">
                <label for="nextTrajetId">Réserver un autre trajet à partir de ${voyage.trajet.arrivalStation.city} ?</label>
                <select id="nextTrajetId" name="nextTrajetId">
                    <option value="">Non</option>
                    <c:forEach var="nextTrajet" items="${nextTrajets}">
                        <option value="${nextTrajet.id}">
                            ${nextTrajet.departStation.name} (${nextTrajet.departStation.city}) -> 
                            ${nextTrajet.arrivalStation.name} (${nextTrajet.arrivalStation.city})
                        </option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit">Confirmer la réservation</button>
        </form>
    </c:if>
    <p><a href="recherche">Retour à la recherche</a></p>
</body>
</html>
