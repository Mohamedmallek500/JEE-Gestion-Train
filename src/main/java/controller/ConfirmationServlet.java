package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Reservation;
import model.User;
import model.Voyage;
import util.HibernateUtil;

import java.io.IOException;
import java.util.logging.Logger;

import dao.ReservationDAO;
import dao.UserDAO;
import dao.VoyageDAO;

@WebServlet("/confirmation")
public class ConfirmationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ConfirmationServlet.class.getName());
    private VoyageDAO voyageDAO;
    private ReservationDAO reservationDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        voyageDAO = new VoyageDAO(HibernateUtil.getSessionFactory());
        reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
        userDAO = new UserDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String voyageId = (String) session.getAttribute("voyageId");
        if (voyageId == null || voyageId.trim().isEmpty()) {
            LOGGER.warning("voyageId est null ou vide dans la session lors de l'accès à /confirmation");
            response.sendRedirect("recherche");
            return;
        }

        Voyage voyage = null;
        Reservation reservation = null;
        try {
            voyage = voyageDAO.findById(Long.parseLong(voyageId));
            if (voyage == null) {
                LOGGER.warning("Aucun voyage trouvé pour l'ID: " + voyageId);
                response.sendRedirect("recherche");
                return;
            }

            // Récupérer l'utilisateur connecté
            User user = (User) session.getAttribute("user");
            if (user == null) {
                LOGGER.warning("Aucun utilisateur connecté dans la session pour enregistrer la réservation");
                request.setAttribute("error", "Veuillez vous connecter pour réserver.");
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }

            String classe = (String) session.getAttribute("classe");
            String[] preferences = (String[]) session.getAttribute("preferences");
            String nextTrajetId = (String) session.getAttribute("nextTrajetId");

            // Convertir preferences en chaîne
            String preferencesStr = preferences != null ? String.join(", ", preferences) : "";

            // Enregistrer la réservation
            reservation = new Reservation(user, voyage, classe, preferencesStr);
            reservationDAO.save(reservation);

            // Mettre à jour les places disponibles dans le voyage
            if (voyage.getPlacesDisponibles() > 0) {
                voyage.setPlacesDisponibles(voyage.getPlacesDisponibles() - 1);
                voyageDAO.update(voyage);
            } else {
                LOGGER.warning("Plus de places disponibles pour le voyage ID: " + voyageId);
                request.setAttribute("error", "Plus de places disponibles pour ce voyage.");
                request.setAttribute("voyage", voyage);
                request.setAttribute("classe", classe);
                request.setAttribute("preferences", preferences);
                request.setAttribute("nextTrajetId", nextTrajetId);
                request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
                return;
            }

            // Incrémenter les points de fidélité de l'utilisateur
            int pointsEarned = 10; 
            user.setLoyaltyPoints(user.getLoyaltyPoints() + pointsEarned);
            userDAO.update(user);

            // Transmettre les données à la JSP
            request.setAttribute("voyage", voyage);
            request.setAttribute("classe", classe);
            request.setAttribute("preferences", preferences);
            request.setAttribute("nextTrajetId", nextTrajetId);
            request.setAttribute("reservationId", reservation.getId());
            request.setAttribute("success", "Réservation effectuée avec succès ! " + pointsEarned + " points de fidélité ajoutés.");
            request.setAttribute("etat", reservation.getEtat());
            request.setAttribute("loyaltyPoints", user.getLoyaltyPoints()); // Pour affichage

            request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de voyageId en Long: " + voyageId + " - " + e.getMessage());
            response.sendRedirect("recherche");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de l'enregistrement de la réservation: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de la réservation : " + e.getMessage());
            request.setAttribute("voyage", voyage);
            request.setAttribute("classe", session.getAttribute("classe"));
            request.setAttribute("preferences", session.getAttribute("preferences"));
            request.setAttribute("nextTrajetId", session.getAttribute("nextTrajetId"));
            request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
        }
    }
}