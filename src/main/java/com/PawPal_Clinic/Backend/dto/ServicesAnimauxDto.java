package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.ServicesAnimaux}
 */
public class ServicesAnimauxDto implements Serializable {
    private  Integer id;
    private  Integer animalId;
    private  Integer veterinaireId;
    private  Integer serviceId;
    private  Instant dateService;
    private  String remarques;

    public ServicesAnimauxDto(Integer id, Integer animalId, Integer veterinaireId, Integer serviceId, Instant dateService, String remarques) {
        this.id = id;
        this.animalId = animalId;
        this.veterinaireId = veterinaireId;
        this.serviceId = serviceId;
        this.dateService = dateService;
        this.remarques = remarques;
    }

    public ServicesAnimauxDto() {
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

    public Integer getServiceId() {
        return serviceId;
    }

    public Instant getDateService() {
        return dateService;
    }

    public String getRemarques() {
        return remarques;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicesAnimauxDto entity = (ServicesAnimauxDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.animalId, entity.animalId) &&
                Objects.equals(this.veterinaireId, entity.veterinaireId) &&
                Objects.equals(this.serviceId, entity.serviceId) &&
                Objects.equals(this.dateService, entity.dateService) &&
                Objects.equals(this.remarques, entity.remarques);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalId, veterinaireId, serviceId, dateService, remarques);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "animalId = " + animalId + ", " +
                "veterinaireId = " + veterinaireId + ", " +
                "serviceId = " + serviceId + ", " +
                "dateService = " + dateService + ", " +
                "remarques = " + remarques + ")";
    }
}