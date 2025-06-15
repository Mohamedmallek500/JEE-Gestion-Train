package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new IllegalArgumentException("SessionFactory cannot be null");
        }
        this.sessionFactory = sessionFactory;
    }

    // Sauvegarder un nouvel utilisateur
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    // Trouver un utilisateur par ID
    public User findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive non-null value");
        }
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new RuntimeException("User with ID " + id + " not found");
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by ID " + id + ": " + e.getMessage(), e);
        }
    }

    // Trouver un utilisateur par nom d'utilisateur
    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return user; // Retourne null si non trouvé, ce qui est acceptable pour cette méthode
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by username " + username + ": " + e.getMessage(), e);
        }
    }

    // Trouver un utilisateur par email
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return user; // Retourne null si non trouvé, ce qui est acceptable pour cette méthode
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by email " + email + ": " + e.getMessage(), e);
        }
    }

    // Lister tous les utilisateurs
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).list();
            return users != null ? users : List.of(); // Retourne une liste vide si null
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all users: " + e.getMessage(), e);
        }
    }

    // Mettre à jour un utilisateur (utilisé pour bloquer/débloquer)
    public void update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update operation");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user); // Utilise merge pour mettre à jour une entité détachée
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update user with ID " + user.getId() + ": " + e.getMessage(), e);
        }
    }

    // Supprimer un utilisateur
    public void delete(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for delete operation");
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(user) ? user : session.merge(user)); // Merge si l'entité est détachée
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete user with ID " + user.getId() + ": " + e.getMessage(), e);
        }
    }
}