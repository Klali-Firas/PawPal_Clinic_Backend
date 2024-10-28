package com.PawPal_Clinic.Backend.dto;

import com.PawPal_Clinic.Backend.model.Commande;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link Commande}
 */
public class CommandeDto implements Serializable {
    private  Integer id;
    private  Integer proprietaireId;
    private  Instant dateCommande;
    private  String statut;

    public CommandeDto(Integer id, Integer proprietaireId, Instant dateCommande, String statut) {
        this.id = id;
        this.proprietaireId = proprietaireId;
        this.dateCommande = dateCommande;
        this.statut = statut;
    }

    public CommandeDto() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getProprietaireId() {
        return proprietaireId;
    }

    public Instant getDateCommande() {
        return dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandeDto entity = (CommandeDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.proprietaireId, entity.proprietaireId) &&
                Objects.equals(this.dateCommande, entity.dateCommande) &&
                Objects.equals(this.statut, entity.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proprietaireId, dateCommande, statut);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "proprietaireId = " + proprietaireId + ", " +
                "dateCommande = " + dateCommande + ", " +
                "statut = " + statut + ")";
    }
}