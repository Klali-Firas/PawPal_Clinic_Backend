package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.RendezVous}
 */
public class RendezVousDto implements Serializable {
    private  Integer id;
    private  Integer animalId;
    private  Integer veterinaireId;
    private  Instant dateRendezVous;
    private  String statut;
    private  Integer motif;
    private  Instant creeLe;
    private  String remarques;


    public RendezVousDto(Integer id, Integer animalId, Integer veterinaireId, Instant dateRendezVous, String statut, Integer motif, Instant creeLe, String remarques) {
        this.id = id;
        this.animalId = animalId;
        this.veterinaireId = veterinaireId;
        this.dateRendezVous = dateRendezVous;
        this.statut = statut;
        this.motif = motif;
        this.creeLe = creeLe;
        this.remarques = remarques;
    }


    public RendezVousDto() {
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

    public Instant getDateRendezVous() {
        return dateRendezVous;
    }

    public String getStatut() {
        return statut;
    }

    public Integer getMotif() {
        return motif;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RendezVousDto entity = (RendezVousDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.animalId, entity.animalId) &&
                Objects.equals(this.veterinaireId, entity.veterinaireId) &&
                Objects.equals(this.dateRendezVous, entity.dateRendezVous) &&
                Objects.equals(this.statut, entity.statut) &&
                Objects.equals(this.motif, entity.motif) &&
                Objects.equals(this.creeLe, entity.creeLe);
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalId, veterinaireId, dateRendezVous, statut, motif, creeLe);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "animalId = " + animalId + ", " +
                "veterinaireId = " + veterinaireId + ", " +
                "dateRendezVous = " + dateRendezVous + ", " +
                "statut = " + statut + ", " +
                "motif = " + motif + ", " +
                "creeLe = " + creeLe + ")";
    }
}