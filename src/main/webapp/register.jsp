<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscription</title>
    <style>
        :root {
            --primary-color: #4361ee;
            --secondary-color: #3f37c9;
            --success-color: #4cc9f0;
            --error-color: #ef233c;
            --text-color: #2b2d42;
            --light-gray: #f8f9fa;
            --white: #ffffff;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --transition: all 0.3s ease;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--light-gray);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            color: var(--text-color);
            line-height: 1.6;
        }
        
        .register-container {
            background-color: var(--white);
            padding: 2.5rem;
            border-radius: 10px;
            box-shadow: var(--shadow);
            width: 100%;
            max-width: 450px;
            transition: var(--transition);
        }
        
        .register-container:hover {
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
        }
        
        h2 {
            color: var(--primary-color);
            text-align: center;
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
        }
        
        input {
            width: 100%;
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
            transition: var(--transition);
            box-sizing: border-box;
        }
        
        input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
        }
        
        input:invalid:not(:placeholder-shown) {
            border-color: var(--error-color);
        }
        
        input:valid:not(:placeholder-shown) {
            border-color: #ccc;
        }
        
        button {
            width: 100%;
            padding: 0.8rem;
            background-color: var(--success-color);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: var(--transition);
            margin-top: 0.5rem;
        }
        
        button:hover {
            background-color: var(--primary-color);
            transform: translateY(-2px);
        }
        
        .error {
            color: var(--error-color);
            background-color: rgba(239, 35, 60, 0.1);
            padding: 0.8rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            text-align: center;
            font-weight: 500;
        }
        
        p {
            text-align: center;
            margin-top: 1.5rem;
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
        
        /* Style pour les indications de validation */
        input:required + .hint:before {
            content: "* Requis";
            color: var(--error-color);
            font-size: 0.8rem;
            display: block;
            margin-top: 0.3rem;
        }
        
        input[type="email"]:invalid:not(:placeholder-shown) + .hint:before {
            content: "Veuillez entrer un email valide";
        }
    </style>
</head>
<body>
    <div class="register-container">
        <h2>Inscription</h2>
        <c:if test="${not empty error}">
            <p class="error" aria-live="polite">${error}</p>
        </c:if>
        <form action="register" method="post">
            <div class="form-group">
                <label for="username">Nom d'utilisateur :</label>
                <input type="text" id="username" name="username" required aria-describedby="username-error" placeholder=" ">
            </div>
            <div class="form-group">
                <label for="password">Mot de passe :</label>
                <input type="password" id="password" name="password" required aria-describedby="password-error" placeholder=" ">
            </div>
            <div class="form-group">
                <label for="email">Email :</label>
                <input type="email" id="email" name="email" required 
                       pattern="^[A-Za-z0-9+_.-]+@(.+)$" 
                       title="Veuillez entrer un email valide (exemple@domaine.com)" 
                       aria-describedby="email-error" placeholder="exemple@domaine.com">
            </div>
            <button type="submit">S'inscrire</button>
        </form>
        <p><a href="login">Déjà inscrit ? Connectez-vous</a></p>
    </div>
</body>
</html>