<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Recherche de Trajets</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --error-color: #ef233c;
            --promo-color: #2ecc71;
            --text-color: #2b2d42;
            --light-gray: #f8f9fa;
            --white: #ffffff;
            --table-header-bg: #f1f3f9;
            --table-border: #e0e0e0;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --transition: all 0.3s ease;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-gray);
            margin: 0;
            padding: 20px;
            color: var(--text-color);
            line-height: 1.6;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        h2, h3 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        
        .form-container {
            background-color: var(--white);
            padding: 2rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 2rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
        }
        
        label {
            flex: 0 0 180px;
            font-weight: 500;
            color: var(--text-color);
        }
        
        select, input[type="date"] {
            flex: 1;
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
            transition: var(--transition);
            background-color: var(--white);
        }
        
        select:focus, input[type="date"]:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
        }
        
        button {
            padding: 0.8rem 1.5rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: var(--transition);
            margin-top: 0.5rem;
            width: auto;
            display: inline-block;
        }
        
        button:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        
        .error {
            color: var(--error-color);
            background-color: rgba(239, 35, 60, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
        }
        
        table {
            border-collapse: collapse;
            width: 100%;
            margin: 2rem 0;
            box-shadow: var(--shadow);
            border-radius: 8px;
            overflow: hidden;
        }
        
        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid var(--table-border);
        }
        
        th {
            background-color: var(--table-header-bg);
            font-weight: 600;
            color: var(--text-color);
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }
        
        tr:hover {
            background-color: rgba(67, 97, 238, 0.05);
        }
        
        .promotion {
            color: var(--promo-color);
            font-weight: bold;
        }
        
        a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
            transition: var(--transition);
        }
        
        a:hover {
            color: var(--secondary-color);
            text-decoration: underline;
        }
        
        ul {
            margin: 0;
            padding-left: 1.2rem;
        }
        
        li {
            margin-bottom: 0.3rem;
        }
        
        .action-link {
            display: inline-block;
            padding: 0.5rem 1rem;
            background-color: var(--primary-color);
            color: white;
            border-radius: 4px;
            text-decoration: none;
            transition: var(--transition);
        }
        
        .action-link:hover {
            background-color: var(--secondary-color);
            text-decoration: none;
            transform: translateY(-2px);
        }
        
        .nav-links {
            margin-bottom: 1.5rem;
            display: flex;
            gap: 1rem;
        }
        
        .nav-links a {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            background-color: var(--light-gray);
        }
        
        .nav-links a:hover {
            background-color: #e0e0e0;
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="form-container">
    <h2>Rechercher un trajet</h2>
    <c:if test="${not empty error}">
        <p class="error" aria-live="polite">${error}</p>
    </c:if>
    <form action="recherche" method="post">
        <div class="form-group">
            <label for="villeDepart">Ville de départ :</label>
            <select id="villeDepart" name="villeDepart" required>
                <option value="">Sélectionnez une ville</option>
                <c:forEach var="city" items="${cities}">
                    <option value="${city}" ${city == villeDepart ? 'selected' : ''}>${city}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="villeDestination">Ville de destination :</label>
            <select id="villeDestination" name="villeDestination" required>
                <option value="">Sélectionnez une ville</option>
                <c:forEach var="city" items="${cities}">
                    <option value="${city}" ${city == villeDestination ? 'selected' : ''}>${city}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="date">Date de départ :</label>
            <input type="date" id="date" name="date" value="${date}" required>
        </div>
        <button type="submit">Rechercher</button>
    </form>
</div>

<c:if test="${not empty voyages}">
    <h3>Résultats de la recherche</h3>
    <table>
        <tr>
            <th>Gare de départ</th>
            <th>Gare d'arrivée</th>
            <th>Heure de départ</th>
            <th>Heure d'arrivée</th>
            <th>Durée</th>
            <th>Prix</th>
            <th>Places disponibles</th>
            <th>Promotions</th>
            <th>Action</th>
        </tr>
        <c:forEach var="voyage" items="${voyages}">
            <tr>
                <td>${voyage.trajet.departStation.name} (${voyage.trajet.departStation.city})</td>
                <td>${voyage.trajet.arrivalStation.name} (${voyage.trajet.arrivalStation.city})</td>
                <td>${voyage.heureDepartFormatted}</td>
                <td>${voyage.heureArriveeFormatted}</td>
                <td>${voyage.duree}</td>
                <td>
                    <c:set var="originalPrice" value="${voyage.prix}"/>
                    <c:set var="bestDiscount" value="0"/>
                    <c:forEach var="promo" items="${voyagePromotions[voyage.id]}">
                        <c:if test="${promo.discountPercentage > bestDiscount}">
                            <c:set var="bestDiscount" value="${promo.discountPercentage}"/>
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${bestDiscount > 0}">
                            <span style="text-decoration: line-through; color: #999;">${originalPrice} €</span>
                            <span class="promotion">${originalPrice * (1 - bestDiscount / 100)} € (-${bestDiscount}%)</span>
                        </c:when>
                        <c:otherwise>
                            ${originalPrice} €
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${voyage.placesDisponibles}</td>
                <td>
                    <c:if test="${not empty voyagePromotions[voyage.id]}">
                        <ul>
                            <c:forEach var="promo" items="${voyagePromotions[voyage.id]}">
                                <li>${promo.code}: ${promo.description} (-${promo.discountPercentage}%)</li>
                            </c:forEach>
                        </ul>
                    </c:if>
                </td>
                <td>
                    <a href="selection?voyageId=${voyage.id}" class="action-link">Sélectionner</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>