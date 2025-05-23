package com.badals.admin.domain;
import com.badals.admin.aop.tenant.TenantSupport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ItemIssuance.
 */
@Entity
@Table(name = "item_issuance")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "itemissuance")
public class ItemIssuance extends Auditable implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name="tenant_id")
    private String tenantId;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

//    @ManyToOne
//    @JsonIgnoreProperties("itemIssuances")
//    private Product product;

    //@ManyToOne
    //@JsonIgnoreProperties("itemIssuances")
    @Column(name="product_id")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ItemIssuance productId(Long productId) {
        this.productId = productId;
        return this;
    }

    @ManyToOne
    @JsonIgnoreProperties("itemIssuances")
    private ShipmentItem shipmentItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ItemIssuance quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public ItemIssuance product(Product product) {
//        this.product = product;
//        return this;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }

    public ShipmentItem getShipmentItem() {
        return shipmentItem;
    }

    public ItemIssuance shipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
        return this;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemIssuance)) {
            return false;
        }
        return id != null && id.equals(((ItemIssuance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ItemIssuance{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
