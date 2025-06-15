<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Revenus générés</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --primary-light: #5a75f1;
            --secondary-color: #3f37c9;
            --success-color: #28a745;
            --light-gray: #f8f9fa;
            --card-bg: #ffffff;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --transition: all 0.3s ease;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-gray);
            color: #2b2d42;
        }
        
        .revenue-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: var(--card-bg);
            border-radius: 12px;
            box-shadow: var(--shadow);
        }
        
        h1 {
            color: var(--primary-color);
            margin-bottom: 2rem;
            font-weight: 700;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--primary-color);
        }
        
        .form-container {
            background-color: rgba(67, 97, 238, 0.05);
            padding: 1.5rem;
            border-radius: 12px;
            margin-bottom: 2rem;
            border: 1px solid rgba(67, 97, 238, 0.1);
        }
        
        .date-input-group {
            margin-bottom: 1.5rem;
            position: relative;
        }
        
        .date-label {
            display: block;
            font-weight: 600;
            color: var(--primary-color);
            margin-bottom: 0.5rem;
            font-size: 0.95rem;
        }
        
        .date-input {
            width: 100%;
            padding: 1rem;
            border: 2px solid #e0e7ff;
            border-radius: 8px;
            font-size: 1rem;
            transition: var(--transition);
            background-color: white;
            color: #2b2d42;
        }
        
        .date-input:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
            outline: none;
        }
        
        .btn {
            padding: 0.9rem 1.8rem;
            font-weight: 600;
            border-radius: 8px;
            transition: var(--transition);
            font-size: 1rem;
            border: none;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            color: white;
            box-shadow: 0 4px 6px rgba(67, 97, 238, 0.2);
        }
        
        .btn-primary:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(67, 97, 238, 0.25);
        }
        
        .btn-primary:active {
            transform: translateY(0);
        }
        
        .btn-secondary {
            background-color: white;
            color: var(--primary-color);
            border: 2px solid var(--primary-color);
        }
        
        .btn-secondary:hover {
            background-color: rgba(67, 97, 238, 0.05);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        
        .btn-icon {
            margin-right: 8px;
            font-size: 1.1rem;
        }
        
        .results-container {
            background-color: rgba(40, 167, 69, 0.05);
            padding: 1.8rem;
            border-radius: 12px;
            border-left: 4px solid var(--success-color);
            margin: 2.5rem 0;
        }
        
        .revenue-amount {
            font-size: 1.8rem;
            font-weight: 700;
            color: var(--success-color);
            margin-top: 0.5rem;
        }
        
        .period-info {
            color: #666;
            font-size: 0.95rem;
            margin-bottom: 1rem;
            font-weight: 500;
        }
        
        .action-buttons {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }
        
        @media (max-width: 768px) {
            .revenue-container {
                padding: 1.5rem;
                margin: 1rem;
            }
            
            .action-buttons {
                flex-direction: column;
            }
            
            .btn {
                width: 100%;
            }
        }
    </style>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <div class="revenue-container">
        <h1>Revenus générés</h1>
        
        <div class="form-container">
            <form method="post" action="${pageContext.request.contextPath}/admin/revenue">
                <div class="date-input-group">
                    <label for="startDate" class="date-label">Date de début</label>
                    <input type="datetime-local" class="date-input" id="startDate" name="startDate" required>
                </div>
                <div class="date-input-group">
                    <label for="endDate" class="date-label">Date de fin</label>
                    <input type="datetime-local" class="date-input" id="endDate" name="endDate" required>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-calculator btn-icon"></i> Calculer les revenus
                </button>
            </form>
        </div>
        
        <c:if test="${not empty totalRevenue}">
            <div class="results-container">
                <p class="period-info">Période analysée : ${startDate} à ${endDate}</p>
                <p><strong>Revenus totaux :</strong></p>
                <p class="revenue-amount"><fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/> €</p>
            </div>
        </c:if>
        
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">
                <i class="bi bi-arrow-left btn-icon"></i> Retour au tableau de bord
            </a>
        </div>
    </div>
</body>
</html>