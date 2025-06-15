package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Station;

import java.util.List;
import java.util.logging.Logger;

public class StationDAO {
    private static final Logger LOGGER = Logger.getLogger(StationDAO.class.getName());
    private final SessionFactory sessionFactory;

    public StationDAO(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }
        this.sessionFactory = sessionFactory;
    }

    public void save(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station to save cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(station);
            transaction.commit();
            LOGGER.info("Station saved successfully: " + station.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.severe("Failed to save station: " + e.getMessage());
            throw new RuntimeException("Failed to save station: " + e.getMessage(), e);
        }
    }

    public Station findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Station ID must be a positive non-null value");
        }
        try (Session session = sessionFactory.openSession()) {
            Station station = session.get(Station.class, id);
            if (station == null) {
                LOGGER.warning("No station found with ID: " + id);
            }
            return station;
        } catch (Exception e) {
            LOGGER.severe("Failed to find station by ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Failed to find station by ID: " + e.getMessage(), e);
        }
    }

    public List<Station> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Station> stations = session.createQuery("FROM Station", Station.class).list();
            LOGGER.info("Retrieved " + stations.size() + " stations");
            return stations;
        } catch (Exception e) {
            LOGGER.severe("Failed to retrieve all stations: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve all stations: " + e.getMessage(), e);
        }
    }

    public void delete(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station to delete cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(station);
            transaction.commit();
            LOGGER.info("Station deleted successfully: " + station.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.severe("Failed to delete station: " + e.getMessage());
            throw new RuntimeException("Failed to delete station: " + e.getMessage(), e);
        }
    }

    public void update(Station station) {
        if (station == null) {
            throw new IllegalArgumentException("Station to update cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(station);
            transaction.commit();
            LOGGER.info("Station updated successfully: " + station.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.severe("Failed to update station: " + e.getMessage());
            throw new RuntimeException("Failed to update station: " + e.getMessage(), e);
        }
    }
}