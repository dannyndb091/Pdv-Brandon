package com.pdv.Test.Repository;

import com.pdv.Test.Models.Movimientos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientosRepository extends JpaRepository<Movimientos,Integer> {

    Movimientos findFirstByMovDocIdOrderByMovLineDesc(Integer docId);

    Movimientos findFirstByMovDocIdAndMovProdId(Integer docId, Integer proId);

    Movimientos findByMovDocIdAndMovLine(Integer invoice, Integer movLine);

    List<Movimientos> findAllByMovDocIdOrderByMovLineAsc(Integer docId);

    Integer countByMovProdId(Integer proId);

    Integer countByMovProdIdAndMovDocId(Integer proId, Integer docId);
}
