<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Promotions</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --edit-color: #28a745;
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
        
        input, select, textarea {
            flex: 1;
            padding: 0.8rem;
            border: 1px solid var(--table-border);
            border-radius: 4px;
            font-size: 1rem;
            transition: var(--transition);
            min-width: 250px;
            max-width: 500px;
        }
        
        textarea {
            min-height: 100px;
            resize: vertical;
        }
        
        input:focus, select:focus, textarea:focus {
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
        
        .edit-button {
            background-color: var(--edit-color);
        }
        
        .edit-button:hover {
            background-color: #218838;
        }
        
        .delete-button {
            background-color: var(--error-color);
        }
        
        .delete-button:hover {
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
        
        .error {
            color: var(--error-color);
            background-color: rgba(239, 35, 60, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid var(--error-color);
        }
        
        .message {
            color: var(--edit-color);
            background-color: rgba(40, 167, 69, 0.1);
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            font-weight: 500;
            border-left: 4px solid var(--edit-color);
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
            margin-bottom: 1.5rem;
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
        
        .action-buttons {
            display: flex;
            gap: 0.5rem;
        }
        
        form {
            margin: 0;
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
            
            input, select, textarea {
                width: 100%;
                max-width: 100%;
            }
            
            .action-buttons {
                flex-direction: column;
            }
            
            button {
                width: 100%;
                margin-bottom: 0.5rem;
            }
        }
    </style>
</head>
<body>
<%@ include file="navbar.jsp" %>
    <h2>Gestion des Promotions</h2>
    
    <c:if test="${not empty error}">
        <p class="error" aria-live="polite">${error}</p>
    </c:if>
    <c:if test="${not empty message}">
        <p class="message" aria-live="polite">${message}</p>
    </c:if>

    <div class="form-container">
        <h3>${editPromotion != null ? 'Modifier une promotion' : 'Ajouter une promotion'}</h3>
        <form action="${pageContext.request.contextPath}/admin/promotions" method="post">
            <input type="hidden" name="action" value="${editPromotion != null ? 'update' : 'add'}">
            <c:if test="${editPromotion != null}">
                <input type="hidden" name="promotionId" value="${editPromotion.id}">
            </c:if>
            <div class="form-group">
                <label for="code">Code :</label>
                <input type="text" id="code" name="code" value="${editPromotion != null ? editPromotion.code : ''}" required>
            </div>
            <div class="form-group">
                <label for="description">Description :</label>
                <textarea id="description" name="description">${editPromotion != null ? editPromotion.description : ''}</textarea>
            </div>
            <div class="form-group">
                <label for="discountPercentage">Pourcentage de réduction (%) :</label>
                <input type="number" id="discountPercentage" name="discountPercentage" step="0.1" min="0" max="100" 
                       value="${editPromotion != null ? editPromotion.discountPercentage : ''}" required>
            </div>
            <div class="form-group">
                <label for="startDate">Date de début :</label>
                <input type="date" id="startDate" name="startDate" value="${editPromotion != null ? editPromotion.startDate : ''}" required>
            </div>
            <div class="form-group">
                <label for="endDate">Date de fin :</label>
                <input type="date" id="endDate" name="endDate" value="${editPromotion != null ? editPromotion.endDate : ''}" required>
            </div>
            <div class="form-group">
                <label for="trajetId">Trajet (optionnel) :</label>
                <select id="trajetId" name="trajetId">
                    <option value="">Aucun (promotion générale)</option>
                    <c:forEach var="trajet" items="${trajets}">
                        <option value="${trajet.id}" ${editPromotion != null && editPromotion.trajet != null && editPromotion.trajet.id == trajet.id ? 'selected' : ''}>
                            ${trajet.departStation.city} - ${trajet.arrivalStation.city}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="minLoyaltyPoints">Points de fidélité minimum :</label>
                <input type="number" id="minLoyaltyPoints" name="minLoyaltyPoints" min="0" 
                       value="${editPromotion != null && editPromotion.minLoyaltyPoints != null ? editPromotion.minLoyaltyPoints : ''}">
            </div>
            <div class="form-group">
                <button type="submit">${editPromotion != null ? 'Modifier' : 'Ajouter'}</button>
                <c:if test="${editPromotion != null}">
                    <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/admin/promotions'">Annuler</button>
                </c:if>
            </div>
        </form>
    </div>

    <h3>Promotions existantes</h3>
    <c:if test="${not empty promotions}">
        <table>
            <tr>
                <th>Code</th>
                <th>Description</th>
                <th>Réduction</th>
                <th>Dates</th>
                <th>Trajet</th>
                <th>Points min.</th>
                <th>Action</th>
            </tr>
            <c:forEach var="promotion" items="${promotions}">
                <tr>
                    <td>${promotion.code}</td>
                    <td>${promotion.description}</td>
                    <td>${promotion.discountPercentage}%</td>
                    <td>${promotion.startDate} - ${promotion.endDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${promotion.trajet != null}">
                                ${promotion.trajet.departStation.city} - ${promotion.trajet.arrivalStation.city}
                            </c:when>
                            <c:otherwise>Générale</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${promotion.minLoyaltyPoints != null ? promotion.minLoyaltyPoints : '-'}</td>
                    <td>
                        <div class="action-buttons">
                            <form action="${pageContext.request.contextPath}/admin/promotions" method="post">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="promotionId" value="${promotion.id}">
                                <button type="submit" class="edit-button">Modifier</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/admin/promotions" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="promotionId" value="${promotion.id}">
                                <button type="submit" class="delete-button" onclick="return confirm('Supprimer cette promotion ?');">Supprimer</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <a href="${pageContext.request.contextPath}/recherche" class="back-link">Retour à la recherche</a>
    </c:if>
</body>
</html>