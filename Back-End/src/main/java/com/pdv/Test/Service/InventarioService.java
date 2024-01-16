package com.pdv.Test.Service;

import com.pdv.Test.Models.*;
import com.pdv.Test.Models.DTOs.Inventario.DcrAddItem;
import com.pdv.Test.Models.DTOs.Inventario.DcrCloseDoc;
import com.pdv.Test.Models.DTOs.Inventario.DcrNewDoc;
import com.pdv.Test.Repository.DocumentosRepository;
import com.pdv.Test.Repository.InventarioRepository;
import com.pdv.Test.Repository.MovimientosRepository;
import com.pdv.Test.Repository.ProductosRepository;
import com.pdv.Test.Service.Others.Verifiers;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final DocumentosRepository docRep;
    private final InventarioRepository invRep;
    private final ProductosRepository proRep;
    private final MovimientosRepository movRep;
    private final Verifiers verifiers;

    @Transactional
    public ResponseEntity<Object> createDecrease() {
        return createDecreaseTransaction();
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> createDecreaseTransaction() {
        try {
            Integer openDocs = docRep.countByDocTypeAndDocCompleted(3,false);
            if (openDocs > 0) throw new RuntimeException("«ERR-I01»");

            Documentos doc = docRep.save(Documentos.builder()
                    .docDate(LocalDate.now())
                    .docIdCliente(-1)
                    .docType(3)
                    .docCompleted(false)
                    .docStatus(true)
                    .docNet(BigDecimal.valueOf(0))
                    .docDiscount(BigDecimal.valueOf(0))
                    .docSubtotal(BigDecimal.valueOf(0))
                    .docTax(BigDecimal.valueOf(0))
                    .docTotal(BigDecimal.valueOf(0))
                    .docProductQty(0).build());

            return ResponseEntity.ok(DcrNewDoc.builder()
                    .Invoice(doc.getDocId()).build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> addItem(DcrAddItem addItem, boolean put) {
        return addItemTransaction(addItem,put);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> addItemTransaction(DcrAddItem addItem, boolean put) {
        try {
            verifyAddItem(addItem);

            if (put){
                if (addItem.getMovLine() == null || addItem.getMovLine() < 1) throw new RuntimeException("«ERR-I02»");
            }

            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-I03»");
            if (!doc.get().getDocDate().equals(LocalDate.now())) throw new RuntimeException("«ERR-I04»");
            if (doc.get().getDocType() != 3) throw new RuntimeException("«ERR-I05»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-I06»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-I07»");

            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-I08»");
            if (!pro.getProStatus()) throw new RuntimeException("«ERR-I09»");

            Movimientos proMovExist;
            Integer movId = null;

            if (put){
                proMovExist = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
                if (proMovExist == null) throw new RuntimeException("«ERR-I10»");
                if (!proMovExist.getMovProdId().equals(pro.getProId()) || !proMovExist.getMovLine().equals(addItem.getMovLine()))
                    throw new RuntimeException("«ERR-I11»");
                movId = proMovExist.getMovId();
            } else {
                proMovExist = movRep.findFirstByMovDocIdAndMovProdId(doc.get().getDocId(), pro.getProId());
                if (proMovExist != null) throw new RuntimeException("«ERR-I12»");

                Movimientos lastMov = movRep.findFirstByMovDocIdOrderByMovLineDesc(doc.get().getDocId());
                addItem.setMovLine((lastMov == null) ? 1 : lastMov.getMovLine() + 1);
            }

            Inventario inv = invRep.findByInvPro(pro.getProId());
            if (inv == null) throw new RuntimeException("«ERR-I13»");
            if (inv.getInvQty() < addItem.getProQty()) throw new RuntimeException("«ERR-I14»");

            BigDecimal costPU = inv.getInvBuysValue().
                    divide(BigDecimal.valueOf(inv.getInvBuys()),2, java.math.RoundingMode.HALF_UP);

            if (costPU.compareTo(addItem.getMovPU()) != 0) throw new RuntimeException("«ERR-I30»");

            BigDecimal result = costPU;
            result = result.multiply(BigDecimal.valueOf(addItem.getProQty()));
            if (result.compareTo(addItem.getMovTotal()) != 0) throw new RuntimeException("«ERR-I15»");

            if (put){
                inv.setInvQty(inv.getInvQty() + proMovExist.getMovQty() - addItem.getProQty());
                inv.setInvRes(inv.getInvRes() - proMovExist.getMovQty() + addItem.getProQty());

                doc.get().setDocProductQty(doc.get().getDocProductQty() - proMovExist.getMovQty() + addItem.getProQty());
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()).subtract(proMovExist.getMovTotal()));
            } else {
                inv.setInvQty(inv.getInvQty() - addItem.getProQty());
                inv.setInvRes(inv.getInvRes() + addItem.getProQty());

                doc.get().setDocProductQty(doc.get().getDocProductQty() + addItem.getProQty());
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()));
            }

            invRep.save(inv);
            Movimientos toSave = Movimientos.builder()
                    .movId(movId)
                    .movDocId(doc.get().getDocId())
                    .movLine(addItem.getMovLine())
                    .movProdId(pro.getProId())
                    .movType(3)
                    .movQty(addItem.getProQty())
                    .movCompleted(false)
                    .movPU(addItem.getMovPU())
                    .movNet(BigDecimal.valueOf(0))
                    .movDiscount(BigDecimal.valueOf(0))
                    .movSubtotal(BigDecimal.valueOf(0))
                    .movTax(BigDecimal.valueOf(0))
                    .movTotal(addItem.getMovTotal()).build();

            movRep.save(toSave);

            docRep.save(doc.get());

            return ResponseEntity.ok(DcrAddItem.builder()
                    .invoice(doc.get().getDocId())
                    .proSKU(pro.getProSku())
                    .movLine(addItem.getMovLine())
                    .movPU(addItem.getMovPU())
                    .movTotal(addItem.getMovTotal()).build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    private void verifyAddItem(DcrAddItem addItem) {
        verifiers.verifyProQty(addItem.getProQty());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovPU());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovTotal());
    }

    @Transactional
    public ResponseEntity<Object> delItem(DcrAddItem addItem) {
        return delItemTransaction(addItem);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> delItemTransaction(DcrAddItem addItem) {
        try {
            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-I03»");
            if (doc.get().getDocType() != 3) throw new RuntimeException("«ERR-I05»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-I06»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-I07»");

            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-I16»");

            Movimientos mov = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
            if (mov == null) throw new RuntimeException("«ERR-I10»");
            if (!mov.getMovProdId().equals(pro.getProId())) throw new RuntimeException("«ERR-I11»");
            if (!mov.getMovQty().equals(addItem.getProQty())) throw new RuntimeException("«ERR-I17»");

            Inventario inv = invRep.findByInvPro(pro.getProId());
            if (inv == null) throw new RuntimeException("«ERR-I13»");

            inv.setInvQty(inv.getInvQty() + mov.getMovQty());
            inv.setInvRes(inv.getInvRes() - mov.getMovQty());

            doc.get().setDocTotal(doc.get().getDocTotal().subtract(mov.getMovTotal()));
            doc.get().setDocProductQty(doc.get().getDocProductQty() - mov.getMovQty());

            invRep.save(inv);
            docRep.save(doc.get());
            movRep.delete(mov);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> closeDecrease(DcrCloseDoc dcr) {
        return closeDecreaseTransaction(dcr);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> closeDecreaseTransaction(DcrCloseDoc dcr) {
        try {
            verifiers.verifyCloseDecrease(dcr);

            Optional<Documentos> doc = docRep.findById(dcr.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-I03»");
            if (doc.get().getDocType() != 3) throw new RuntimeException("«ERR-I05»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-I19»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-I20»");

            if (doc.get().getDocTotal().compareTo(dcr.getDocTotal()) != 0) throw new RuntimeException("«ERR-I21»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-I22»");

            BigDecimal rTotal = BigDecimal.valueOf(0);

            for (Movimientos m : movs){
                if (m.getMovCompleted()) throw new RuntimeException("«ERR-I23»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-I24»");

                rTotal = rTotal.add(m.getMovTotal());

                m.setMovCompleted(true);

                inv.setInvRes(inv.getInvRes() - m.getMovQty());
                inv.setInvDcr(inv.getInvDcr() + m.getMovQty());
                inv.setInvDcrValue(inv.getInvDcrValue().add(m.getMovTotal()));

                movRep.save(m);
                invRep.save(inv);
            }

            if (rTotal.compareTo(doc.get().getDocTotal()) != 0) throw new RuntimeException("«ERR-I25»");

            doc.get().setDocCompleted(true);

            docRep.save(doc.get());

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> cancelDecrease(DcrNewDoc dcr) {
        return cancelDecreaseTransaction(dcr);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> cancelDecreaseTransaction(DcrNewDoc dcr) {
        try{
            Optional<Documentos> doc = docRep.findById(dcr.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-I03»");
            if (doc.get().getDocType() != 3) throw new RuntimeException("«ERR-I05»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-I26»");
            if (!doc.get().getDocCompleted()) throw new RuntimeException("«ERR-I27»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-I28»");

            for (Movimientos m : movs){
                if (!m.getMovCompleted()) throw new RuntimeException("«ERR-I29»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-I13»");

                m.setMovCompleted(false);

                inv.setInvDcr(inv.getInvDcr() - m.getMovQty());
                inv.setInvDcrValue(inv.getInvDcrValue().subtract(m.getMovTotal()));
                inv.setInvQty(inv.getInvQty() + m.getMovQty());

                movRep.save(m);
                invRep.save(inv);
            }

            doc.get().setDocStatus(false);

            docRep.save(doc.get());

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteDecrease(DcrNewDoc dcr) {
        return deleteDecreaseTransaction(dcr);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> deleteDecreaseTransaction(DcrNewDoc dcr) {
        try {
            Optional<Documentos> doc = docRep.findById(dcr.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-I03»");
            if (doc.get().getDocType() != 3) throw new RuntimeException("«ERR-I05»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-I06»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-I07»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());

            for (Movimientos m : movs){
                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-I13»");

                inv.setInvRes(inv.getInvRes() - m.getMovQty());
                inv.setInvQty(inv.getInvQty() + m.getMovQty());

                invRep.save(inv);
                movRep.delete(m);
            }

            docRep.delete(doc.get());

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }
}
