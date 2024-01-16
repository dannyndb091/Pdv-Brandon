package com.pdv.Test.Service;

import com.pdv.Test.Models.*;
import com.pdv.Test.Models.DTOs.Ventas.VtaAddItem;
import com.pdv.Test.Models.DTOs.Ventas.VtaCloseSell;
import com.pdv.Test.Models.DTOs.Ventas.VtaNewDoc;
import com.pdv.Test.Repository.*;
import com.pdv.Test.Service.Others.Verifiers;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VentasService {
    private final DocumentosRepository docRep;
    private final ClienteRepository cliRep;
    private final CortesRepository corRep;
    private final InventarioRepository invRep;
    private final MovimientosRepository movRep;
    private final PagosRepository payRep;
    private final ProductosRepository proRep;
    private final CortesService cortesService;
    private final Verifiers verifiers;

    @Transactional
    public ResponseEntity<Object> createSellDoc(VtaNewDoc vta) {
        return createSellDocTransaction(vta);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> createSellDocTransaction(VtaNewDoc vta) {
        try {
            cortesService.verifyArchOpen();

            if (vta.getClCode() == null) throw new RuntimeException("«ERR-V11»");
            ClienteProveedor cli = cliRep.findByClCode(vta.getClCode());
            if (cli == null) throw new RuntimeException("«ERR-V12»");
            if (!cli.getClStatus()) throw new RuntimeException("«ERR-V13»");
            if (!(cli.getClTypeClient() == 0 || cli.getClTypeClient() == 2)) throw new RuntimeException("«ERR-V14»");

            Integer openDocs = docRep.
                    countByDocIdClienteAndDocTypeAndDocCompletedAndDocStatus(cli.getClId(),2,false,true);
            if (openDocs > 0) throw new RuntimeException("«ERR-V15»");

            Documentos doc = docRep.save(Documentos.builder()
                    .docDate(LocalDate.now())
                    .docIdCliente(cli.getClId())
                    .docType(2)
                    .docCompleted(false)
                    .docStatus(true)
                    .docNet(BigDecimal.valueOf(0))
                    .docDiscount(BigDecimal.valueOf(0))
                    .docSubtotal(BigDecimal.valueOf(0))
                    .docTax(BigDecimal.valueOf(0))
                    .docTotal(BigDecimal.valueOf(0))
                    .docProductQty(0).build());

            return ResponseEntity.ok(VtaNewDoc.builder()
                    .Invoice(doc.getDocId())
                    .clCode(cli.getClCode()).build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> addSellItem(VtaAddItem addItem, boolean put) {
        return addSellItemTransaction(addItem,put);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> addSellItemTransaction(VtaAddItem addItem, boolean put) {
        try {
            cortesService.verifyArchOpen();

            verifyAddItem(addItem);

            if (put){
                if (addItem.getMovLine() == null || addItem.getMovLine() < 1) throw new RuntimeException("«ERR-C44»");
            }

            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-V16»");
            if (!doc.get().getDocDate().equals(LocalDate.now())) throw new RuntimeException("«ERR-V50»");
            if (doc.get().getDocType() != 2) throw new RuntimeException("«ERR-V19»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-V17»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-V18»");

            ClienteProveedor cli = cliRep.findByClCode(addItem.getClCode());
            if (cli == null) throw new RuntimeException("«ERR-V20»");
            if (!doc.get().getDocIdCliente().equals(cli.getClId())) throw new RuntimeException("«ERR-V21»");

            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-V22»");
            if (!pro.getProStatus()) throw new RuntimeException("«ERR-V23»");

            Movimientos proMovExist;
            Integer movId = null;

            if (put){
                proMovExist = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
                if (proMovExist == null) throw new RuntimeException("«ERR-V40»");
                if (!proMovExist.getMovProdId().equals(pro.getProId()) || !proMovExist.getMovLine().equals(addItem.getMovLine()))
                    throw new RuntimeException("«ERR-V68»");
                movId = proMovExist.getMovId();
            } else {
                proMovExist = movRep.findFirstByMovDocIdAndMovProdId(doc.get().getDocId(), pro.getProId());
                if (proMovExist != null) throw new RuntimeException("«ERR-V24»");

                Movimientos lastMov = movRep.findFirstByMovDocIdOrderByMovLineDesc(doc.get().getDocId());
                addItem.setMovLine((lastMov == null) ? 1 : lastMov.getMovLine() + 1);
            }

            Inventario inv = invRep.findByInvPro(pro.getProId());
            if (inv == null) throw new RuntimeException("«ERR-V25»");
            if (inv.getInvQty() < addItem.getProQty()) throw new RuntimeException("«ERR-V26»");

            if (pro.getProPrice().compareTo(addItem.getMovPU()) != 0) throw new RuntimeException("«ERR-V27»");
            BigDecimal maxDisc = pro.getProPrice().multiply(pro.getProMaxPercDisc());
            if (addItem.getMovDisc().compareTo(maxDisc) > 0) throw new RuntimeException("«ERR-V28»");

            if (addItem.getMovDisc().compareTo(addItem.getMovNet()) >= 0) throw new RuntimeException("«ERR-C31»");
            BigDecimal result = pro.getProPrice();
            if (addItem.getMovPU().compareTo(result) != 0) throw new RuntimeException("«ERR-V29»");
            result = result.multiply(BigDecimal.valueOf(addItem.getProQty()));
            if (addItem.getMovNet().compareTo(result) != 0) throw new RuntimeException("«ERR-V30»");
            result = result.subtract(addItem.getMovDisc());
            if (addItem.getMovSubtotal().compareTo(result) != 0) throw new RuntimeException("«ERR-V31»");
            BigDecimal tax = result.multiply(BigDecimal.valueOf(0.16));
            if (tax.compareTo(addItem.getMovTax()) != 0) throw new RuntimeException("«ERR-V32»");
            result = result.add(tax);
            if (result.compareTo(addItem.getMovTotal()) != 0) throw new RuntimeException("«ERR-V33»");

            if (put){
                inv.setInvQty(inv.getInvQty() + proMovExist.getMovQty() - addItem.getProQty());
                inv.setInvRes(inv.getInvRes() - proMovExist.getMovQty() + addItem.getProQty());

                doc.get().setDocProductQty(doc.get().getDocProductQty() - proMovExist.getMovQty() + addItem.getProQty());
                doc.get().setDocNet(doc.get().getDocNet().add(addItem.getMovNet()).subtract(proMovExist.getMovNet()));
                doc.get().setDocDiscount(doc.get().getDocDiscount().add(addItem.getMovDisc()).subtract(proMovExist.getMovDiscount()));
                doc.get().setDocSubtotal(doc.get().getDocSubtotal().add(addItem.getMovSubtotal()).subtract(proMovExist.getMovSubtotal()));
                doc.get().setDocTax(doc.get().getDocTax().add(addItem.getMovTax()).subtract(proMovExist.getMovTax()));
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()).subtract(proMovExist.getMovTotal()));
            } else {
                inv.setInvQty(inv.getInvQty() - addItem.getProQty());
                inv.setInvRes(inv.getInvRes() + addItem.getProQty());

                doc.get().setDocProductQty(doc.get().getDocProductQty() + addItem.getProQty());
                doc.get().setDocNet(doc.get().getDocNet().add(addItem.getMovNet()));
                doc.get().setDocDiscount(doc.get().getDocDiscount().add(addItem.getMovDisc()));
                doc.get().setDocSubtotal(doc.get().getDocSubtotal().add(addItem.getMovSubtotal()));
                doc.get().setDocTax(doc.get().getDocTax().add(addItem.getMovTax()));
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()));
            }

            invRep.save(inv);
            Movimientos toSave = Movimientos.builder()
                    .movId(movId)
                    .movDocId(doc.get().getDocId())
                    .movLine(addItem.getMovLine())
                    .movProdId(pro.getProId())
                    .movType(2)
                    .movQty(addItem.getProQty())
                    .movCompleted(false)
                    .movPU(addItem.getMovPU())
                    .movNet(addItem.getMovNet())
                    .movDiscount(addItem.getMovDisc())
                    .movSubtotal(addItem.getMovSubtotal())
                    .movTax(addItem.getMovTax())
                    .movTotal(addItem.getMovTotal()).build();

            movRep.save(toSave);

            docRep.save(doc.get());

            return ResponseEntity.ok(VtaAddItem.builder()
                    .invoice(doc.get().getDocId())
                    .proSKU(pro.getProSku())
                    .movLine(addItem.getMovLine())
                    .movPU(addItem.getMovPU())
                    .movNet(addItem.getMovNet())
                    .movDisc(addItem.getMovDisc())
                    .movSubtotal(addItem.getMovSubtotal())
                    .movTax(addItem.getMovTax())
                    .movTotal(addItem.getMovTotal()).build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    private void verifyAddItem(VtaAddItem addItem) {
        verifiers.verifyProQty(addItem.getProQty());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovPU());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovNet());
        verifiers.verifyBigDecimalData(addItem.getMovDisc());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovSubtotal());
        verifiers.verifyBigDecimalData(addItem.getMovTax());
        verifiers.verifyBigDecimalDataSuperiorCero(addItem.getMovTotal());
    }

    @Transactional
    public ResponseEntity<Object> delSellItem(VtaAddItem addItem) {
        return delSellItemTransaction(addItem);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> delSellItemTransaction(VtaAddItem addItem) {
        try {
            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-V36»");
            if (doc.get().getDocType() != 2) throw new RuntimeException("«ERR-V39»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-V37»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-V38»");

            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-V22»");

            Movimientos mov = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
            if (mov == null) throw new RuntimeException("«ERR-V40»");
            if (!mov.getMovProdId().equals(pro.getProId())) throw new RuntimeException("«ERR-V41»");
            if (!mov.getMovQty().equals(addItem.getProQty())) throw new RuntimeException("«ERR-V42»");

            Inventario inv = invRep.findByInvPro(pro.getProId());
            if (inv == null) throw new RuntimeException("«ERR-V25»");

            inv.setInvQty(inv.getInvQty() + mov.getMovQty());
            inv.setInvRes(inv.getInvRes() - mov.getMovQty());

            doc.get().setDocNet(doc.get().getDocNet().subtract(mov.getMovNet()));
            doc.get().setDocDiscount(doc.get().getDocDiscount().subtract(mov.getMovDiscount()));
            doc.get().setDocSubtotal(doc.get().getDocSubtotal().subtract(mov.getMovSubtotal()));
            doc.get().setDocTax(doc.get().getDocTax().subtract(mov.getMovTax()));
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
    public ResponseEntity<Object> closeSellDoc(VtaCloseSell closeSell) {
        return closeSellDocTransaction(closeSell);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> closeSellDocTransaction(VtaCloseSell closeSell) {
        try {
            cortesService.verifyArchOpen();

            verifiers.verifyCloseSellData(closeSell);

            Optional<Documentos> doc = docRep.findById(closeSell.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-V43»");
            if (doc.get().getDocType() != 2) throw new RuntimeException("«ERR-V39»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-V37»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-V38»");

            if (doc.get().getDocNet().compareTo(closeSell.getDocNet()) != 0
                    || doc.get().getDocDiscount().compareTo(closeSell.getDocDisc()) != 0
                    || doc.get().getDocSubtotal().compareTo(closeSell.getDocSubtotal()) != 0
                    || doc.get().getDocTax().compareTo(closeSell.getDocTax()) != 0
                    || doc.get().getDocTotal().compareTo(closeSell.getDocTotal()) != 0
                ) throw new RuntimeException("«ERR-V49»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-V44»");

            BigDecimal payed = closeSell.getPayCash().add(closeSell.getPayDigital());
            if (doc.get().getDocTotal().compareTo(payed) != 0) throw new RuntimeException("«ERR-V45»");

            Pagos pay = payRep.findByPayDoc(doc.get().getDocId());
            if (pay != null) throw new RuntimeException("«ERR-V46»");

            BigDecimal rNet = BigDecimal.valueOf(0);
            BigDecimal rDisc = BigDecimal.valueOf(0);
            BigDecimal rSubtotal = BigDecimal.valueOf(0);
            BigDecimal rTax = BigDecimal.valueOf(0);
            BigDecimal rTotal = BigDecimal.valueOf(0);

            for (Movimientos m : movs){
                if (m.getMovCompleted()) throw new RuntimeException("«ERR-V47»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-V25»");

                rNet = rNet.add(m.getMovNet());
                rDisc = rDisc.add(m.getMovDiscount());
                rSubtotal = rSubtotal.add(m.getMovSubtotal());
                rTax = rTax.add(m.getMovTax());
                rTotal = rTotal.add(m.getMovTotal());

                m.setMovCompleted(true);

                inv.setInvRes(inv.getInvRes() - m.getMovQty());
                inv.setInvSells(inv.getInvSells() + m.getMovQty());
                inv.setInvSellsValue(inv.getInvSellsValue().add(m.getMovSubtotal()));

                movRep.save(m);
                invRep.save(inv);
            }

            if (rNet.compareTo(doc.get().getDocNet()) != 0 ||
                    rDisc.compareTo(doc.get().getDocDiscount()) != 0 ||
                    rSubtotal.compareTo(doc.get().getDocSubtotal()) != 0 ||
                    rTax.compareTo(doc.get().getDocTax()) != 0 ||
                    rTotal.compareTo(doc.get().getDocTotal()) != 0
                ) throw new RuntimeException("«ERR-V48»");

            doc.get().setDocCompleted(true);

            Optional<Cortes> arch = corRep.findById(doc.get().getDocDate());
            if (arch.isEmpty()) throw new RuntimeException("«ERR-V52»");
            if (arch.get().getArcClosed()) throw new RuntimeException("«ERR-V53»");

            arch.get().setArcCash(arch.get().getArcCash().add(closeSell.getPayCash()));
            arch.get().setArcDigital(arch.get().getArcDigital().add(closeSell.getPayDigital()));

            corRep.save(arch.get());

            payRep.save(Pagos.builder()
                    .payDoc(doc.get().getDocId())
                    .payType(2)
                    .payStatus(true)
                    .payCash(closeSell.getPayCash())
                    .payDigital(closeSell.getPayDigital()).build());

            docRep.save(doc.get());

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> cancelSellDoc(VtaNewDoc docCancel) {
        return cancelSellDocTransaction(docCancel);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> cancelSellDocTransaction(VtaNewDoc docCancel) {
        try{
            Optional<Documentos> doc = docRep.findById(docCancel.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-V54»");
            if (doc.get().getDocType() != 2) throw new RuntimeException("«ERR-V58»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-V60»");
            if (!doc.get().getDocCompleted()) throw new RuntimeException("«ERR-V59»");
            if (!doc.get().getDocDate().equals(LocalDate.now())) throw new RuntimeException("«ERR-V65»");

            Pagos pay = payRep.findByPayDoc(doc.get().getDocId());
            if (pay == null) throw new RuntimeException("«ERR-V61»");
            if (!pay.getPayStatus()) throw new RuntimeException("«ERR-V62»");

            Optional<Cortes> arch = corRep.findById(doc.get().getDocDate());
            if (arch.isEmpty()) throw new RuntimeException("«ERR-V66»");
            if (arch.get().getArcClosed()) throw new RuntimeException("«ERR-V67»");

            arch.get().setArcCash(arch.get().getArcCash().subtract(pay.getPayCash()));
            arch.get().setArcDigital(arch.get().getArcDigital().subtract(pay.getPayDigital()));

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-V63»");

            for (Movimientos m : movs){
                if (!m.getMovCompleted()) throw new RuntimeException("«ERR-V64»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-V25»");

                m.setMovCompleted(false);

                inv.setInvSells(inv.getInvSells() - m.getMovQty());
                inv.setInvSellsValue(inv.getInvSellsValue().subtract(m.getMovSubtotal()));
                inv.setInvQty(inv.getInvQty() + m.getMovQty());

                movRep.save(m);
                invRep.save(inv);
            }

            corRep.save(arch.get());

            doc.get().setDocStatus(false);
            pay.setPayStatus(false);

            docRep.save(doc.get());
            payRep.save(pay);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteSellDoc(VtaNewDoc docDelete) {
        return deleteSellDocTransaction(docDelete);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> deleteSellDocTransaction(VtaNewDoc docDelete) {
        try {
            Optional<Documentos> doc = docRep.findById(docDelete.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-V54»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-V55»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-V56»");
            if (doc.get().getDocType() != 2) throw new RuntimeException("«ERR-V57»");

            ClienteProveedor prov = cliRep.findByClCode(docDelete.getClCode());
            if (prov == null) throw new RuntimeException("«ERR-V12»");
            if (!prov.getClId().equals(doc.get().getDocIdCliente())) throw new RuntimeException("«ERR-V21»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());

            for (Movimientos m : movs){
                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-V25»");

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
