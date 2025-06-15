package dao;

import model.Promotion;
import model.Trajet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PromotionDAO {
    private final SessionFactory sessionFactory;

    public PromotionDAO(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }
        this.sessionFactory = sessionFactory;
    }

    public void save(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(promotion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save promotion: " + e.getMessage(), e);
        }
    }

    public Promotion findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive non-null value");
        }
        try (Session session = sessionFactory.openSession()) {
            Promotion promotion = session.get(Promotion.class, id);
            if (promotion == null) {
                throw new RuntimeException("Promotion with ID " + id + " not found");
            }
            return promotion;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find promotion by ID " + id + ": " + e.getMessage(), e);
        }
    }

    public List<Promotion> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Promotion", Promotion.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all promotions: " + e.getMessage(), e);
        }
    }

    public void update(Promotion promotion) {
        if (promotion == null || promotion.getId() == null) {
            throw new IllegalArgumentException("Promotion or ID cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(promotion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update promotion with ID " + promotion.getId() + ": " + e.getMessage(), e);
        }
    }

    public void delete(Promotion promotion) {
        if (promotion == null || promotion.getId() == null) {
            throw new IllegalArgumentException("Promotion or ID cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(promotion) ? promotion : session.merge(promotion));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete promotion with ID " + promotion.getId() + ": " + e.getMessage(), e);
        }
    }

    public List<Promotion> findValidPromotionsForTrajet(Trajet trajet, LocalDate date) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Promotion p WHERE (p.trajet = :trajet OR p.trajet IS NULL) " +
                         "AND p.startDate <= :date AND p.endDate >= :date";
            Query<Promotion> query = session.createQuery(hql, Promotion.class);
            query.setParameter("trajet", trajet);
            query.setParameter("date", date);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find valid promotions for trajet: " + e.getMessage(), e);
        }
    }
}