package com.badals.admin.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.Party} entity.
 */
@Data
public class PartyDTO implements Serializable {

    private Long id;

    private String company;

    private String siret;

    private String ape;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String email;

    @NotNull
    private String passwd;

    private String secureKey;

    private String salt;

    private Integer active;
    private String mobile;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartyDTO customerDTO = (PartyDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PartyDTO{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", siret='" + getSiret() + "'" +
            ", ape='" + getApe() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", passwd='" + getPasswd() + "'" +
            ", secureKey='" + getSecureKey() + "'" +
            ", salt='" + getSalt() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
