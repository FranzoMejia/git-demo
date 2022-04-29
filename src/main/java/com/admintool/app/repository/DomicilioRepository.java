package com.admintool.app.repository;

import com.admintool.app.domain.Domicilio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Domicilio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {}
