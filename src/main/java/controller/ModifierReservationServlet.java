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
            LOGGER.warning("Utilisateur non connect� lors de l'acc�s � /modifierReservation");
            session = request.getSession(true); // Cr�er une session si n�cessaire
            session.setAttribute("error", "Veuillez vous connecter pour modifier une r�servation.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        LOGGER.info("Acc�s � doGet - ID: " + reservationId);
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de r�servation manquant lors de l'acc�s � /modifierReservation");
            session.setAttribute("error", "ID de r�servation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("R�servation non trouv�e pour l'ID: " + reservationId);
                session.setAttribute("error", "R�servation non trouv�e.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            LOGGER.info("R�servation r�cup�r�e: " + reservation);
            LOGGER.info("�tat de la r�servation: " + (reservation.getEtat() != null ? reservation.getEtat() : "null"));
            if (!"achet�".equals(reservation.getEtat())) {
                LOGGER.warning("R�servation non modifiable (�tat: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les r�servations achet�es peuvent �tre modifi�es.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            if (!reservation.getUser().equals(user) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autoris� � modifier la r�servation ID: " + reservationId);
                session.setAttribute("error", "Vous n'�tes pas autoris� � modifier cette r�servation.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            request.setAttribute("reservation", reservation);
            LOGGER.info("Forward vers /modifierReservation.jsp avec la r�servation : " + reservation);
            request.getRequestDispatcher("/modifierReservation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de r�servation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'acc�s � la r�servation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de l'acc�s � la r�servation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            LOGGER.warning("Utilisateur non connect� lors de la modification de la r�servation");
            session = request.getSession(true);
            session.setAttribute("error", "Veuillez vous connecter pour modifier une r�servation.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String reservationId = request.getParameter("id");
        if (reservationId == null || reservationId.trim().isEmpty()) {
            LOGGER.warning("ID de r�servation manquant lors de la modification");
            session.setAttribute("error", "ID de r�servation manquant.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
            return;
        }

        try {
            Reservation reservation = reservationDAO.findById(Long.parseLong(reservationId));
            if (reservation == null) {
                LOGGER.warning("R�servation non trouv�e pour l'ID: " + reservationId);
                session.setAttribute("error", "R�servation non trouv�e.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            if (!"achet�".equals(reservation.getEtat())) {
                LOGGER.warning("R�servation non modifiable (�tat: " + reservation.getEtat() + ") pour l'ID: " + reservationId);
                session.setAttribute("error", "Seules les r�servations achet�es peuvent �tre modifi�es.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            User user = (User) session.getAttribute("user");
            if (!reservation.getUser().equals(user) && !"admin".equals(user.getRole())) {
                LOGGER.warning("Utilisateur non autoris� � modifier la r�servation ID: " + reservationId);
                session.setAttribute("error", "Vous n'�tes pas autoris� � modifier cette r�servation.");
                response.sendRedirect(request.getContextPath() + "/monCompte");
                return;
            }

            String classe = request.getParameter("classe");
            String[] preferences = request.getParameterValues("preferences");

            reservation.setClasse(classe);
            reservation.setPreferences(preferences != null ? String.join(", ", preferences) : "");
            reservationDAO.update(reservation);

            session.setAttribute("success", "R�servation modifi�e avec succ�s !");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de reservationId en Long: " + reservationId + " - " + e.getMessage());
            session.setAttribute("error", "ID de r�servation invalide.");
            response.sendRedirect(request.getContextPath() + "/monCompte");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la modification de la r�servation: " + e.getMessage());
            session.setAttribute("error", "Erreur lors de la modification de la r�servation: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/monCompte");
        }
    }
}