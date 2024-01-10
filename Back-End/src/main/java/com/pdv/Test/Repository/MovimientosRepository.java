package com.pdv.Test.Repository;

import com.pdv.Test.Models.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientosRepository extends JpaRepository<Movimientos,Integer> {
}
