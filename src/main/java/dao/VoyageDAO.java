package dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.time.LocalDate;

import model.Trajet;
import model.Voyage;

public class VoyageDAO {
    private final SessionFactory sessionFactory;

    public VoyageDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Voyage voyage) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(voyage);
            session.getTransaction().commit();
        }
    }

    public Voyage findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Voyage.class, id);
        }
    }

    public List<Voyage> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Voyage", Voyage.class).list();
        }
    }

    public void delete(Voyage voyage) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(voyage);
            session.getTransaction().commit();
        }
    }

    public void update(Voyage voyage) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(voyage);
            session.getTransaction().commit();
        }
    }

    public List<Voyage> findByTrajetAndDate(Trajet trajet, LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Voyage v WHERE v.trajet = :trajet AND DATE(v.heureDepart) = :date",
                    Voyage.class)
                    .setParameter("trajet", trajet)
                    .setParameter("date", date)
                    .list();
        }
    }
}