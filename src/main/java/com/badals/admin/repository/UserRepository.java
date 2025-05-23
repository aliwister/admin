package com.badals.admin.repository;

import com.badals.admin.domain.Customer;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findById(Long id);
    boolean existsByEmail(String email);

    Optional<Customer> findOneBySecureKey(String activationKey);

    Optional<Customer> findOneBySecureKeyAndEmail(String activationKey, String email);
    //List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);


    Optional<Customer> findOneByResetKey(String resetKey);

    Optional<Customer> findOneByEmailIgnoreCase(String email);

    //Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<Customer> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    //@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<Customer> findOneWithAuthoritiesByEmail(String login);

    //@EntityGraph(attributePaths = "authorities")
    //@Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    //Optional<User> findOneWithAuthoritiesByEmail(String email);

    //Page<User> findAllByLoginNot(Pageable pageable, String login);
}
