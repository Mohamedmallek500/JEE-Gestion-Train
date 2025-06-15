package controller;

import dao.ReservationDAO;
import util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/popular-trajets")
public class PopularTrajetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ReservationDAO reservationDAO = new ReservationDAO(HibernateUtil.getSessionFactory());
            List<Object[]> popularTrajets = reservationDAO.findMostPopularTrajets();
            request.setAttribute("popularTrajets", popularTrajets);
            request.getRequestDispatcher("/popular-trajets.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des trajets populaires : " + e.getMessage());
        }
    }
}