package com.badals.admin.domain;

import com.badals.admin.domain.enumeration.PackageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * A Pkg.
 */
@Entity
@Data
@Table(name = "shipment_progress")
public class ShipmentProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column String reference;

    @Column BigDecimal total;

    @Column BigDecimal done;
    @Column BigDecimal todo;
}
