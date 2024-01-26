package com.pdv.Test.Repository;

import com.pdv.Test.Models.ClienteProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteProveedor,Integer> {
    ClienteProveedor findByClCode(String clCode);

    ClienteProveedor findByClMail(String clMail);

    ClienteProveedor findByClCellphone(String clCellphone);

    List<ClienteProveedor> findAllByClStatusAndClTypeClientNotOrderByClNameAsc(boolean b, int i);
}
