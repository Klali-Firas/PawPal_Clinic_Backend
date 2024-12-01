package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.CommandeProduit}
 */
public class CommandeProduitDto implements Serializable {
    private  Integer id;
    private  Integer commandeId;
    private  Integer produitId;
    private  Integer quantite;

    public CommandeProduitDto(Integer id, Integer commandeId, Integer produitId, Integer quantite) {
        this.id = id;
        this.commandeId = commandeId;
        this.produitId = produitId;
        this.quantite = quantite;
    }

    public CommandeProduitDto() {
    }
    public Integer getId() {
        return id;
    }

    public Integer getCommandeId() {
        return commandeId;
    }

    public Integer getProduitId() {
        return produitId;
    }

    public Integer getQuantite() {
        return quantite;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandeProduitDto entity = (CommandeProduitDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.commandeId, entity.commandeId) &&
                Objects.equals(this.produitId, entity.produitId) &&
                Objects.equals(this.quantite, entity.quantite);}

    @Override
    public int hashCode() {
        return Objects.hash(id, commandeId, produitId, quantite);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "commandeId = " + commandeId + ", " +
                "produitId = " + produitId + ", " +
                "quantite = " + quantite +  ")";
    }
}