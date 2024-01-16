package com.pdv.Test.Repository;

import com.pdv.Test.Models.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DocumentosRepository extends JpaRepository<Documentos,Integer> {


    Documentos findByDocIdClienteAndDocCompletedAndDocStatus(Integer clId, boolean b, boolean b1);

    int countByDocIdCliente(Integer Id);

    Integer countByDocDateAndDocType(LocalDate now, int i);

    List<Documentos> findAllByDocDateAndDocType(LocalDate arcDate, int i);

    Integer countByDocDateAndDocTypeAndDocCompleted(LocalDate arcDate, int i, boolean b);

    Integer countByDocIdClienteAndDocTypeAndDocCompletedAndDocStatus(Integer clId, int i, boolean b, boolean b1);

    Integer countByDocTypeAndDocCompleted(int i, boolean b);
}
