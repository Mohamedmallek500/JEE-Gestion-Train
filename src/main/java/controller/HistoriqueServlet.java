package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Reservation;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import dao.ReservationDAO;
/**
 * Servlet implementation class HistoriqueServlet
 */
@WebServlet("/historique")
public class HistoriqueServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(HistoriqueServlet.class.getName());
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /historique");
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        List<Reservation> reservationsUtilisees = reservationDAO.findByUserAndEtat(user, "utilisé");

        request.setAttribute("reservations", reservationsUtilisees);
        request.getRequestDispatcher("/historique.jsp").forward(request, response);
    }
}