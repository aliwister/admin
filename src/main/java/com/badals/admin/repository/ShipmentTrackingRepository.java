package com.badals.admin.repository;
import com.badals.admin.domain.Product;
import com.badals.admin.domain.ShipmentTracking;
import com.badals.admin.domain.projection.SortQueue;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the  ShipmentTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentTrackingRepository extends JpaRepository<ShipmentTracking, Long> {

}