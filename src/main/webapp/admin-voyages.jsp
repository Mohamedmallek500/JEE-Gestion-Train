<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Espace Administrateur - Voyages</title>
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
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 0.5rem;
        }
        
        h3 {
            color: var(--secondary-color);
            margin: 2rem 0 1rem;
            font-weight: 500;
        }
        
        .form-container {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 2rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
            display: flex;
            flex-wrap: wrap;
            align-items: center;
        }
        
        label {
            flex: 0 0 200px;
            font-weight: 500;
            color: var(--text-color);
            margin-bottom: 0.5rem;
        }
        
        input, select {
            flex: 1;
            padding: 0.8rem;
            border: 1px solid var(--table-border);
            border-radius: 4px;
            font-size: 1rem;
            transition: var(--transition);
            min-width: 250px;
            max-width: 400px;
        }
        
        input[type="datetime-local"] {
            /* Style spécifique pour les champs datetime */
            padding: 0.7rem;
        }
        
        input:focus, select:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
        }
        
        button {
            padding: 0.8rem 1.5rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: var(--transition);
            margin-right: 1rem;
        }
        
        button:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
        
        .actions {
            display: flex;
            gap: 0.5rem;
        }
        
        .actions a {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            transition: var(--transition);
        }
        
        .actions a:first-child {
            background-color: var(--success-color);
            color: white;
        }
        
        .actions a:last-child {
            background-color: var(--error-color);
            color: white;
        }
        
        .actions a:hover {
            opacity: 0.9;
            transform: translateY(-1px);
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
            margin-top: 2rem;
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
            .form-group {
                flex-direction: column;
                align-items: flex-start;
            }
            
            label {
                flex: 1;
                margin-bottom: 0.5rem;
            }
            
            input, select {
                width: 100%;
                max-width: 100%;
            }
            
            .actions {
                flex-direction: column;
                gap: 0.5rem;
            }
            
            .actions a {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
    <h2>Espace Administrateur - Gestion des Voyages</h2>
    
    <div class="form-container">
        <h3>Ajouter un voyage</h3>
        <form action="voyages" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label for="trajetId">Trajet :</label>
                <select id="trajetId" name="trajetId" required>
                    <c:forEach var="trajet" items="${trajets}">
                        <option value="${trajet.id}">
                            ${trajet.departStation.name} → ${trajet.arrivalStation.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="heureDepart">Heure de départ :</label>
                <input type="datetime-local" id="heureDepart" name="heureDepart" required>
            </div>
            <div class="form-group">
                <label for="heureArrivee">Heure d'arrivée :</label>
                <input type="datetime-local" id="heureArrivee" name="heureArrivee" required>
            </div>
            <div class="form-group">
                <label for="prix">Prix :</label>
                <input type="number" id="prix" name="prix" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="placesDisponibles">Places disponibles :</label>
                <input type="number" id="placesDisponibles" name="placesDisponibles" required>
            </div>
            <button type="submit">Ajouter</button>
        </form>
    </div>

    <h3>Liste des voyages</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Trajet</th>
            <th>Heure de départ</th>
            <th>Heure d'arrivée</th>
            <th>Prix</th>
            <th>Places disponibles</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="voyage" items="${voyages}">
            <tr>
                <td>${voyage.id}</td>
                <td>${voyage.trajet.departStation.name} → ${voyage.trajet.arrivalStation.name}</td>
                <td>${voyage.heureDepartFormatted}</td>
                <td>${voyage.heureArriveeFormatted}</td>
                <td>${voyage.prix} €</td>
                <td>${voyage.placesDisponibles}</td>
                <td>
                    <div class="actions">
                        <a href="voyages?action=edit&id=${voyage.id}">Modifier</a>
                        <a href="voyages?action=delete&id=${voyage.id}" 
                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce voyage ?')">Supprimer</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${not empty voyage}">
        <div class="form-container">
            <h3>Modifier un voyage</h3>
            <form action="voyages" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${voyage.id}">
                <div class="form-group">
                    <label for="trajetId">Trajet :</label>
                    <select id="trajetId" name="trajetId" required>
                        <c:forEach var="trajet" items="${trajets}">
                            <option value="${trajet.id}" ${trajet.id == voyage.trajet.id ? 'selected' : ''}>
                                ${trajet.departStation.name} → ${trajet.arrivalStation.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="heureDepart">Heure de départ :</label>
                    <input type="datetime-local" id="heureDepart" name="heureDepart" value="${voyage.heureDepart}" required>
                </div>
                <div class="form-group">
                    <label for="heureArrivee">Heure d'arrivée :</label>
                    <input type="datetime-local" id="heureArrivee" name="heureArrivee" value="${voyage.heureArrivee}" required>
                </div>
                <div class="form-group">
                    <label for="prix">Prix :</label>
                    <input type="number" id="prix" name="prix" step="0.01" value="${voyage.prix}" required>
                </div>
                <div class="form-group">
                    <label for="placesDisponibles">Places disponibles :</label>
                    <input type="number" id="placesDisponibles" name="placesDisponibles" value="${voyage.placesDisponibles}" required>
                </div>
                <button type="submit">Mettre à jour</button>
            </form>
        </div>
    </c:if>

    <a href="../admin" class="back-link">Retour à l'espace admin</a>
</body>
</html>