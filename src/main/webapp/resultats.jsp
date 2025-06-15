<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultats de recherche</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2>Résultats des trajets</h2>
    <c:choose>
        <c:when test="${empty trajets}">
            <p>Aucun trajet disponible pour ces critères.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Ville de départ</th>
                    <th>Destination</th>
                    <th>Heure de départ</th>
                    <th>Heure d'arrivée</th>
                    <th>Durée</th>
                    <th>Prix</th>
                    <th>Places disponibles</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="trajet" items="${trajets}">
                    <tr>
                        <td>${trajet.villeDepart}</td>
                        <td>${trajet.villeDestination}</td>
                     <td>${trajet.heureDepartFormatted}</td>
                        <td>${trajet.heureArriveeFormatted}</td>
                        <td>${trajet.duree}</td>
                        <td><fmt:formatNumber value="${trajet.prix}" type="currency" currencyCode="EUR"/></td>
                        <td>${trajet.placesDisponibles}</td>
                        <td><a href="selection?trajetId=${trajet.id}">Sélectionner</a></td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
    <p><a href="recherche">Nouvelle recherche</a></p>
</body>
</html>