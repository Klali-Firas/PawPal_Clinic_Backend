package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @ColumnDefault("nextval('services_service_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Integer id;

    @Column(name = "nom_service", nullable = false, length = 100)
    private String nomService;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "prix", precision = 10, scale = 2)
    private BigDecimal prix;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

}