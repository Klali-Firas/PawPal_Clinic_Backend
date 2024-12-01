package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class AviDto implements Serializable {
    private Integer id;
    private Integer rendezVousId;
    private Integer proprietaireId;
    private Integer note;
    private String commentaire;
    private Instant creeLe;

    public AviDto(Integer id, Integer rendezVousId, Integer proprietaireId, Integer note, String commentaire, Instant creeLe) {
        this.id = id;
        this.rendezVousId = rendezVousId;
        this.proprietaireId = proprietaireId;
        this.note = note;
        this.commentaire = commentaire;
        this.creeLe = creeLe;
    }

    public AviDto() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getRendezVousId() {
        return rendezVousId;
    }

    public Integer getProprietaireId() {
        return proprietaireId;
    }

    public Integer getNote() {
        return note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Instant getCreeLe() {
        return creeLe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AviDto entity = (AviDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.rendezVousId, entity.rendezVousId) &&
                Objects.equals(this.proprietaireId, entity.proprietaireId) &&
                Objects.equals(this.note, entity.note) &&
                Objects.equals(this.commentaire, entity.commentaire) &&
                Objects.equals(this.creeLe, entity.creeLe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rendezVousId, proprietaireId, note, commentaire, creeLe);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "rendezVousId = " + rendezVousId + ", " +
                "proprietaireId = " + proprietaireId + ", " +
                "note = " + note + ", " +
                "commentaire = " + commentaire + ", " +
                "creeLe = " + creeLe + ")";
    }
}