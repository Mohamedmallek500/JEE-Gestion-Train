package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.HibernateUtil;
import dao.ReservationDAO;
import model.Reservation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servlet implementation class MonCompteServlet
 */
@WebServlet("/monCompte")
public class MonCompteServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MonCompteServlet.class.getName());
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
            LOGGER.warning("Utilisateur non connect� lors de l'acc�s � /monCompte");
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Update reservation statuses to "utilis�" if departure time has passed
        try {
            List<Reservation> reservationsToUpdate = reservationDAO.findPurchasedReservationsBeforeDate(LocalDateTime.now());
            LOGGER.info("Found " + reservationsToUpdate.size() + " reservations to update to 'utilis�'");
            for (Reservation reservation : reservationsToUpdate) {
                reservation.setEtat("utilis�");
                reservationDAO.update(reservation);
                LOGGER.info("Updated reservation ID " + reservation.getId() + " to 'utilis�'");
            }
        } catch (Exception e) {
            LOGGER.severe("Error updating reservation statuses: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de la mise � jour des statuts des r�servations");
        }

        // Load reservations only for non-admin users
        if (!"admin".equals(user.getRole())) {
            request.setAttribute("reservations", reservationDAO.findByUser(user));
        }

        // Load additional data for admin users
        if ("admin".equals(user.getRole())) {
            request.setAttribute("allPurchasedReservations", reservationDAO.findAllByEtat("achet�"));
            request.setAttribute("allUsedReservations", reservationDAO.findAllUsedReservations());
        }

        request.getRequestDispatcher("/monCompte.jsp").forward(request, response);
    }
}