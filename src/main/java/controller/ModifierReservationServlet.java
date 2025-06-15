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
import java.util.logging.Logger;

import dao.ReservationDAO;

@WebServlet("/modifierReservation")
public class ModifierReservationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ModifierReservationServlet.class.getName());
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
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /modifierReservation");
            session = request.getSession(true); // Créer une session si nécessaire
            session.setAttribute("error", "Veuillez vous connecter pour modifier une réservation.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        LOGGER.info("Accès à doGet - ID: " + reservationId);
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de réservation manquant lors de l'accès à /modifierReservation");
            session.setAttribute("error", "ID de réservation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            LOGGER.info("Réservation récupérée: " + reservation);
            LOGGER.info("État de la réservation: " + (reservation.getEtat() != null ? reservation.getEtat() : "null"));
            if (!"acheté".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non modifiable (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les réservations achetées peuvent être modifiées.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            if (!reservation.getUser().equals(user) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autorisé à modifier la réservation ID: " + reservationId);
                session.setAttribute("error", "Vous n'êtes pas autorisé à modifier cette réservation.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            request.setAttribute("reservation", reservation);
            LOGGER.info("Forward vers /modifierReservation.jsp avec la réservation : " + reservation);
            request.getRequestDispatcher("/modifierReservation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'accès à la réservation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de l'accès à la réservation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de la modification de la réservation");
            session = request.getSession(true);
            session.setAttribute("error", "Veuillez vous connecter pour modifier une réservation.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de réservation manquant lors de la modification");
            session.setAttribute("error", "ID de réservation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            if (!"acheté".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non modifiable (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les réservations achetées peuvent être modifiées.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            if (!reservation.getUser().equals(user) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autorisé à modifier la réservation ID: " + reservationId);
                session.setAttribute("error", "Vous n'êtes pas autorisé à modifier cette réservation.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            String classe = request.getParameter("classe");
            String[] preferences = request.getParameterValues("preferences");

            reservation.setClasse(classe);
            reservation.setPreferences(preferences != null ? String.join(", ", preferences) : "");
            reservationDAO.update(reservation);

            session.setAttribute("success", "Réservation modifiée avec succès !");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la modification de la réservation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la modification de la réservation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }
}