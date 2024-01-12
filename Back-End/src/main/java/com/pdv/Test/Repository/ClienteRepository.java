package com.pdv.Test.Repository;

import com.pdv.Test.Models.ClienteProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteProveedor,Integer> {
    ClienteProveedor findByClCode(String clCode);

    ClienteProveedor findByClMail(String clMail);

    ClienteProveedor findByClCellphone(String clCellphone);
}
