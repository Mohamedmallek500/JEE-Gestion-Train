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

@WebServlet("/annulerReservation")
public class AnnulerReservationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AnnulerReservationServlet.class.getName());
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
            LOGGER.warning("Utilisateur non connecté lors de l'accès à /annulerReservation");
            response.sendRedirect("login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de réservation manquant lors de l'accès à /annulerReservation");
            session.setAttribute("error", "ID de réservation manquant.");
            response.sendRedirect("monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect("monCompte");
                return;
            }

            if (!"acheté".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non annulable (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Cette réservation ne peut pas être annulée (état: " + reservation.getEtat() + ").");
                response.sendRedirect("monCompte");
                return;
            }

            User sessionUser = (User) session.getAttribute("user");
            User reservationUser = reservation.getUser();
            if (sessionUser == null || reservationUser == null || !sessionUser.getId().equals(reservationUser.getId())) {
                LOGGER.warning("Utilisateur non autorisé à annuler la réservation ID: " + reservationId);
                session.setAttribute("error", "Vous n'êtes pas autorisé à annuler cette réservation.");
                response.sendRedirect("monCompte");
                return;
            }

            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/annulerReservation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect("monCompte");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connecté lors de l'annulation de la réservation");
            response.sendRedirect("login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de réservation manquant lors de la soumission à /annulerReservation");
            session.setAttribute("error", "ID de réservation manquant.");
            response.sendRedirect("monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("Réservation non trouvée pour l'ID: " + reservationId);
                session.setAttribute("error", "Réservation non trouvée.");
                response.sendRedirect("monCompte");
                return;
            }

            if (!"acheté".equals(reservation.getEtat())) {
                LOGGER.warning("Réservation non annulable (état: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Cette réservation ne peut pas être annulée (état: " + reservation.getEtat() + ").");
                response.sendRedirect("monCompte");
                return;
            }

            User sessionUser = (User) session.getAttribute("user");
            User reservationUser = reservation.getUser();
            if (sessionUser == null || reservationUser == null || !sessionUser.getId().equals(reservationUser.getId())) {
                LOGGER.warning("Utilisateur non autorisé à annuler la réservation ID: " + reservationId);
                session.setAttribute("error", "Vous n'êtes pas autorisé à annuler cette réservation.");
                response.sendRedirect("monCompte");
                return;
            }

            // Mettre à jour l'état de la réservation
            reservation.setEtat("en attente d'annulation");
            reservationDAO.update(reservation);
            LOGGER.info("Demande d'annulation enregistrée pour la réservation ID: " + reservationId);
            session.setAttribute("success", "Demande d'annulation envoyée. En attente de confirmation par l'administrateur.");
            response.sendRedirect("monCompte");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de réservation invalide.");
            response.sendRedirect("monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'annulation de la réservation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la demande d'annulation: " + e.getMessage());
            response.sendRedirect("monCompte");
        }
    }
}