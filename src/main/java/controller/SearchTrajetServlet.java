package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Promotion;
import model.Station;
import model.Trajet;
import model.User;
import model.Voyage;
import util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import dao.PromotionDAO;
import dao.StationDAO;
import dao.TrajetDAO;
import dao.VoyageDAO;

@WebServlet("/recherche")
public class SearchTrajetServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SearchTrajetServlet.class.getName());
    private TrajetDAO trajetDAO;
    private VoyageDAO voyageDAO;
    private StationDAO stationDAO;
    private PromotionDAO promotionDAO;

    @Override
    public void init() throws ServletException {
        trajetDAO = new TrajetDAO(HibernateUtil.getSessionFactory());
        voyageDAO = new VoyageDAO(HibernateUtil.getSessionFactory());
        stationDAO = new StationDAO(HibernateUtil.getSessionFactory());
        promotionDAO = new PromotionDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Station> stations = stationDAO.findAll();
        List<String> cities = stations.stream()
                .map(Station::getCity)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        request.setAttribute("cities", cities);

        String villeDepart = request.getParameter("villeDepart");
        String villeDestination = request.getParameter("villeDestination");
        String date = request.getParameter("date");
        request.setAttribute("villeDepart", villeDepart);
        request.setAttribute("villeDestination", villeDestination);
        request.setAttribute("date", date);

        request.getRequestDispatcher("/recherche.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String villeDepart = request.getParameter("villeDepart");
        String villeDestination = request.getParameter("villeDestination");
        String dateStr = request.getParameter("date");

        if (villeDepart == null || villeDepart.trim().isEmpty() || 
            villeDestination == null || villeDestination.trim().isEmpty() || 
            dateStr == null || dateStr.trim().isEmpty()) {
            request.setAttribute("error", "Veuillez sélectionner une ville de départ, une ville de destination et une date.");
            doGet(request, response);
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            LOGGER.warning("Format de date invalide: " + dateStr + " - " + e.getMessage());
            request.setAttribute("error", "Format de date invalide. Utilisez AAAA-MM-JJ.");
            doGet(request, response);
            return;
        }

        try {
            List<Trajet> trajets = trajetDAO.findAll();
            List<Voyage> voyages = new ArrayList<>();
            Map<Long, List<Promotion>> voyagePromotions = new HashMap<>();
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            int userLoyaltyPoints = user != null ? user.getLoyaltyPoints() : 0;

            for (Trajet trajet : trajets) {
                if (trajet.getDepartStation().getCity().equalsIgnoreCase(villeDepart) &&
                    trajet.getArrivalStation().getCity().equalsIgnoreCase(villeDestination)) {
                    List<Voyage> matchingVoyages = voyageDAO.findByTrajetAndDate(trajet, date);
                    for (Voyage voyage : matchingVoyages) {
                        List<Promotion> validPromotions = promotionDAO.findValidPromotionsForTrajet(trajet, date);
                        List<Promotion> applicablePromotions = new ArrayList<>();
                        for (Promotion promo : validPromotions) {
                            boolean isApplicable = true;
                            if (promo.getMinLoyaltyPoints() != null && userLoyaltyPoints < promo.getMinLoyaltyPoints()) {
                                isApplicable = false;
                            }
                            if (isApplicable) {
                                applicablePromotions.add(promo);
                            }
                        }
                        if (!applicablePromotions.isEmpty()) {
                            voyagePromotions.put(voyage.getId(), applicablePromotions);
                        }
                        voyages.add(voyage);
                    }
                }
            }

            request.setAttribute("voyages", voyages);
            request.setAttribute("voyagePromotions", voyagePromotions);
            request.setAttribute("villeDepart", villeDepart);
            request.setAttribute("villeDestination", villeDestination);
            request.setAttribute("date", dateStr);
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la recherche des voyages: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de la recherche. Veuillez réessayer.");
        }

        doGet(request, response);
    }
}