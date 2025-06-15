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
            LOGGER.warning("Utilisateur non connect� lors de l'acc�s � /admin/annuler");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            LOGGER.warning("Utilisateur non autoris� � acc�der � /admin/annuler - Role: " + user.getRole());
            session.setAttribute("error", "Vous n'�tes pas autoris� � acc�der � cette page.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            List<Reservation> reservationsEnAttente = reservationDAO.findByEtat("en attente d'annulation");
            request.setAttribute("reservations", reservationsEnAttente);
            request.getRequestDispatcher("/adminAnnuler.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la r�cup�ration des r�servations: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la r�cup�ration des r�servations: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connect� lors de la soumission � /admin/annuler");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            LOGGER.warning("Utilisateur non autoris� � soumettre � /admin/annuler - Role: " + user.getRole());
            session.setAttribute("error", "Vous n'�tes pas autoris� � effectuer cette action.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        String reservationId = request.getParameter("id");
        String action = request.getParameter("action");

        if (reservationId == null || reservationId.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            LOGGER.warning("Param�tres manquants lors de la soumission � /admin/annuler - ID: " + reservationId + ", Action: " + action);
            session.setAttribute("error", "Param�tres manquants.");
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("R�servation non trouv�e pour l'ID: " + reservationId);
                session.setAttribute("error", "R�servation non trouv�e.");
                response.sendRedirect(request.getContextPath() + "/admin/annuler");
                return;
            }

            if (!"en attente d'annulation".equals(reservation.getEtat())) {
                LOGGER.warning("R�servation non en attente d'annulation (�tat: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Cette r�servation n'est pas en attente d'annulation.");
                response.sendRedirect(request.getContextPath() + "/admin/annuler");
                return;
            }

            if ("confirmer".equals(action)) {
                reservation.setEtat("annul�");
                reservationDAO.update(reservation);
                LOGGER.info("Annulation confirm�e pour la r�servation ID: " + reservationId);
                session.setAttribute("success", "Annulation confirm�e avec succ�s.");
            } else if ("rejeter".equals(action)) {
                reservation.setEtat("achet�");
                reservationDAO.update(reservation);
                LOGGER.info("Annulation rejet�e pour la r�servation ID: " + reservationId);
                session.setAttribute("success", "Annulation rejet�e. La r�servation est de nouveau active.");
            } else {
                LOGGER.warning("Action invalide: " + action);
                session.setAttribute("error", "Action invalide.");
            }

            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de r�servation invalide.");
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la gestion de l'annulation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la gestion de l'annulation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/annuler");
        }
    }
}