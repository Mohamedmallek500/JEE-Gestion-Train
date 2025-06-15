package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/admin/users")
public class AdminUserManagementServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminUserManagementServlet.class.getName());
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /admin/users");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equals(currentUser.getRole())) {
            LOGGER.warning("Utilisateur non autorisé à accéder à /admin/users");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        // Lister tous les utilisateurs
        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/adminUsers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de l'action sur /admin/users");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (!"admin".equals(currentUser.getRole())) {
            LOGGER.warning("Utilisateur non autorisé à effectuer une action sur /admin/users");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        String userId = request.getParameter("id");
        String action = request.getParameter("action");

        if (userId == null || action == null) {
            session.setAttribute("error", "Paramètres manquants pour l'action.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        try {
            User user = userDAO.findById(Long.parseLong(userId));
            if (user == null) {
                session.setAttribute("error", "Utilisateur non trouvé.");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            // Vérifier si l'admin essaie de se bloquer lui-même
            if (user.getId().equals(currentUser.getId()) && "block".equals(action)) {
                session.setAttribute("error", "Vous ne pouvez pas bloquer votre propre compte.");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            if ("block".equals(action)) {
                user.setBlocked(true);
                session.setAttribute("success", "Utilisateur bloqué avec succès.");
            } else if ("unblock".equals(action)) {
                user.setBlocked(false);
                session.setAttribute("success", "Utilisateur débloqué avec succès.");
            }

            userDAO.update(user);
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de userId: " + userId + " - " + e.getMessage());
            session.setAttribute("error", "ID utilisateur invalide.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'action sur l'utilisateur: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de l'action sur l'utilisateur.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}