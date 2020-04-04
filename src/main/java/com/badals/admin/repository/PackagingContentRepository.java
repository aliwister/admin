package com.badals.admin.repository;
import com.badals.admin.domain.PackagingContent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PackagingContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagingContentRepository extends JpaRepository<PackagingContent, Long> {

}
