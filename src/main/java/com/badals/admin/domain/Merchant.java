package com.badals.admin.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Merchant.
 */
@Entity
@Table(name = "merchant", catalog = "profileshop")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "added")
    private Instant added;

    public Merchant(Long l) {
        this.id = l;
    }

    public Merchant() {
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Merchant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getAdded() {
        return added;
    }

    public Merchant added(Instant added) {
        this.added = added;
        return this;
    }

    public void setAdded(Instant added) {
        this.added = added;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Merchant)) {
            return false;
        }
        return id != null && id.equals(((Merchant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", added='" + getAdded() + "'" +
            "}";
    }
}
