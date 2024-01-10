package com.pdv.Test.Repository;

import com.pdv.Test.Models.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosRepository extends JpaRepository<Productos,Integer> {
    Productos findByProSku(String proSKU);
}
