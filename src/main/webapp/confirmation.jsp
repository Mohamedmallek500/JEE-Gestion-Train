<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmation de la Réservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            max-width: 800px;
            margin-left: auto;
            margin-right: auto;
        }
        h2 {
            color: #333;
        }
        .success { color: green; margin-bottom: 15px; }
        .error { color: red; margin-bottom: 15px; }
        a {
            color: #007BFF;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h2>Confirmation de la réservation</h2>
    <c:if test="${not empty success}">
        <p class="success">${success} (ID de réservation : ${reservationId})</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    <c:if test="${not empty voyage}">
        <h3>Détails du voyage</h3>
        <p><strong>Gare de départ :</strong> ${voyage.trajet.departStation.name} (${voyage.trajet.departStation.city})</p>
        <p><strong>Gare d'arrivée :</strong> ${voyage.trajet.arrivalStation.name} (${voyage.trajet.arrivalStation.city})</p>
        <p><strong>Heure de départ :</strong> ${voyage.heureDepartFormatted}</p>
        <p><strong>Heure d'arrivée :</strong> ${voyage.heureArriveeFormatted}</p>
        <p><strong>Durée :</strong> ${voyage.duree}</p>
        <p><strong>Prix :</strong> ${voyage.prix} €</p>
        <p><strong>Classe :</strong> ${classe}</p>
        <p><strong>Préférences :</strong> 
            <c:if test="${not empty preferences}">
                <c:forEach var="pref" items="${preferences}">
                    ${pref}<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
            </c:if>
            <c:if test="${empty preferences}">Aucune</c:if>
        </p>
        <p><strong>État :</strong> ${etat}</p>
        <p><strong>Points de fidélité actuels :</strong> ${loyaltyPoints}</p>
        <p><strong>Prochain trajet :</strong> 
            <c:choose>
                <c:when test="${not empty nextTrajetId}">
                    Trajet sélectionné (ID: ${nextTrajetId}). <a href="recherche?villeDepart=${voyage.trajet.arrivalStation.city}">Rechercher un voyage</a>
                </c:when>
                <c:otherwise>Aucun</c:otherwise>
            </c:choose>
        </p>
    </c:if>
    <p><a href="recherche">Retour à la recherche</a></p>
</body>
</html>