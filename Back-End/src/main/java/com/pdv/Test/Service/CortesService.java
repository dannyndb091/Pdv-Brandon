package com.pdv.Test.Service;

import com.pdv.Test.Models.Cortes;
import com.pdv.Test.Models.DTOs.Cortes.CortesApertura;
import com.pdv.Test.Models.Documentos;
import com.pdv.Test.Repository.CortesRepository;
import com.pdv.Test.Repository.DocumentosRepository;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CortesService {
    private final CortesRepository corRep;
    private final DocumentosRepository docRep;

    @Transactional
    public ResponseEntity<Object> createArch(CortesApertura apert) {
        return createArchTransaction(apert);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> createArchTransaction(CortesApertura apert) {
        try {
            if (BigDecimal.valueOf(0).compareTo(apert.getArcApertureCash()) > 0)
                throw new RuntimeException("«ERR-V04»");

            Integer archOpen = corRep.countByArcClosed(false);
            if (archOpen > 0) throw new RuntimeException("«ERR-V01»");

            Integer archToday = corRep.countByArcDate(LocalDate.now());
            if (archToday > 0) throw new RuntimeException("«ERR-V02»");

            Integer sellsToday = docRep.countByDocDateAndDocType(LocalDate.now(), 2);
            if (sellsToday > 0) throw new RuntimeException("«ERR-V03»");

            corRep.save(Cortes.builder()
                    .arcDate(LocalDate.now())
                    .arcClosed(false)
                    .arcCash(BigDecimal.valueOf(0))
                    .arcDigital(BigDecimal.valueOf(0))
                    .arcApertureCash(apert.getArcApertureCash()).build());

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> closeArch() {
        return closeArchTransaction();
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> closeArchTransaction() {
        try {
            Integer archOpened = corRep.countByArcClosed(false);
            if (archOpened == 0) throw new RuntimeException("«ERR-V05»");
            if (archOpened > 1) throw new RuntimeException("«ERR-V06»");

            Cortes arch = corRep.findByArcClosed(false);

            Integer docsOpen = docRep.countByDocDateAndDocTypeAndDocCompleted(arch.getArcDate(), 2, false);
            if (docsOpen > 0) throw new RuntimeException("«ERR-V07»");

            BigDecimal totalArch = arch.getArcCash().add(arch.getArcDigital());
            BigDecimal docsSum = BigDecimal.valueOf(0);

            List<Documentos> docs = docRep.findAllByDocDateAndDocTypeAndDocCompletedAndDocStatus(arch.getArcDate(),2,true,true);

            for (Documentos doc : docs){
                docsSum = docsSum.add(doc.getDocTotal());
            }

            if (totalArch.compareTo(docsSum) != 0) throw new RuntimeException("«ERR-V08»");

            arch.setArcClosed(true);

            corRep.save(arch);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    public void verifyArchOpen(){
        try {
            Cortes arch = corRep.findByArcClosed(false);
            if (arch == null) throw new RuntimeException("«ERR-V09»");
            if (!arch.getArcDate().equals(LocalDate.now())) throw new RuntimeException("«ERR-V10»");
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
