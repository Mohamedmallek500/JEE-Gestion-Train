<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord administrateur</title>
    <style>
        :root {
            --primary-color: #4361ee;
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
        
        .admin-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background-color: var(--card-bg);
            border-radius: 8px;
            box-shadow: var(--shadow);
        }
        
        h1 {
            color: var(--primary-color);
            margin-bottom: 2rem;
            font-weight: 600;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--primary-color);
        }
        
        .dashboard-menu {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-top: 2rem;
        }
        
        .dashboard-card {
            background-color: white;
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: var(--shadow);
            transition: var(--transition);
            border-left: 4px solid var(--primary-color);
            text-decoration: none;
            color: inherit;
        }
        
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
            border-left-color: var(--secondary-color);
        }
        
        .dashboard-card h3 {
            color: var(--primary-color);
            margin-bottom: 0.5rem;
            font-weight: 600;
        }
        
        .dashboard-card p {
            color: #666;
            margin-bottom: 0;
        }
        
        .dashboard-card .icon {
            font-size: 2rem;
            color: var(--primary-color);
            margin-bottom: 1rem;
        }
        
        @media (max-width: 768px) {
            .admin-container {
                padding: 1.5rem;
                margin: 1rem;
            }
            
            .dashboard-menu {
                grid-template-columns: 1fr;
            }
        }
    </style>
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <%@ include file="navbar.jsp" %>
    
    <div class="admin-container">
        <h1>Tableau de bord administrateur</h1>
        
        <div class="dashboard-menu">
            <a href="${pageContext.request.contextPath}/admin/popular-trajets" class="dashboard-card">
                <div class="icon"><i class="bi bi-graph-up"></i></div>
                <h3>Trajets populaires</h3>
                <p>Consulter les trajets les plus demandés</p>
            </a>
            
            <a href="${pageContext.request.contextPath}/admin/revenue" class="dashboard-card">
                <div class="icon"><i class="bi bi-currency-euro"></i></div>
                <h3>Revenus</h3>
                <p>Analyse des revenus générés</p>
            </a>
            
            <a href="${pageContext.request.contextPath}/admin/reservation-evolution" class="dashboard-card">
                <div class="icon"><i class="bi bi-bar-chart-line"></i></div>
                <h3>Évolution</h3>
                <p>Tendance des réservations</p>
            </a>
        </div>
    </div>

    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>