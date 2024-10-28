package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.Produit}
 */
public class ProduitDto implements Serializable {
    private  Integer id;
    private  String nomProduit;
    private  String description;
    private  BigDecimal prix;
    private  Integer quantiteStock;
    private  Instant creeLe;

    public ProduitDto(Integer id, String nomProduit, String description, BigDecimal prix, Integer quantiteStock, Instant creeLe) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.description = description;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.creeLe = creeLe;
    }

    public ProduitDto() {
    }
    public Integer getId() {
        return id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduitDto entity = (ProduitDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nomProduit, entity.nomProduit) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.prix, entity.prix) &&
                Objects.equals(this.quantiteStock, entity.quantiteStock) &&
                Objects.equals(this.creeLe, entity.creeLe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomProduit, description, prix, quantiteStock, creeLe);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nomProduit = " + nomProduit + ", " +
                "description = " + description + ", " +
                "prix = " + prix + ", " +
                "quantiteStock = " + quantiteStock + ", " +
                "creeLe = " + creeLe + ")";
    }
}