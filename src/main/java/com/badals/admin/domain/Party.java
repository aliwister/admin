package com.badals.admin.domain;

import lombok.Data;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "party", catalog = "admin")
@SelectBeforeUpdate(false)
public class Party {
    @Id
    String id;
    String name;

    public Party() {
    }

    public Party(String id) {
        this.id = id;
    }

    public static Party STACKRY = new Party("m31");
    public static Party MYUS = new Party("m6");
    public static Party BADALS = new Party("m11");
    public static Party AMFORWARD = new Party("m7");
}
