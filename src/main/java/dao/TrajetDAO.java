package dao;

import model.Trajet;
import model.Station;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.time.LocalDate;


import java.util.List;

public class TrajetDAO {
    private final SessionFactory sessionFactory;

    public TrajetDAO(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }
        this.sessionFactory = sessionFactory;
    }

    // Sauvegarder un nouveau trajet
    public void save(Trajet trajet) {
        if (trajet == null || trajet.getDepartStation() == null || trajet.getArrivalStation() == null) {
            throw new IllegalArgumentException("Trajet or stations cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(trajet);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save trajet: " + e.getMessage(), e);
        }
    }

    // Trouver un trajet par ID
    public Trajet findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive non-null value");
        }
        try (Session session = sessionFactory.openSession()) {
            Trajet trajet = session.get(Trajet.class, id);
            if (trajet == null) {
                throw new RuntimeException("Trajet with ID " + id + " not found");
            }
            return trajet;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find trajet by ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Lister tous les trajets
    public List<Trajet> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Trajet", Trajet.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all trajets: " + e.getMessage(), e);
        }
    }

    // Mettre à jour un trajet
    public void update(Trajet trajet) {
        if (trajet == null || trajet.getId() == null || trajet.getDepartStation() == null || trajet.getArrivalStation() == null) {
            throw new IllegalArgumentException("Trajet, ID, or stations cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(trajet);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update trajet with ID " + trajet.getId() + ": " + e.getMessage(), e);
        }
    }

    // Supprimer un trajet
    public void delete(Trajet trajet) {
        if (trajet == null || trajet.getId() == null) {
            throw new IllegalArgumentException("Trajet or ID cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(trajet) ? trajet : session.merge(trajet));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete trajet with ID " + trajet.getId() + ": " + e.getMessage(), e);
        }
    }
    
    
    public List<Trajet> findTrajets(String villeDepart, String villeDestination, LocalDate date) {
        if (villeDepart == null || villeDestination == null) {
            throw new IllegalArgumentException("Departure and destination cities cannot be null");
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT t FROM Trajet t " +
                    "WHERE t.departStation.city = :villeDepart " +
                    "AND t.arrivalStation.city = :villeDestination",
                    Trajet.class)
                    .setParameter("villeDepart", villeDepart)
                    .setParameter("villeDestination", villeDestination)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find trajets for " + villeDepart + " to " + villeDestination + ": " + e.getMessage(), e);
        }
    }
}