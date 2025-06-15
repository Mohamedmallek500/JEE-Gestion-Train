package model;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    @Column(name = "classe", nullable = false)
    private String classe;

    @Column(name = "preferences")
    private String preferences;

    @Column(name = "date_reservation", nullable = false)
    private java.time.LocalDateTime dateReservation;

    @Column(name = "etat", nullable = false)
    private String etat;

    // Constructeurs
    public Reservation() {
        this.dateReservation = java.time.LocalDateTime.now();
        this.etat = "acheté"; // État par défaut
    }

    public Reservation(User user, Voyage voyage, String classe, String preferences) {
        this.user = user;
        this.voyage = voyage;
        this.classe = classe;
        this.preferences = preferences;
        this.dateReservation = java.time.LocalDateTime.now();
        this.etat = "acheté"; // État par défaut
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Voyage getVoyage() { return voyage; }
    public void setVoyage(Voyage voyage) { this.voyage = voyage; }
    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    public java.time.LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(java.time.LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}