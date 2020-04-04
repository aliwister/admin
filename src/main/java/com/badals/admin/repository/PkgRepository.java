package com.badals.admin.repository;
import com.badals.admin.domain.Pkg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Pkg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PkgRepository extends JpaRepository<Pkg, Long> {

}
