package com.pdv.Test.Repository;

import com.pdv.Test.Models.Cortes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CortesRepository extends JpaRepository<Cortes, LocalDate> {
    Integer countByArcClosed(boolean b);

    Integer countByArcDate(LocalDate now);

    Cortes findByArcClosed(boolean b);
}
