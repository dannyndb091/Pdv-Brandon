package com.pdv.Test.Repository;

import com.pdv.Test.Models.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentosRepository extends JpaRepository<Documentos,Integer> {


    Documentos findByDocIdClienteAndDocCompletedAndDocStatus(Integer clId, boolean b, boolean b1);
}
