package com.PawPal_Clinic.Backend.dto;

import com.PawPal_Clinic.Backend.model.Rappel;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link Rappel}
 */
public class RappelDto implements Serializable {
    private  Integer id;
    private  Integer animalId;
    private  Integer veterinaireId;
    private  Instant dateRappel;
    private  String typeRappel;
    private  String statut;
    private  Instant creeLe;

    public RappelDto(Integer id, Integer animalId, Integer veterinaireId, Instant dateRappel, String typeRappel, String statut, Instant creeLe) {
        this.id = id;
        this.animalId = animalId;
        this.veterinaireId = veterinaireId;
        this.dateRappel = dateRappel;
        this.typeRappel = typeRappel;
        this.statut = statut;
        this.creeLe = creeLe;
    }

    public RappelDto() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getAnimalId() {
        return animalId;
    }

    public Integer getVeterinaireId() {
        return veterinaireId;
    }

    public Instant getDateRappel() {
        return dateRappel;
    }

    public String getTypeRappel() {
        return typeRappel;
    }

    public String getStatut() {
        return statut;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RappelDto entity = (RappelDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.animalId, entity.animalId) &&
                Objects.equals(this.veterinaireId, entity.veterinaireId) &&
                Objects.equals(this.dateRappel, entity.dateRappel) &&
                Objects.equals(this.typeRappel, entity.typeRappel) &&
                Objects.equals(this.statut, entity.statut) &&
                Objects.equals(this.creeLe, entity.creeLe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalId, veterinaireId, dateRappel, typeRappel, statut, creeLe);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "animalId = " + animalId + ", " +
                "veterinaireId = " + veterinaireId + ", " +
                "dateRappel = " + dateRappel + ", " +
                "typeRappel = " + typeRappel + ", " +
                "statut = " + statut + ", " +
                "creeLe = " + creeLe + ")";
    }
}