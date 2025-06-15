<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des trajets</title>
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
        
        .form-section {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: var(--shadow);
            margin-bottom: 2rem;
            max-width: 600px;
        }
        
        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: var(--text-color);
        }
        
        select {
            width: 100%;
            padding: 0.8rem;
            border: 1px solid var(--table-border);
            border-radius: 4px;
            font-size: 1rem;
            transition: var(--transition);
            max-width: 400px;
            box-sizing: border-box;
            margin-bottom: 1rem;
            background-color: white;
        }
        
        select:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
        }
        
        .action-button {
            padding: 0.8rem 1.5rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: var(--transition);
            margin-top: 0.5rem;
        }
        
        .action-button:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        
        .action-button.delete {
            background-color: var(--error-color);
        }
        
        .action-button.delete:hover {
            background-color: #d11a2a;
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
        
        .action-buttons {
            display: flex;
            gap: 0.5rem;
        }
        
        form {
            margin: 0;
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
            th, td {
                padding: 0.8rem;
            }
            
            .action-buttons {
                flex-direction: column;
                gap: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>

    <h2>Gestion des trajets</h2>

    <c:if test="${not empty success}">
        <p class="success">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <div class="form-section">
        <h3>Ajouter un trajet</h3>
        <form action="${pageContext.request.contextPath}/admin/trajets" method="post">
            <input type="hidden" name="action" value="add">
            <label>Gare de départ:
                <select name="departStationId" required>
                    <c:forEach var="station" items="${stations}">
                        <option value="${station.id}">${station.name}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Gare d'arrivée:
                <select name="arrivalStationId" required>
                    <c:forEach var="station" items="${stations}">
                        <option value="${station.id}">${station.name}</option>
                    </c:forEach>
                </select>
            </label>
            <button type="submit" class="action-button">Ajouter</button>
        </form>
    </div>

    <c:if test="${not empty trajets}">
        <table>
            <tr>
                <th>ID</th>
                <th>Gare Départ</th>
                <th>Gare Arrivée</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="trajet" items="${trajets}">
                <tr>
                    <td>${trajet.id}</td>
                    <td>${trajet.departStation.name}</td>
                    <td>${trajet.arrivalStation.name}</td>
                    <td>
                        <div class="action-buttons">
                            <form action="${pageContext.request.contextPath}/admin/trajets" method="get">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id" value="${trajet.id}">
                                <button type="submit" class="action-button">Modifier</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin/trajets" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${trajet.id}">
                                <button type="submit" class="action-button delete">Supprimer</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <c:if test="${not empty trajet}">
            <div class="form-section">
                <h3>Modifier un trajet</h3>
                <form action="${pageContext.request.contextPath}/admin/trajets" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${trajet.id}">
                    <label>Gare de départ:
                        <select name="departStationId" required>
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.id}" ${station.id == trajet.departStation.id ? 'selected' : ''}>${station.name}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <label>Gare d'arrivée:
                        <select name="arrivalStationId" required>
                            <c:forEach var="station" items="${stations}">
                                <option value="${station.id}" ${station.id == trajet.arrivalStation.id ? 'selected' : ''}>${station.name}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <button type="submit" class="action-button">Mettre à jour</button>
                </form>
            </div>
        </c:if>
    </c:if>

    <c:if test="${empty trajets}">
        <p>Aucun trajet trouvé.</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/monCompte" class="back-link">Retour à mon compte</a>
</body>
</html>