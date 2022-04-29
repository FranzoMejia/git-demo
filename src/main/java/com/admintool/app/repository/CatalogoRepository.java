package com.admintool.app.repository;

import com.admintool.app.domain.Catalogo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Catalogo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {}
