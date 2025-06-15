<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Historique des Voyages</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --text-color: #2b2d42;
            --light-gray: #f8f9fa;
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
        
        h2 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            font-weight: 600;
            font-size: 2rem;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
        }
        
        .empty-message {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            text-align: center;
            font-size: 1.1rem;
            color: #666;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
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
        
        .station-info {
            display: flex;
            flex-direction: column;
        }
        
        .station-city {
            font-size: 0.85rem;
            color: #666;
            margin-top: 0.25rem;
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
        
        .back-link {
            display: inline-block;
            margin-top: 1.5rem;
            padding: 0.8rem 1.5rem;
            background-color: var(--primary-color);
            color: white;
            border-radius: 4px;
            text-decoration: none;
        }
        
        .back-link:hover {
            background-color: var(--secondary-color);
            text-decoration: none;
        }
        
        @media (max-width: 768px) {
            th, td {
                padding: 0.8rem;
            }
            
            .station-info {
                flex-direction: row;
                gap: 0.5rem;
            }
            
            .station-city {
                margin-top: 0;
            }
        }
    </style>
</head>
<body>
    <h2>Historique de vos voyages</h2>
    
    <c:if test="${empty reservations}">
        <div class="empty-message">
            Aucun voyage utilisé trouvé.
        </div>
    </c:if>
    
    <c:if test="${not empty reservations}">
        <table>
            <thead>
                <tr>
                    <th>ID Réservation</th>
                    <th>Gare Départ</th>
                    <th>Gare Arrivée</th>
                    <th>Date Réservation</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="reservation" items="${reservations}">
                    <tr>
                        <td>${reservation.id}</td>
                        <td>
                            <div class="station-info">
                                <span>${reservation.voyage.trajet.departStation.name}</span>
                                <span class="station-city">${reservation.voyage.trajet.departStation.city}</span>
                            </div>
                        </td>
                        <td>
                            <div class="station-info">
                                <span>${reservation.voyage.trajet.arrivalStation.name}</span>
                                <span class="station-city">${reservation.voyage.trajet.arrivalStation.city}</span>
                            </div>
                        </td>
                        <td>${reservation.dateReservation}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    
    <a href="recherche" class="back-link">Retour à la recherche</a>
</body>
</html>