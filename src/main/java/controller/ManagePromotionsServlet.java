package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Promotion;
import model.Trajet;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import dao.PromotionDAO;
import dao.TrajetDAO;

@WebServlet("/admin/promotions")
public class ManagePromotionsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ManagePromotionsServlet.class.getName());
    private PromotionDAO promotionDAO;
    private TrajetDAO trajetDAO;

    @Override
    public void init() throws ServletException {
        promotionDAO = new PromotionDAO(HibernateUtil.getSessionFactory());
        trajetDAO = new TrajetDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Promotion> promotions = promotionDAO.findAll();
        List<Trajet> trajets = trajetDAO.findAll();
        request.setAttribute("promotions", promotions);
        request.setAttribute("trajets", trajets);
        request.getRequestDispatcher("/promotions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String code = request.getParameter("code");
                String description = request.getParameter("description");
                double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));
                LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
                LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
                Long trajetId = request.getParameter("trajetId").isEmpty() ? null : Long.parseLong(request.getParameter("trajetId"));
                Integer minLoyaltyPoints = request.getParameter("minLoyaltyPoints").isEmpty() ? null : Integer.parseInt(request.getParameter("minLoyaltyPoints"));

                Trajet trajet = trajetId != null ? trajetDAO.findById(trajetId) : null;
                Promotion promotion = new Promotion(code, description, discountPercentage, startDate, endDate, trajet, minLoyaltyPoints);
                promotionDAO.save(promotion);
                request.setAttribute("message", "Promotion ajoutée avec succès.");
            } else if ("edit".equals(action)) {
                Long promotionId = Long.parseLong(request.getParameter("promotionId"));
                Promotion promotion = promotionDAO.findById(promotionId);
                request.setAttribute("editPromotion", promotion);
            } else if ("update".equals(action)) {
                Long promotionId = Long.parseLong(request.getParameter("promotionId"));
                Promotion promotion = promotionDAO.findById(promotionId);
                promotion.setCode(request.getParameter("code"));
                promotion.setDescription(request.getParameter("description"));
                promotion.setDiscountPercentage(Double.parseDouble(request.getParameter("discountPercentage")));
                promotion.setStartDate(LocalDate.parse(request.getParameter("startDate")));
                promotion.setEndDate(LocalDate.parse(request.getParameter("endDate")));
                Long trajetId = request.getParameter("trajetId").isEmpty() ? null : Long.parseLong(request.getParameter("trajetId"));
                promotion.setTrajet(trajetId != null ? trajetDAO.findById(trajetId) : null);
                promotion.setMinLoyaltyPoints(request.getParameter("minLoyaltyPoints").isEmpty() ? null : Integer.parseInt(request.getParameter("minLoyaltyPoints")));
                promotionDAO.update(promotion);
                request.setAttribute("message", "Promotion modifiée avec succès.");
            } else if ("delete".equals(action)) {
                Long promotionId = Long.parseLong(request.getParameter("promotionId"));
                Promotion promotion = promotionDAO.findById(promotionId);
                promotionDAO.delete(promotion);
                request.setAttribute("message", "Promotion supprimée avec succès.");
            }
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la gestion des promotions: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de l'opération: " + e.getMessage());
        }

        doGet(request, response);
    }
}