package model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "role", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'user'")
    private String role;

    @Column(name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isBlocked;

    @Column(name = "loyalty_points", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int loyaltyPoints; // Champ pour les points de fidélité

    // Constructeurs
    public User() {
        this.role = "user";
        this.isBlocked = false;
        this.loyaltyPoints = 0;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "user";
        this.isBlocked = false;
        this.loyaltyPoints = 0;
    }

    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isBlocked = false;
        this.loyaltyPoints = 0;
    }

    public User(String username, String password, String email, String role, int loyaltyPoints) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isBlocked = false;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { this.isBlocked = blocked; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    // Méthode utilitaire pour vérifier si l'utilisateur est administrateur
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "', role='" + role + "', isBlocked=" + isBlocked + ", loyaltyPoints=" + loyaltyPoints + "}";
    }
}