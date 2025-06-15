package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Trajet;
import util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import dao.TrajetDAO;

/**
 * Servlet implementation class RechercheProchainTrajetServlet
 */
@WebServlet("/rechercheProchain")
public class RechercheProchainTrajetServlet extends HttpServlet {
    private TrajetDAO trajetDAO;

    @Override
    public void init() throws ServletException {
        trajetDAO = new TrajetDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String villeDepart = request.getParameter("villeDepart");
        String villeDestination = request.getParameter("villeDestination");
        String dateStr = request.getParameter("date");

        LocalDate date = LocalDate.parse(dateStr);
        List<Trajet> trajets = trajetDAO.findTrajets(villeDepart, villeDestination, date);

        request.setAttribute("trajets", trajets);
        request.setAttribute("premierTrajetId", request.getParameter("trajetId")); // Pour garder une trace du premier trajet
        request.getRequestDispatcher("/resultatsProchain.jsp").forward(request, response);
    }
}
