package com.pdv.Test.Repository;

import com.pdv.Test.Models.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository<Inventario,Integer> {
    Inventario findByInvPro(Integer proId);
}
