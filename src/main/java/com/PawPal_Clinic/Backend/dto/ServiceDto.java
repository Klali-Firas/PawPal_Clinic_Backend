package com.PawPal_Clinic.Backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO for {@link com.PawPal_Clinic.Backend.model.Service}
 */
public class ServiceDto implements Serializable {
    private  Integer id;
    private  String nomService;
    private  String description;
    private  BigDecimal prix;

    public ServiceDto(Integer id, String nomService, String description, BigDecimal prix) {
        this.id = id;
        this.nomService = nomService;
        this.description = description;
        this.prix = prix;
    }

    public ServiceDto() {
    }
    public Integer getId() {
        return id;
    }

    public String getNomService() {
        return nomService;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDto entity = (ServiceDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nomService, entity.nomService) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.prix, entity.prix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomService, description, prix);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nomService = " + nomService + ", " +
                "description = " + description + ", " +
                "prix = " + prix + ")";
    }
}