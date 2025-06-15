<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Évolution des réservations - Tableau de bord</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --danger-color: #f72585;
            --light-bg: #f8f9fa;
            --dark-bg: #212529;
            --border-radius: 8px;
            --box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-bg);
            color: #333;
            line-height: 1.6;
        }
        
        .dashboard-container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: var(--border-radius);
            box-shadow: var(--box-shadow);
        }
        
        .page-header {
            color: var(--primary-color);
            margin-bottom: 30px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .filter-card {
            background: white;
            border-radius: var(--border-radius);
            padding: 20px;
            margin-bottom: 30px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        
        .form-label {
            font-weight: 500;
            color: #555;
        }
        
        .form-control, .form-select {
            padding: 10px 15px;
            border-radius: var(--border-radius);
            border: 1px solid #ced4da;
            transition: all 0.3s;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.25rem rgba(67, 97, 238, 0.25);
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            border: none;
            padding: 10px 20px;
            border-radius: var(--border-radius);
            font-weight: 500;
            transition: all 0.3s;
        }
        
        .btn-primary:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
        }
        
        .chart-container {
            position: relative;
            height: 400px;
            margin: 30px 0;
        }
        
        .data-summary {
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }
        
        .summary-card {
            flex: 1;
            min-width: 200px;
            background: white;
            border-radius: var(--border-radius);
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            border-left: 4px solid var(--primary-color);
        }
        
        .summary-card h3 {
            font-size: 1.1rem;
            color: #666;
            margin-bottom: 10px;
        }
        
        .summary-card p {
            font-size: 1.8rem;
            font-weight: 600;
            color: var(--primary-color);
            margin: 0;
        }
        
        .alert {
            border-radius: var(--border-radius);
        }
        
        @media (max-width: 768px) {
            .dashboard-container {
                padding: 20px;
                margin: 15px;
            }
            
            .page-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }
            
            .data-summary {
                flex-direction: column;
                gap: 15px;
            }
            
            .summary-card {
                min-width: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <div class="page-header">
            <h1><i class="bi bi-graph-up"></i> Évolution des réservations</h1>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary">
                <i class="bi bi-arrow-left"></i> Retour au tableau de bord
            </a>
        </div>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        
        <div class="filter-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/reservation-evolution">
                <div class="row">
                    <div class="col-md-5">
                        <label for="startDate" class="form-label">Date de début</label>
                        <input type="datetime-local" class="form-control" id="startDate" name="startDate" value="${startDate}" required>
                    </div>
                    <div class="col-md-5">
                        <label for="endDate" class="form-label">Date de fin</label>
                        <input type="datetime-local" class="form-control" id="endDate" name="endDate" value="${endDate}" required>
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-funnel"></i> Filtrer
                        </button>
                    </div>
                </div>
                <input type="hidden" name="groupBy" value="month">
            </form>
        </div>
        
        <c:if test="${not empty evolutionData}">
            <div class="data-summary">
                <div class="summary-card">
                    <h3>Total réservations</h3>
                    <p>${totalReservations}</p>
                </div>
                <div class="summary-card">
                    <h3>Période analysée</h3>
                    <p>${periodLength} jours</p>
                </div>
                <div class="summary-card">
                    <h3>Moyenne quotidienne</h3>
                    <p>${averagePerDay}</p>
                </div>
            </div>
            
            <div class="chart-container">
                <canvas id="evolutionChart"></canvas>
            </div>
            
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    try {
                        const ctx = document.getElementById('evolutionChart').getContext('2d');
                        const labels = ${labels};
                        const data = ${data};
                        
                        new Chart(ctx, {
                            type: 'line',
                            data: {
                                labels: labels,
                                datasets: [{
                                    label: 'Nombre de réservations',
                                    data: data,
                                    borderColor: '#4361ee',
                                    backgroundColor: 'rgba(67, 97, 238, 0.1)',
                                    borderWidth: 3,
                                    pointBackgroundColor: '#4361ee',
                                    pointRadius: 5,
                                    pointHoverRadius: 7,
                                    fill: true,
                                    tension: 0.3
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: {
                                        position: 'top',
                                    },
                                    tooltip: {
                                        callbacks: {
                                            label: function(context) {
                                                return `${context.raw} réservations`;
                                            }
                                        }
                                    },
                                    title: {
                                        display: true,
                                        text: 'Évolution des réservations par mois',
                                        font: {
                                            size: 18,
                                            weight: 'bold'
                                        },
                                        padding: {
                                            top: 10,
                                            bottom: 20
                                        }
                                    }
                                },
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        suggestedMax: Math.max(...data) + 2,
                                        ticks: {
                                            stepSize: 1,
                                            precision: 0
                                        },
                                        title: {
                                            display: true,
                                            text: 'Nombre de réservations',
                                            font: {
                                                weight: 'bold'
                                            }
                                        },
                                        grid: {
                                            color: 'rgba(0, 0, 0, 0.05)'
                                        }
                                    },
                                    x: {
                                        title: {
                                            display: true,
                                            text: 'Période',
                                            font: {
                                                weight: 'bold'
                                            }
                                        },
                                        grid: {
                                            display: false
                                        }
                                    }
                                }
                            }
                        });
                    } catch (e) {
                        console.error('Erreur lors de la création du graphique:', e);
                        document.getElementById('evolutionChart').innerHTML = 
                            '<div class="alert alert-danger">Erreur lors de l\'affichage du graphique</div>';
                    }
                });
            </script>
        </c:if>
        
        <c:if test="${empty evolutionData}">
            <div class="alert alert-info">
                <i class="bi bi-info-circle"></i> Aucune donnée à afficher pour la période sélectionnée.
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>