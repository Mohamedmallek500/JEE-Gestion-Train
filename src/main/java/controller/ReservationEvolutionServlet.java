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
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@WebServlet("/admin/reservation-evolution")
public class ReservationEvolutionServlet extends HttpServlet {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final Logger logger = Logger.getLogger(ReservationEvolutionServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            String startDateStr = startDate.format(FORMATTER);
            String endDateStr = endDate.format(FORMATTER);

            ReservationDAO reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
            List<Object[]> evolutionData = reservationDAO.findReservationEvolution(startDate, endDate);

            logger.info("doGet - Evolution Data: " + evolutionData);
            if (evolutionData.isEmpty()) {
                logger.info("doGet - No data found for period: " + startDateStr + " to " + endDateStr);
                request.setAttribute("errorMessage", "Aucune donnée trouvée pour la période sélectionnée.");
            } else {
                List<String> labels = evolutionData.stream()
                    .map(row -> row[0].toString())
                    .collect(Collectors.toList());
                List<Long> data = evolutionData.stream()
                    .map(row -> ((Number) row[1]).longValue())
                    .collect(Collectors.toList());

                logger.info("doGet - Labels: " + labels);
                logger.info("doGet - Data: " + data);

                request.setAttribute("evolutionData", evolutionData);
                request.setAttribute("labels", labels);
                request.setAttribute("data", data);
            }

            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            logger.info("doGet - Attributes set - evolutionData: " + request.getAttribute("evolutionData"));
            request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Erreur dans doGet: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors du chargement initial : " + e.getMessage());
            request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            if (startDateStr == null || startDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "La date de début est requise.");
                setDefaultAttributes(request);
                request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
                return;
            }
            if (endDateStr == null || endDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "La date de fin est requise.");
                setDefaultAttributes(request);
                request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
                return;
            }

            LocalDateTime startDate = LocalDateTime.parse(startDateStr, FORMATTER);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, FORMATTER);

            if (startDate.isAfter(endDate)) {
                request.setAttribute("errorMessage", "La date de début doit être antérieure à la date de fin.");
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
                return;
            }

            ReservationDAO reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
            List<Object[]> evolutionData = reservationDAO.findReservationEvolution(startDate, endDate);

            logger.info("doPost - Evolution Data: " + evolutionData);
            if (evolutionData.isEmpty()) {
                logger.info("doPost - No data found for period: " + startDateStr + " to " + endDateStr);
                request.setAttribute("errorMessage", "Aucune donnée trouvée pour la période sélectionnée.");
            } else {
                List<String> labels = evolutionData.stream()
                    .map(row -> row[0].toString())
                    .collect(Collectors.toList());
                List<Long> data = evolutionData.stream()
                    .map(row -> ((Number) row[1]).longValue())
                    .collect(Collectors.toList());

                logger.info("doPost - Labels: " + labels);
                logger.info("doPost - Data: " + data);

                request.setAttribute("evolutionData", evolutionData);
                request.setAttribute("labels", labels);
                request.setAttribute("data", data);
            }

            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            logger.info("doPost - Attributes set - evolutionData: " + request.getAttribute("evolutionData"));
            request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
        } catch (DateTimeParseException e) {
            logger.severe("Format de date invalide: " + e.getMessage());
            request.setAttribute("errorMessage", "Format de date invalide : " + e.getMessage());
            setDefaultAttributes(request);
            request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Erreur dans doPost: " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur lors de la génération du graphique : " + e.getMessage());
            setDefaultAttributes(request);
            request.getRequestDispatcher("/reservation-evolution.jsp").forward(request, response);
        }
    }

    private void setDefaultAttributes(HttpServletRequest request) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        String startDateStr = startDate.format(FORMATTER);
        String endDateStr = endDate.format(FORMATTER);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
    }
}