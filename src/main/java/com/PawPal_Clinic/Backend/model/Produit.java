package com.PawPal_Clinic.Backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "produits")
public class Produit {
    @Id
    @ColumnDefault("nextval('produits_produit_id_seq'::regclass)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produit_id", nullable = false)
    private Integer id;

    @Column(name = "nom_produit", nullable = false, length = 100)
    private String nomProduit;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "quantite_stock", nullable = false)
    private Integer quantiteStock;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "cree_le", insertable = false, updatable = false)
    private Instant creeLe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
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

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Instant creeLe) {
        this.creeLe = creeLe;
    }

}