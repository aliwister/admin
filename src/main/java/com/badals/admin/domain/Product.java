package com.badals.admin.domain;

import com.badals.admin.domain.enumeration.Condition;
import com.badals.admin.domain.enumeration.VariationType;
import com.badals.admin.domain.pojo.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Product.
 */
@Entity
@Data
@Table(name = "product", catalog = "profileshop")
@SelectBeforeUpdate(false)
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Getter
    @Setter
    @Column(name = "ref")
    public String ref;

    @Getter @Setter
    private String slug;

    @Column(name = "parent_id")
    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private Product parent;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id",referencedColumnName = "ref")
    private Set<Product> children = new HashSet<>();;

    @NotNull
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "upc")
    private String upc;

    @Column(name = "api")
    private String api;

    @Column(name = "pricing_api")
    private String pricingApi;

    @Column(name = "unit")
    private String unit;

    @Column(name = "image")
    private String image;

    @Type(type = "json")
    @Column(name = "images", columnDefinition = "string")
    List<Gallery> gallery;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    private Boolean oversize= false;

    @Column(name = "stub", nullable = false)
    private Boolean stub;

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @Column(name = "in_stock", nullable = false)
    private Boolean inStock;

    public Product stub(boolean b) {
        stub = b;
        return this;
    }

    public Product inStock(Boolean b) {
        inStock = b;
        return this;
    }



    @Type(type = "json")
    @Column(name = "hashtags", columnDefinition = "string")
    private List<String> hashtags;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "brand")
    private String brand;

    @Column(name = "url")
    private String url;



    //@NotNull
    @Column(name = "last_modified_date", nullable = false, updatable=false, insertable=false)
    private Instant updated;

    //@NotNull
    @Column(name = "created_date", nullable = false, updatable=false, insertable=false)
    private Instant created;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_condition")
    private Condition condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private VariationType variationType;

    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "available_for_order")
    private Boolean availableForOrder;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Column(name = "volume_weight", precision = 21, scale = 2)
    private BigDecimal volumeWeight;
/*
    @OneToMany(mappedBy = "product", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<ProductLang> productLangs = new HashSet<>();*/


    @Column(name = "expires")
    private Instant expires;

    // Variations
    @Type(type = "json")
    @Column(name = "variation_dimensions", columnDefinition = "string")
    List<String> variationDimensions;

    @Type(type = "json")
    @Column(name = "variation_options", columnDefinition = "string")
    List<VariationOption> variationOptions;

    @Type(type = "json")
    @Column(name = "variations", columnDefinition = "string")
    List<Variation> variations;

    @Type(type = "json")
    @Column(name = "variation_attributes", columnDefinition = "string")
    List<Attribute> variationAttributes;

    @Type(type = "json")
    @Getter @Setter @Column(name = "description", columnDefinition = "string")
    List<ProductLang> langs = new ArrayList<>();



    @Type(type = "json")
    @Getter @Setter @Column(name = "delivery_profiles", columnDefinition = "string")
    List<Attribute> deliveryProfiles;

    @Type(type = "json")
    @Column(name = "list_price")
    PriceMap listPrice;

    @Type(type = "json")
    @Column(name = "price")
    PriceMap price;


    @Column @Getter @Setter
    String rating;


/*    @ManyToOne
    @JoinColumn(name="merchant_id", insertable = false, updatable = false)
    Merchant merchant;*/

    @Getter @Setter @Column(name = "merchant_id")
    private Long merchantId;

/*    @ManyToOne
    @JoinColumn(name="tenant_id", referencedColumnName = "name", insertable = false, updatable = false)
    Tenant tenant;*/

    @Getter @Setter @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "deleted")
    private Boolean deleted;

/*

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }*/

    public Product merchantId(Long l) {
        this.merchantId = l;
        return this;
    }

    public Product ref(String ref) {
        this.ref = ref;
        return this;
    }

    public Product slug(String slug) {
        this.slug = slug;
        return this;
    }

    public Product sku(String sku) {
        this.sku = sku;
        return this;
    }

    public Product upc(String upc) {
        this.upc = upc;
        return this;
    }

    public Product image(String image) {
        this.image = image;
        return this;
    }

    public Product gallery(List<Gallery> gallery) {
        this.gallery = gallery;
        return this;
    }

    public Product releaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Product active(Boolean active) {
        this.active = active;
        return this;
    }



    public Product title(String title) {
        this.title = title;
        return this;
    }

    public Product brand(String brand) {
        this.brand = brand;
        return this;
    }



    public Product updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public Product created(Instant created) {
        this.created = created;
        return this;
    }

    public Product condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public Product variationType(VariationType variationType) {
        this.variationType = variationType;
        return this;
    }

    public Product isUsed(Boolean isUsed) {
        this.isUsed = isUsed;
        return this;
    }

    public Product availableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
        return this;
    }

    public Product weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public Product volumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
        return this;
    }

/*    public Set<ProductLang> getProductLangs() {
        return productLangs;
    }

    public ProfileProduct productLangs(Set<ProductLang> productLangs) {
        this.productLangs = productLangs;
        return this;
    }*/


    public Product parent(Product master) {
        this.parent = master;
        return this;
    }

    public Set<Product> getChildren() {
        return children;
    }

    public void setChildren(Set<Product> children) {
        this.children = children;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", ref=" + ref +
                ", slug='" + slug + '\'' +
                ", parentId=" + parentId +
                '}';
    }



    public void removeChild(Product child) {
        this.children.remove(child);
    }

    public BigDecimal getComputedWeight() {
        if(volumeWeight != null && volumeWeight.compareTo(weight) == 1) {
            return volumeWeight;
        }
        return weight;
    }
}

