package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Trajet;
import model.Voyage;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import dao.TrajetDAO;
import dao.VoyageDAO;

@WebServlet("/selection")
public class SelectionTrajetServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SelectionTrajetServlet.class.getName());
    private VoyageDAO voyageDAO;
    private TrajetDAO trajetDAO;

    @Override
    public void init() throws ServletException {
        voyageDAO = new VoyageDAO(HibernateUtil.getSessionFactory());
        trajetDAO = new TrajetDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String voyageId = request.getParameter("voyageId");
        if (voyageId == null || voyageId.trim().isEmpty()) {
            LOGGER.warning("voyageId est null ou vide lors de l'accès à /selection");
            response.sendRedirect("recherche");
            return;
        }

        try {
            Voyage voyage = voyageDAO.findById(Long.parseLong(voyageId));
            if (voyage == null) {
                LOGGER.warning("Aucun voyage trouvé pour l'ID: " + voyageId);
                response.sendRedirect("recherche");
                return;
            }

            // Stocker le voyageId dans la session
            HttpSession session = request.getSession();
            session.setAttribute("voyageId", voyageId);

            // Trouver les trajets possibles pour une continuation (à partir de la ville d'arrivée)
            String arrivalCity = voyage.getTrajet().getArrivalStation().getCity();
            List<Trajet> nextTrajets = trajetDAO.findAll().stream()
                    .filter(t -> t.getDepartStation().getCity().equalsIgnoreCase(arrivalCity))
                    .collect(Collectors.toList());

            request.setAttribute("voyage", voyage);
            request.setAttribute("nextTrajets", nextTrajets);
            request.getRequestDispatcher("/selection.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion de voyageId en Long: " + voyageId + " - " + e.getMessage());
            response.sendRedirect("recherche");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String voyageId = (String) session.getAttribute("voyageId");
        if (voyageId == null || voyageId.trim().isEmpty()) {
            LOGGER.warning("voyageId est null ou vide dans la session lors de la soumission à /selection");
            response.sendRedirect("recherche");
            return;
        }

        // Valider que voyageId est un nombre
        try {
            Long.parseLong(voyageId);
        } catch (NumberFormatException e) {
            LOGGER.severe("voyageId invalide dans la session: " + voyageId + " - " + e.getMessage());
            response.sendRedirect("recherche");
            return;
        }

        // Récupérer les options sélectionnées
        String classe = request.getParameter("classe");
        String[] preferences = request.getParameterValues("preferences");
        String nextTrajetId = request.getParameter("nextTrajetId");

        // Stocker les choix dans la session
        session.setAttribute("classe", classe);
        session.setAttribute("preferences", preferences);
        if (nextTrajetId != null && !nextTrajetId.isEmpty()) {
            session.setAttribute("nextTrajetId", nextTrajetId);
        } else {
            session.removeAttribute("nextTrajetId");
        }

        // Rediriger vers la page de confirmation
        response.sendRedirect("confirmation");
    }
}