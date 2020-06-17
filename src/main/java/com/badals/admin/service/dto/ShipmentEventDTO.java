package com.badals.admin.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentEvent} entity.
 */
public class ShipmentEventDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer ref;

    @NotNull
    private String lang;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRef() {
        return ref;
    }

    public void setRef(Integer ref) {
        this.ref = ref;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentEventDTO shipmentEventDTO = (ShipmentEventDTO) o;
        if (shipmentEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShipmentEventDTO{" +
            "id=" + getId() +
            ", ref=" + getRef() +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
