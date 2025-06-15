<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier Réservation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --light-bg: #f8f9fa;
            --border-radius: 8px;
            --box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-bg);
            color: #333;
            line-height: 1.6;
        }
        
        .container {
            max-width: 800px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: var(--border-radius);
            box-shadow: var(--box-shadow);
        }
        
        h2 {
            color: var(--primary-color);
            margin-bottom: 25px;
            font-weight: 600;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }
        
        .form-label {
            font-weight: 500;
            margin-bottom: 8px;
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
        
        .form-check {
            margin-bottom: 10px;
        }
        
        .form-check-input {
            width: 20px;
            height: 20px;
            margin-top: 0.2em;
            border: 2px solid #ced4da;
        }
        
        .form-check-input:checked {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
        }
        
        .form-check-label {
            margin-left: 8px;
            cursor: pointer;
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            border: none;
            padding: 10px 20px;
            border-radius: var(--border-radius);
            font-weight: 500;
            letter-spacing: 0.5px;
            transition: all 0.3s;
        }
        
        .btn-primary:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
        }
        
        .alert-success {
            background-color: #d1fae5;
            color: #065f46;
            border: none;
            border-radius: var(--border-radius);
            padding: 15px;
            margin-bottom: 25px;
        }
        
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: var(--primary-color);
            text-decoration: none;
            transition: all 0.3s;
        }
        
        .back-link:hover {
            color: var(--secondary-color);
            text-decoration: underline;
            transform: translateX(-3px);
        }
        
        @media (max-width: 768px) {
            .container {
                padding: 20px;
                margin: 20px;
            }
            
            h2 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Modifier votre réservation</h2>
        
        <c:if test="${not empty success}">
            <div class="alert alert-success" role="alert">
                <i class="bi bi-check-circle-fill"></i> ${success}
            </div>
        </c:if>
        
        <c:if test="${not empty reservation}">
            <form action="modifierReservation" method="post">
                <input type="hidden" name="id" value="${reservation.id}">
                
                <div class="mb-4">
                    <label for="classe" class="form-label">Classe :</label>
                    <select class="form-select" id="classe" name="classe" required>
                        <option value="Économique" ${reservation.classe == 'Économique' ? 'selected' : ''}>Économique</option>
                        <option value="2ème" ${reservation.classe == '2ème' ? 'selected' : ''}>2ème classe</option>
                        <option value="1ère" ${reservation.classe == '1ère' ? 'selected' : ''}>1ère classe</option>
                    </select>
                </div>
                
                <div class="mb-4">
                    <label class="form-label">Préférences :</label>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="preferences" value="Fenêtre" id="fenetre" ${reservation.preferences.contains('Fenêtre') ? 'checked' : ''}>
                        <label class="form-check-label" for="fenetre">Fenêtre</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="preferences" value="Espace famille" id="famille" ${reservation.preferences.contains('Espace famille') ? 'checked' : ''}>
                        <label class="form-check-label" for="famille">Espace famille</label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="preferences" value="Non-fumeur" id="nonfumeur" ${reservation.preferences.contains('Non-fumeur') ? 'checked' : ''}>
                        <label class="form-check-label" for="nonfumeur">Non-fumeur</label>
                    </div>
                </div>
                
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-save"></i> Enregistrer les modifications
                    </button>
                </div>
            </form>
        </c:if>
        
        <a href="monCompte" class="back-link">
            <i class="bi bi-arrow-left"></i> Retour à mon compte
        </a>
    </div>

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <!-- Bootstrap JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>