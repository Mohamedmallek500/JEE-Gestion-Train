package controller;

import dao.ReservationDAO;
import util.HibernateUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/revenue")
public class RevenueServlet extends HttpServlet {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/revenue.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ReservationDAO reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, FORMATTER);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, FORMATTER);

            Double totalRevenue = reservationDAO.calculateTotalRevenue(startDate, endDate);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.getRequestDispatcher("/revenue.jsp").forward(request, response);
        } catch (DateTimeParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Format de date invalide : " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors du calcul des revenus : " + e.getMessage());
        }
    }
}