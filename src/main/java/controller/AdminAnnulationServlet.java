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

@WebServlet("/admin/annuler")
public class AdminAnnulationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminAnnulationServlet.class.getName());
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
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /admin/annuler");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            LOGGER.warning("Utilisateur non autorisé à accéder à /admin/annuler - Role: " + user.getRole());
            session.setAttribute("error", "Vous n'êtes pas autorisé à accéder à cette page.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            List<Reservation> reservationsEnAttente = reservationDAO.findByEtat("en attente d'annulation");
            request.setAttribute("reservations", reservationsEnAttente);
            request.getRequestDispatcher("/adminAnnuler.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération des réservations: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la récupération des réservations: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de la soumission à /admin/annuler");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            LOGGER.warning("Utilisateur non autorisé à soumettre à /admin/annuler - Role: " + user.getRole());
            session.setAttribute("error", "Vous n'êtes pas autorisé à effectuer cette action.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        String reservationId = request.getParameter("id");
        String action = request.getParameter("action");

        if (reservationId == null || reservationId.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            LOGGER.warning("Paramètres manquants lors de la soumission à /admin/annuler - ID: " + reservationId + ", Action: " + action);
            session.setAttribute("error", "Paramètres manquants.");
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect(request.getContextPath() + "/admin/annuler");
                return;
            }

            if (!"en attente d'annulation".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non en attente d'annulation (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Cette réservation n'est pas en attente d'annulation.");
                response.sendRedirect(request.getContextPath() + "/admin/annuler");
                return;
            }

            if ("confirmer".equals(action)) {
                reservation.setEtat("annulé");
                reservationDAO.update(reservation);
                LOGGER.info("Annulation confirmée pour la réservation ID: " + reservationId);
                session.setAttribute("success", "Annulation confirmée avec succès.");
            } else if ("rejeter".equals(action)) {
                reservation.setEtat("acheté");
                reservationDAO.update(reservation);
                LOGGER.info("Annulation rejetée pour la réservation ID: " + reservationId);
                session.setAttribute("success", "Annulation rejetée. La réservation est de nouveau active.");
            } else {
                LOGGER.warning("Action invalide: " + action);
                session.setAttribute("error", "Action invalide.");
            }

            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la gestion de l'annulation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la gestion de l'annulation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        }
    }
}