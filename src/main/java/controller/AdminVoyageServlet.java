package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Trajet;
import model.User;
import model.Voyage;
import util.HibernateUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import dao.TrajetDAO;
import dao.VoyageDAO;

@WebServlet("/admin/voyages")
public class AdminVoyageServlet extends HttpServlet {
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        List<Voyage> voyages = voyageDAO.findAll();
        List<Trajet> trajets = trajetDAO.findAll();
        request.setAttribute("voyages", voyages);
        request.setAttribute("trajets", trajets);

        if ("edit".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Voyage voyage = voyageDAO.findById(id);
            request.setAttribute("voyage", voyage);
        }

        request.getRequestDispatcher("/admin-voyages.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            Long trajetId = Long.parseLong(request.getParameter("trajetId"));
            LocalDateTime heureDepart = LocalDateTime.parse(request.getParameter("heureDepart"));
            LocalDateTime heureArrivee = LocalDateTime.parse(request.getParameter("heureArrivee"));
            double prix = Double.parseDouble(request.getParameter("prix"));
            int placesDisponibles = Integer.parseInt(request.getParameter("placesDisponibles"));
            Trajet trajet = trajetDAO.findById(trajetId);
            Voyage voyage = new Voyage(trajet, heureDepart, heureArrivee, prix, placesDisponibles);
            voyageDAO.save(voyage);
        } else if ("update".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Voyage voyage = voyageDAO.findById(id);
            if (voyage != null) {
                Long trajetId = Long.parseLong(request.getParameter("trajetId"));
                voyage.setTrajet(trajetDAO.findById(trajetId));
                voyage.setHeureDepart(LocalDateTime.parse(request.getParameter("heureDepart")));
                voyage.setHeureArrivee(LocalDateTime.parse(request.getParameter("heureArrivee")));
                voyage.setPrix(Double.parseDouble(request.getParameter("prix")));
                voyage.setPlacesDisponibles(Integer.parseInt(request.getParameter("placesDisponibles")));
                voyageDAO.update(voyage);
            }
        } else if ("delete".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Voyage voyage = voyageDAO.findById(id);
            if (voyage != null) {
                voyageDAO.delete(voyage);
            }
        }

        response.sendRedirect("voyages");
    }
}