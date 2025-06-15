package model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "voyages")
public class Voyage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @Column(name = "heure_depart", nullable = false)
    private LocalDateTime heureDepart;

    @Column(name = "heure_arrivee", nullable = false)
    private LocalDateTime heureArrivee;

    @Column(name = "prix", nullable = false)
    private double prix;

    @Column(name = "places_disponibles", nullable = false)
    private int placesDisponibles;

    // Constructeurs
    public Voyage() {}

    public Voyage(Trajet trajet, LocalDateTime heureDepart, LocalDateTime heureArrivee, double prix, int placesDisponibles) {
        this.trajet = trajet;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.prix = prix;
        this.placesDisponibles = placesDisponibles;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trajet getTrajet() { return trajet; }
    public void setTrajet(Trajet trajet) { this.trajet = trajet; }
    public LocalDateTime getHeureDepart() { return heureDepart; }
    public void setHeureDepart(LocalDateTime heureDepart) { this.heureDepart = heureDepart; }
    public LocalDateTime getHeureArrivee() { return heureArrivee; }
    public void setHeureArrivee(LocalDateTime heureArrivee) { this.heureArrivee = heureArrivee; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }

    // Méthodes pour formater LocalDateTime
    public String getHeureDepartFormatted() {
        return heureDepart != null 
            ? heureDepart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) 
            : "";
    }

    public String getHeureArriveeFormatted() {
        return heureArrivee != null 
            ? heureArrivee.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) 
            : "";
    }

    // Méthode pour calculer la durée
    public String getDuree() {
        if (heureDepart == null || heureArrivee == null) {
            return "";
        }
        Duration duration = Duration.between(heureDepart, heureArrivee);
        long heures = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%dh%02dm", heures, minutes);
    }
}