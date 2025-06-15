package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Station;
import model.Trajet;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;

import dao.StationDAO;
import dao.TrajetDAO;

@WebServlet("/admin/trajets")
public class AdminTrajetServlet extends HttpServlet {
    private TrajetDAO trajetDAO;
    private StationDAO stationDAO;

    @Override
    public void init() throws ServletException {
        trajetDAO = new TrajetDAO(HibernateUtil.getSessionFactory());
        stationDAO = new StationDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        List<Trajet> trajets = trajetDAO.findAll();
        List<Station> stations = stationDAO.findAll();
        request.setAttribute("trajets", trajets);
        request.setAttribute("stations", stations);

        if ("edit".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Trajet trajet = trajetDAO.findById(id);
            if (trajet != null) {
                request.setAttribute("trajet", trajet);
            } else {
                request.setAttribute("error", "Trajet introuvable pour modification.");
            }
        }

        request.getRequestDispatcher("/admin-trajets.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                Long departStationId = Long.parseLong(request.getParameter("departStationId"));
                Long arrivalStationId = Long.parseLong(request.getParameter("arrivalStationId"));
                Station departStation = stationDAO.findById(departStationId);
                Station arrivalStation = stationDAO.findById(arrivalStationId);
                if (departStation == null || arrivalStation == null) {
                    throw new IllegalArgumentException("Station de départ ou d'arrivée introuvable.");
                }
                Trajet trajet = new Trajet(departStation, arrivalStation);
                trajetDAO.save(trajet);
                request.getSession().setAttribute("success", "Trajet ajouté avec succès.");
            } else if ("update".equals(action) && request.getParameter("id") != null) {
                Long id = Long.parseLong(request.getParameter("id"));
                Trajet trajet = trajetDAO.findById(id);
                if (trajet != null) {
                    Long departStationId = Long.parseLong(request.getParameter("departStationId"));
                    Long arrivalStationId = Long.parseLong(request.getParameter("arrivalStationId"));
                    Station departStation = stationDAO.findById(departStationId);
                    Station arrivalStation = stationDAO.findById(arrivalStationId);
                    if (departStation == null || arrivalStation == null) {
                        throw new IllegalArgumentException("Station de départ ou d'arrivée introuvable.");
                    }
                    trajet.setDepartStation(departStation);
                    trajet.setArrivalStation(arrivalStation);
                    trajetDAO.update(trajet);
                    request.getSession().setAttribute("success", "Trajet modifié avec succès.");
                } else {
                    request.getSession().setAttribute("error", "Trajet introuvable pour modification.");
                }
            } else if ("delete".equals(action) && request.getParameter("id") != null) {
                Long id = Long.parseLong(request.getParameter("id"));
                Trajet trajet = trajetDAO.findById(id);
                if (trajet != null) {
                    trajetDAO.delete(trajet);
                    request.getSession().setAttribute("success", "Trajet supprimé avec succès.");
                } else {
                    request.getSession().setAttribute("error", "Trajet introuvable pour suppression.");
                }
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Erreur lors de l'opération : " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/trajets");
    }
}