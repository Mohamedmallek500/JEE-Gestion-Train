package model;

import jakarta.persistence.*;

@Entity
@Table(name = "trajets")
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "depart_station_id", nullable = false)
    private Station departStation;

    @ManyToOne
    @JoinColumn(name = "arrival_station_id", nullable = false)
    private Station arrivalStation;

    // Constructeurs
    public Trajet() {}

    public Trajet(Station departStation, Station arrivalStation) {
        this.departStation = departStation;
        this.arrivalStation = arrivalStation;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Station getDepartStation() { return departStation; }
    public void setDepartStation(Station departStation) { this.departStation = departStation; }
    public Station getArrivalStation() { return arrivalStation; }
    public void setArrivalStation(Station arrivalStation) { this.arrivalStation = arrivalStation; }
}