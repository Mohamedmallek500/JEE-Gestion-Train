<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des utilisateurs</title>
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
        }
        
        a:hover {
            color: var(--secondary-color);
            text-decoration: underline;
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
        
        .action-button {
            padding: 0.5rem 1rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 0.9rem;
            font-weight: 500;
            cursor: pointer;
            transition: var(--transition);
        }
        
        .action-button:hover {
            background-color: var(--secondary-color);
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            opacity: 1;
        }
        
        .action-button.block {
            background-color: var(--error-color);
        }
        
        .action-button.block:hover {
            background-color: #d11a2a;
        }
        
        .back-link {
            display: inline-block;
            margin-top: 1.5rem;
            padding: 0.5rem 1rem;
            background-color: var(--primary-color);
            color: white;
            border-radius: 4px;
            text-decoration: none;
        }
        
        .back-link:hover {
            background-color: var(--secondary-color);
            text-decoration: none;
        }
        
        form {
            display: inline;
            margin-right: 0.5rem;
        }
        
        @media (max-width: 768px) {
            th, td {
                padding: 0.8rem 0.5rem;
                font-size: 0.9rem;
            }
            
            .action-button {
                padding: 0.4rem 0.8rem;
                font-size: 0.8rem;
            }
        }
    </style>
</head>
<body>
    <%@ include file="navbar.jsp" %>

    <h2>Gestion des utilisateurs</h2>

    <c:if test="${not empty success}">
        <p class="success">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <c:if test="${not empty users}">
        <table>
            <tr>
                <th>ID</th>
                <th>Nom d'utilisateur</th>
                <th>Email</th>
                <th>Rôle</th>
                <th>État</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.blocked ? 'Bloqué' : 'Actif'}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.blocked}">
                                <form action="${pageContext.request.contextPath}/admin/users" method="post">
                                    <input type="hidden" name="id" value="${user.id}">
                                    <input type="hidden" name="action" value="unblock">
                                    <button type="submit" class="action-button">Débloquer</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/admin/users" method="post">
                                    <input type="hidden" name="id" value="${user.id}">
                                    <input type="hidden" name="action" value="block">
                                    <button type="submit" class="action-button block">Bloquer</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${empty users}">
        <p>Aucun utilisateur trouvé.</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/monCompte" class="back-link">Retour à mon compte</a>
</body>
</html>