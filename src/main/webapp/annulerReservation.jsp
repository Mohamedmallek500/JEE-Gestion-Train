<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Annuler Réservation</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #28a745;
            --error-color: #dc3545;
            --text-color: #2b2d42;
            --light-gray: #f8f9fa;
            --card-bg: #ffffff;
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
            max-width: 800px;
            margin: 0 auto;
        }
        
        .confirmation-card {
            background-color: var(--card-bg);
            padding: 2rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 2rem;
        }
        
        h2 {
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            font-weight: 600;
            font-size: 2rem;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
        }
        
        p {
            margin-bottom: 1rem;
            font-size: 1.1rem;
        }
        
        strong {
            color: var(--primary-color);
            font-weight: 600;
        }
        
        .success {
            color: var(--success-color);
            background-color: rgba(40, 167, 69, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid var(--success-color);
        }
        
        .error {
            color: var(--error-color);
            background-color: rgba(220, 53, 69, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid var(--error-color);
        }
        
        button {
            padding: 0.8rem 1.5rem;
            background-color: var(--error-color);
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: var(--transition);
            margin-top: 1rem;
        }
        
        button:hover {
            background-color: #c82333;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
        
        .reservation-details {
            background-color: rgba(67, 97, 238, 0.05);
            padding: 1.5rem;
            border-radius: 6px;
            margin: 1.5rem 0;
            border-left: 3px solid var(--primary-color);
        }
        
        @media (max-width: 768px) {
            .confirmation-card {
                padding: 1.5rem;
            }
            
            h2 {
                font-size: 1.8rem;
            }
        }
    </style>
</head>
<body>
    <div class="confirmation-card">
        <h2>Annuler votre réservation</h2>
        
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        
        <c:if test="${not empty reservation}">
            <p>Êtes-vous sûr de vouloir annuler la réservation suivante ?</p>
            
            <div class="reservation-details">
                <p><strong>Gare Départ :</strong> ${reservation.voyage.trajet.departStation.name}</p>
                <p><strong>Gare Arrivée :</strong> ${reservation.voyage.trajet.arrivalStation.name}</p>
                <p><strong>Date :</strong> ${reservation.dateReservation}</p>
            </div>
            
            <form action="annulerReservation" method="post">
                <input type="hidden" name="id" value="${reservation.id}">
                <button type="submit">Confirmer l'annulation</button>
            </form>
        </c:if>
        
        <a href="monCompte" class="back-link">Retour à mon compte</a>
    </div>
</body>
</html>