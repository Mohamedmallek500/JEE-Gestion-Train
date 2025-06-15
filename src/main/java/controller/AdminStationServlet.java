package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Station;
import model.User;
import util.HibernateUtil;

import java.io.IOException;
import java.util.List;

import dao.StationDAO;

@WebServlet("/admin/stations")
public class AdminStationServlet extends HttpServlet {
    private StationDAO stationDAO;

    @Override
    public void init() throws ServletException {
        stationDAO = new StationDAO(HibernateUtil.getSessionFactory());
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
        List<Station> stations = stationDAO.findAll();
        request.setAttribute("stations", stations);

        if ("edit".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Station station = stationDAO.findById(id);
            request.setAttribute("station", station);
        }

        request.getRequestDispatcher("/admin-stations.jsp").forward(request, response);
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
            String name = request.getParameter("name");
            String city = request.getParameter("city");
            Station station = new Station(name, city);
            stationDAO.save(station);
        } else if ("update".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Station station = stationDAO.findById(id);
            if (station != null) {
                station.setName(request.getParameter("name"));
                station.setCity(request.getParameter("city"));
                stationDAO.update(station);
            }
        } else if ("delete".equals(action) && request.getParameter("id") != null) {
            Long id = Long.parseLong(request.getParameter("id"));
            Station station = stationDAO.findById(id);
            if (station != null) {
                stationDAO.delete(station);
            }
        }

        response.sendRedirect("stations");
    }
}