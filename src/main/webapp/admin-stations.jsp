<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Espace Administrateur - Gares</title>
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
            max-width: 600px;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
            color: var(--text-color);
        }
        
        input {
            width: 100%;
            padding: 0.8rem;
            border: 1px solid var(--table-border);
            border-radius: 4px;
            font-size: 1rem;
            transition: var(--transition);
            max-width: 400px;
            box-sizing: border-box;
        }
        
        input:focus {
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
        
        a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
            transition: var(--transition);
            margin-right: 1rem;
        }
        
        a:hover {
            color: var(--secondary-color);
            text-decoration: underline;
        }
        
        .actions a {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 4px;
        }
        
        .actions a:last-child {
            color: var(--error-color);
        }
        
        .actions a:last-child:hover {
            color: #d11a2a;
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
            
            .actions a {
                display: block;
                margin-bottom: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>

    <h2>Espace Administrateur - Gestion des Gares</h2>
    
    <div class="form-container">
        <h3>Ajouter une gare</h3>
        <form action="stations" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label for="name">Nom :</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="city">Ville :</label>
                <input type="text" id="city" name="city" required>
            </div>
            <button type="submit">Ajouter</button>
        </form>
    </div>

    <h3>Liste des gares</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Ville</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="station" items="${stations}">
            <tr>
                <td>${station.id}</td>
                <td>${station.name}</td>
                <td>${station.city}</td>
                <td class="actions">
                    <a href="stations?action=edit&id=${station.id}">Modifier</a>
                    <a href="stations?action=delete&id=${station.id}" 
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette gare ?')">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${not empty station}">
        <div class="form-container">
            <h3>Modifier une gare</h3>
            <form action="stations" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${station.id}">
                <div class="form-group">
                    <label for="name">Nom :</label>
                    <input type="text" id="name" name="name" value="${station.name}" required>
                </div>
                <div class="form-group">
                    <label for="city">Ville :</label>
                    <input type="text" id="city" name="city" value="${station.city}" required>
                </div>
                <button type="submit">Mettre à jour</button>
            </form>
        </div>
    </c:if>

    <a href="${pageContext.request.contextPath}/monCompte" class="back-link">Retour à mon compte</a>
    
</body>
</html>