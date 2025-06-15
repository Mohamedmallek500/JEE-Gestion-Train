<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mon Compte</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --error-color: #ef233c;
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
        }
        
        h3 {
            color: var(--secondary-color);
            margin: 1.5rem 0 1rem;
            font-weight: 500;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
            display: inline-block;
        }
        
        .section {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 2rem;
            transition: var(--transition);
        }
        
        .section:hover {
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 1.5rem 0;
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
        
        a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
            transition: var(--transition);
            padding: 0.3rem 0.5rem;
            border-radius: 4px;
        }
        
        a:hover {
            color: var(--secondary-color);
            background-color: rgba(67, 97, 238, 0.1);
            text-decoration: none;
        }
        
        .success {
            color: #2ecc71;
            background-color: rgba(46, 204, 113, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid #2ecc71;
        }
        
        .error {
            color: var(--error-color);
            background-color: rgba(239, 35, 60, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid var(--error-color);
        }
        
        .action-links {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
        }
        
        .action-links a {
            white-space: nowrap;
        }
        
        .action-links a:not(:last-child)::after {
            content: "|";
            margin-left: 0.5rem;
            color: #ccc;
        }
        
        .bottom-links {
            margin-top: 2rem;
            display: flex;
            gap: 1rem;
        }
        
        .bottom-links a {
            padding: 0.5rem 1rem;
            background-color: var(--primary-color);
            color: white;
            border-radius: 4px;
        }
        
        .bottom-links a:hover {
            background-color: var(--secondary-color);
            color: white;
        }
        
        @media (max-width: 768px) {
            th, td {
                padding: 0.8rem 0.5rem;
                font-size: 0.9rem;
            }
            
            .action-links {
                flex-direction: column;
                gap: 0.3rem;
            }
            
            .action-links a:not(:last-child)::after {
                content: none;
            }
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
    <h2>Mon Compte</h2>
    <c:if test="${not empty success}">
        <p class="success">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <c:if test="${sessionScope.user.role == 'admin'}">
        <div class="section">
            <h3>Administration</h3>
            <p><a href="${pageContext.request.contextPath}/admin/annuler">Gérer les demandes d'annulation</a></p>
            
            <h3>Toutes les réservations achetées</h3>
            <c:if test="${not empty allPurchasedReservations}">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Utilisateur</th>
                        <th>Gare Départ</th>
                        <th>Gare Arrivée</th>
                        <th>État</th>
                        <th>Heure Départ</th>
                        <th>Heure Arrivée</th>
                        <th>Actions</th>
                    </tr>
                    <c:forEach var="reservation" items="${allPurchasedReservations}">
                        <tr>
                            <td>${reservation.id}</td>
                            <td>${reservation.user.username}</td>
                            <td>${reservation.voyage.trajet.departStation.name}</td>
                            <td>${reservation.voyage.trajet.arrivalStation.name}</td>
                            <td>${reservation.etat}</td>
                            <td>${reservation.voyage.heureDepartFormatted}</td>
                            <td>${reservation.voyage.heureArriveeFormatted}</td>
                            <td class="action-links">
                                <a href="${pageContext.request.contextPath}/modifierReservation?id=${reservation.id}">Modifier</a>
                                <a href="${pageContext.request.contextPath}/annulerReservation?id=${reservation.id}">Annuler</a>
                                <a href="${pageContext.request.contextPath}/telechargerBillet?id=${reservation.id}">PDF</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <c:if test="${empty allPurchasedReservations}">
                <p>Aucune réservation achetée trouvée.</p>
            </c:if>

            <h3>Historique des voyages de tous les utilisateurs</h3>
            <c:if test="${not empty allUsedReservations}">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Utilisateur</th>
                        <th>Gare Départ</th>
                        <th>Gare Arrivée</th>
                        <th>Date Réservation</th>
                        <th>État</th>
                    </tr>
                    <c:forEach var="reservation" items="${allUsedReservations}">
                        <tr>
                            <td>${reservation.id}</td>
                            <td>${reservation.user.username}</td>
                            <td>${reservation.voyage.trajet.departStation.name}</td>
                            <td>${reservation.voyage.trajet.arrivalStation.name}</td>
                            <td>${reservation.dateReservation}</td>
                            <td>${reservation.etat}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
            <c:if test="${empty allUsedReservations}">
                <p>Aucun voyage utilisé trouvé.</p>
            </c:if>
        </div>
    </c:if>

    <c:if test="${sessionScope.user.role != 'admin'}">
        <div class="section">
            <h3>Réservations achetées</h3>
            <c:if test="${not empty reservations}">
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Gare Départ</th>
                        <th>Gare Arrivée</th>
                        <th>État</th>
                        <th>Heure Départ</th>
                        <th>Heure Arrivée</th>
                        <th>Actions</th>
                    </tr>
                    <c:forEach var="reservation" items="${reservations}">
                        <c:if test="${reservation.etat == 'acheté'}">
                            <tr>
                                <td>${reservation.id}</td>
                                <td>${reservation.voyage.trajet.departStation.name}</td>
                                <td>${reservation.voyage.trajet.arrivalStation.name}</td>
                                <td>${reservation.etat}</td>
                                <td>${reservation.voyage.heureDepartFormatted}</td>
                                <td>${reservation.voyage.heureArriveeFormatted}</td>
                                <td class="action-links">
                                    <a href="${pageContext.request.contextPath}/modifierReservation?id=${reservation.id}">Modifier</a>
                                    <a href="${pageContext.request.contextPath}/annulerReservation?id=${reservation.id}">Annuler</a>
                                    <a href="${pageContext.request.contextPath}/telechargerBillet?id=${reservation.id}">PDF</a>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
            </c:if>
            <c:if test="${empty reservations}">
                <p>Aucune réservation trouvée.</p>
            </c:if>
        </div>

        <div class="section">
            <h3>Historique des voyages</h3>
            <p><a href="${pageContext.request.contextPath}/historique">Voir les billets utilisés</a></p>
        </div>
    </c:if>

    <div class="bottom-links">
        <a href="${pageContext.request.contextPath}/recherche">Retour à la recherche</a>
    </div>
</body>
</html>