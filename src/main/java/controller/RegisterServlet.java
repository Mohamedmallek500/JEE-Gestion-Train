package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private UserDAO userDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        // Validation des champs
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "Le nom d'utilisateur est requis.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Le mot de passe est requis.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "L'email est requis.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("error", "L'email n'est pas valide.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Vérifier si l'utilisateur existe déjà (par username)
        User existingUserByUsername = userDAO.findByUsername(username);
        if (existingUserByUsername != null) {
            request.setAttribute("error", "Nom d'utilisateur déjà pris !");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Vérifier si l'email existe déjà
        User existingUserByEmail = userDAO.findByEmail(email);
        if (existingUserByEmail != null) {
            request.setAttribute("error", "Cet email est déjà utilisé !");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            // Hacher le mot de passe
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Créer un nouvel utilisateur (le rôle sera automatiquement "user")
            User user = new User(username, hashedPassword, email);
            userDAO.save(user);

            // Ajouter un message de succès dans la session
            HttpSession session = request.getSession();
            session.setAttribute("success", "Inscription réussie ! Veuillez vous connecter.");
            
            // Rediriger vers la page de login après inscription réussie
            response.sendRedirect("login");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'inscription de l'utilisateur : " + e.getMessage());
            request.setAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}