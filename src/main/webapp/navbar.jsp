<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    nav {
        background-color: #4361ee; /* Couleur primaire comme dans la page recherche */
        padding: 12px 0;
        margin-bottom: 20px;
        width: 100vw;
        position: relative;
        left: 50%;
        right: 50%;
        margin-left: -50vw;
        margin-right: -50vw;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    nav ul {
        list-style-type: none;
        margin: 0 auto;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        max-width: 1200px;
        flex-wrap: wrap;
    }
    nav ul li {
        margin: 0 12px;
        position: relative;
    }
    nav ul li a {
        color: white;
        text-decoration: none;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        font-size: 15px;
        font-weight: 500;
        padding: 8px 12px;
        border-radius: 4px;
        transition: all 0.2s ease;
    }
    nav ul li a:hover {
        background-color: rgba(255, 255, 255, 0.2);
        color: white;
        transform: translateY(-1px);
    }
    nav ul li img {
        height: 32px;
        vertical-align: middle;
        transition: transform 0.2s ease;
    }
    nav ul li:first-child a:hover img {
        transform: scale(1.05);
    }
    /* Style pour le message de bienvenue */
    nav ul li.welcome-message {
        color: white;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        font-size: 14px;
        font-style: italic;
        margin-right: 20px;
        background-color: rgba(255, 255, 255, 0.1);
        padding: 6px 12px;
        border-radius: 15px;
    }
    /* Style pour les liens admin */
    nav ul li.admin-link a {
        background-color: rgba(76, 201, 240, 0.2); /* Couleur success de la page recherche */
    }
    nav ul li.admin-link a:hover {
        background-color: rgba(76, 201, 240, 0.3);
    }
</style>

<nav>
    <ul>
        <!-- Log
        <li>
            <a href="${pageContext.request.contextPath}/recherche">
                <img src="${pageContext.request.contextPath}/images/logo.webp" alt="Logo" />
            </a>
        </li>o -->

        <!-- Message de bienvenue -->
        <c:if test="${not empty sessionScope.user}">
            <li class="welcome-message">
                Bienvenue, ${sessionScope.user.username} !
            </li>
        </c:if>

        <!-- Lien principal -->
        <li><a href="${pageContext.request.contextPath}/recherche">Rechercher un trajet</a></li>

        <!-- Liens utilisateur -->
        <c:if test="${not empty sessionScope.user}">
            <li><a href="${pageContext.request.contextPath}/monCompte">Mon compte</a></li>
            
            <!-- Liens admin -->
            <c:if test="${sessionScope.user.role == 'admin'}">
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/users">Gérer les utilisateurs</a></li>
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/stations">Gérer les stations</a></li>
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/trajets">Gérer les trajets</a></li>
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/voyages">Gérer les voyages</a></li>
                <li class="admin-link"><a href="${pageContext.request.contextPath}/admin/promotions">Gérer les promotions</a></li>
            </c:if>

            <li><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></li>
        </c:if>

        <!-- Lien connexion -->
        <c:if test="${empty sessionScope.user}">
            <li><a href="${pageContext.request.contextPath}/login">Se connecter</a></li>
        </c:if>
    </ul>
</nav>