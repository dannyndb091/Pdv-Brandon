package com.pdv.Test.Service;

import com.pdv.Test.Models.*;
import com.pdv.Test.Models.DTOs.Compras.CprAddItem;
import com.pdv.Test.Models.DTOs.Compras.CprCloseBuy;
import com.pdv.Test.Models.DTOs.Compras.CprNewDoc;
import com.pdv.Test.Repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ComprasService {
    private final DocumentosRepository docRep;
    private final ClienteRepository cliRep;
    private final InventarioRepository invRep;
    private final MovimientosRepository movRep;
    private final ProductosRepository proRep;
    private final PagosRepository payRep;

    @Transactional
    public ResponseEntity<Object> createBuyDoc(CprNewDoc cprNewDoc) {
        return creatoBuyDocTransaction(cprNewDoc);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> creatoBuyDocTransaction(CprNewDoc cprNewDoc) {
        try {
            if (cprNewDoc.getDate() == null) throw new RuntimeException("«ERR-C04»"); //Sin fecha para el documento.
            if (cprNewDoc.getClCode() == null) throw new RuntimeException("«ERR-C05»"); //Sin codigo de Provedor.
            if (!cprNewDoc.getDate().equals(LocalDate.now())) throw new RuntimeException("«ERR-C00»"); //No se pueden crear compras de otros dias.
            ClienteProveedor prov = cliRep.findByClCode(cprNewDoc.getClCode());
            if (prov == null) throw new RuntimeException("«ERR-C01»"); //No Existe el Provedor.
            if (!prov.getClStatus()) throw new RuntimeException("«ERR-C39»"); //No se encuentra activo el provedor.
            if (!Set.of(1,2).contains(prov.getClTypeClient())) throw new RuntimeException("«ERR-C03»"); //No es proveedor.
            Documentos openDoc = docRep.findByDocIdClienteAndDocCompletedAndDocStatus(
                    prov.getClId(),false,true);
            if (openDoc != null) throw new RuntimeException("«ERR-C30»");

            Documentos doc = docRep.save(Documentos.builder()
                    .docDate(LocalDate.now())
                    .docIdCliente(prov.getClId())
                    .docType(1)
                    .docCompleted(false)
                    .docStatus(true)
                    .docNet(new BigDecimal("0"))
                    .docDiscount(new BigDecimal("0"))
                    .docTax(new BigDecimal("0"))
                    .docSubtotal(new BigDecimal("0"))
                    .docTotal(new BigDecimal("0"))
                    .docProductQty(0).build());
            docRep.flush();

            return ResponseEntity.ok(CprNewDoc.builder()
                    .failed(false)
                    .openDoc(false)
                    .clCode(prov.getClCode())
                    .date(doc.getDocDate())
                    .invoice(doc.getDocId())
                    .exceptionMessage("")
                    .clName(prov.getClName()).build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> addItem(CprAddItem addItem, boolean put) {
        return addItemTransaction(addItem, put);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> addItemTransaction(CprAddItem addItem, boolean put) {
        try {
            if (put){
                if (addItem.getMovLine() == null || addItem.getMovLine() < 1) throw new RuntimeException("«ERR-C44»");
            }

            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C06»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C07»");
            if (doc.get().getDocType() != 1) throw new RuntimeException("«ERR-C08»");

            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-C09»");
            if (!pro.getProStatus()) throw new RuntimeException("«ERR-C10»");
            if (pro.getProType() == 0) throw new RuntimeException("«ERR-C14»");

            Inventario inv = invRep.findByInvPro(pro.getProId());
            if (inv == null) inv = createInv(pro.getProId());

            Movimientos proMovExist;
            Integer movId = null;

            if (put){
                proMovExist = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
                if (proMovExist == null) throw new RuntimeException("«ERR-C45»");
                if (!proMovExist.getMovProdId().equals(pro.getProId()) || !proMovExist.getMovLine().equals(addItem.getMovLine()))
                    throw new RuntimeException("«ERR-C46»");
                movId = proMovExist.getMovId();
            } else {
                proMovExist = movRep.findFirstByMovDocIdAndMovProdId(doc.get().getDocId(), pro.getProId());
                if (proMovExist != null) throw new RuntimeException("«ERR-C16»");
            }

            if (!put){
                Movimientos lastMov = movRep.findFirstByMovDocIdOrderByMovLineDesc(doc.get().getDocId());
                addItem.setMovLine((lastMov == null) ? 1 : lastMov.getMovLine() + 1);
            }

            if (addItem.getMovDisc().compareTo(addItem.getMovNet()) >= 0) throw new RuntimeException("«ERR-C31»");
            BigDecimal result = addItem.getMovPU().multiply((BigDecimal.valueOf(addItem.getProQty())));
            if (result.compareTo(addItem.getMovNet()) != 0) throw new RuntimeException("«ERR-C15»");
            result = result.subtract(addItem.getMovDisc());
            if (result.compareTo(addItem.getMovSubtotal()) != 0) throw new RuntimeException("«ERR-C15»");
            BigDecimal taxes = result.multiply(BigDecimal.valueOf(0.16));
            if (addItem.getMovTax().compareTo(taxes) != 0) throw new RuntimeException("«ERR-C15»");
            result = result.add(taxes);
            if (result.compareTo(addItem.getMovTotal()) != 0) throw new RuntimeException("«ERR-C15»");

            if (put){
                inv.setInvInc(inv.getInvInc() - proMovExist.getMovQty() + addItem.getProQty());
                doc.get().setDocNet(doc.get().getDocNet().add(addItem.getMovNet()).subtract(proMovExist.getMovNet()));
                doc.get().setDocDiscount(doc.get().getDocDiscount().add(addItem.getMovDisc()).subtract(proMovExist.getMovDiscount()));
                doc.get().setDocSubtotal(doc.get().getDocSubtotal().add(addItem.getMovSubtotal()).subtract(proMovExist.getMovSubtotal()));
                doc.get().setDocTax(doc.get().getDocTax().add(addItem.getMovTax()).subtract(proMovExist.getMovTax()));
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()).subtract(proMovExist.getMovTotal()));
                doc.get().setDocProductQty(doc.get().getDocProductQty() + addItem.getProQty() - proMovExist.getMovQty());
            }
            else {
                inv.setInvInc(inv.getInvInc() + addItem.getProQty());
                doc.get().setDocNet(doc.get().getDocNet().add(addItem.getMovNet()));
                doc.get().setDocDiscount(doc.get().getDocDiscount().add(addItem.getMovDisc()));
                doc.get().setDocSubtotal(doc.get().getDocSubtotal().add(addItem.getMovSubtotal()));
                doc.get().setDocTax(doc.get().getDocTax().add(addItem.getMovTax()));
                doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()));
                doc.get().setDocProductQty(doc.get().getDocProductQty() + addItem.getProQty());
            }

            invRep.save(inv);

            Movimientos toSave = Movimientos.builder()
                    .movId(movId)
                    .movDocId(doc.get().getDocId())
                    .movProdId(pro.getProId())
                    .movLine(addItem.getMovLine())
                    .movType(1)
                    .movQty(addItem.getProQty())
                    .movCompleted(false)
                    .movPU(addItem.getMovPU())
                    .movNet(addItem.getMovNet())
                    .movDiscount(addItem.getMovDisc())
                    .movSubtotal(addItem.getMovSubtotal())
                    .movTax(addItem.getMovTax())
                    .movTotal(addItem.getMovTotal())
                    .build();

            if (put){
                Movimientos getMovId = movRep.findByMovDocIdAndMovLine(doc.get().getDocId(), addItem.getMovLine());
                if (getMovId == null) throw new RuntimeException("«ERR-C45»");
                toSave.setMovId(getMovId.getMovId());
            }

            movRep.save(toSave);

            docRep.save(doc.get());

            return ResponseEntity.ok(CprAddItem.builder()
                    .invoice(doc.get().getDocId())
                    .proQty(inv.getInvInc())
                    .movLine(addItem.getMovLine())
                    .docNet(doc.get().getDocNet())
                    .docDisc(doc.get().getDocDiscount())
                    .docSubtotal(doc.get().getDocSubtotal())
                    .docTax(doc.get().getDocTax())
                    .docTotal(doc.get().getDocTotal())
                    .build());
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private Inventario createInv(Integer proId) {
        try {
            return invRep.save(Inventario.builder()
                    .invBuys(0)
                    .invSells(0)
                    .invQty(0)
                    .invRes(0)
                    .invInc(0)
                    .invBuysValue(BigDecimal.valueOf(0))
                    .invSellsValue(BigDecimal.valueOf(0))
                    .invPro(proId)
                    .build());
        } catch (Exception e){
            throw new HibernateException("«ERR-C13»");
        }
    }

    @Transactional
    public ResponseEntity<Object> delItem(CprAddItem addItem) {
        return delItemTransaction(addItem);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> delItemTransaction(CprAddItem addItem) {
        try{
            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C17»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C22»");
            if (doc.get().getDocType() != 1) throw new RuntimeException("«ERR-C18»");

            Movimientos mov = movRep.findByMovDocIdAndMovLine(
                    addItem.getInvoice(),
                    addItem.getMovLine()
            );
            if (mov == null) throw new RuntimeException("«ERR-C19»");
            if (mov.getMovCompleted()) throw new RuntimeException("«ERR-C20»");

            Inventario inv = invRep.findByInvPro(mov.getMovProdId());
            if (inv == null) throw new RuntimeException("«ERR-C21»");

            doc.get().setDocNet(doc.get().getDocNet().subtract(mov.getMovNet()));
            doc.get().setDocDiscount(doc.get().getDocDiscount().subtract(mov.getMovDiscount()));
            doc.get().setDocSubtotal(doc.get().getDocSubtotal().subtract(mov.getMovSubtotal()));
            doc.get().setDocTax(doc.get().getDocTax().subtract(mov.getMovTax()));
            doc.get().setDocTotal(doc.get().getDocTotal().subtract(mov.getMovTotal()));
            doc.get().setDocProductQty(doc.get().getDocProductQty() - mov.getMovQty());

            inv.setInvInc(inv.getInvInc()- mov.getMovQty());

            docRep.save(doc.get());
            invRep.save(inv);
            movRep.delete(mov);

            return ResponseEntity.ok(
                    CprAddItem.builder()
                            .invoice(doc.get().getDocId())
                            .docNet(doc.get().getDocNet())
                            .docDisc(doc.get().getDocDiscount())
                            .docSubtotal(doc.get().getDocSubtotal())
                            .docTax(doc.get().getDocTax())
                            .docTotal(doc.get().getDocTotal())
                            .proQty(doc.get().getDocProductQty()).build()
            );
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> closeBuy(CprCloseBuy closeBuy) {
        return closeBuyTransaction(closeBuy);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> closeBuyTransaction(CprCloseBuy closeBuy) {
        try {
            Optional<Documentos> doc = docRep.findById(closeBuy.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C23»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C24»");
            if (doc.get().getDocType() != 1) throw new RuntimeException("«ERR-C25»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-C27»");

            Pagos pay = payRep.findByPayDoc(doc.get().getDocId());
            if (pay != null) throw new RuntimeException("«ERR-C28»");

            BigDecimal payments = closeBuy.getPayCash().add(closeBuy.getPayDigital());
            if (doc.get().getDocTotal().compareTo(payments) != 0) throw new RuntimeException("«ERR-C26»");

            if (
                    doc.get().getDocNet().compareTo(closeBuy.getDocNet()) != 0
                            || doc.get().getDocDiscount().compareTo(closeBuy.getDocDisc()) != 0
                            || doc.get().getDocSubtotal().compareTo(closeBuy.getDocSubtotal()) != 0
                            || doc.get().getDocTax().compareTo(closeBuy.getDocTax()) != 0
                            || doc.get().getDocTotal().compareTo(closeBuy.getDocTotal()) != 0
            ) throw new RuntimeException("«ERR-C15»");

            BigDecimal finalNet = new BigDecimal(0);
            BigDecimal finalDiscount = new BigDecimal(0);
            BigDecimal finalSubtotal = new BigDecimal(0);
            BigDecimal finalTax = new BigDecimal(0);
            BigDecimal finalTotal = new BigDecimal(0);

            for (Movimientos m : movs){
                if (m.getMovCompleted()) throw new RuntimeException("«ERR-C20»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-C21»");

                m.setMovCompleted(true);

                inv.setInvQty(inv.getInvQty() + m.getMovQty());
                inv.setInvInc(inv.getInvInc() - m.getMovQty());
                inv.setInvBuys(inv.getInvBuys() + m.getMovQty());
                inv.setInvBuysValue(inv.getInvBuysValue().add(m.getMovSubtotal()));

                finalNet = finalNet.add(m.getMovNet());
                finalDiscount = finalDiscount.add(m.getMovDiscount());
                finalSubtotal = finalSubtotal.add(m.getMovSubtotal());
                finalTax = finalTax.add(m.getMovTax());
                finalTotal = finalTotal.add(m.getMovTotal());

                movRep.save(m);
                invRep.save(inv);
            }

            if (
                    finalNet.compareTo(doc.get().getDocNet()) != 0
                    || finalDiscount.compareTo(doc.get().getDocDiscount()) != 0
                    || finalSubtotal.compareTo(doc.get().getDocSubtotal()) != 0
                    || finalTax.compareTo(doc.get().getDocTax()) != 0
                    || finalTotal.compareTo(doc.get().getDocTotal()) != 0
            ) throw new RuntimeException("«ERR-C29»");

            doc.get().setDocCompleted(true);
            docRep.save(doc.get());

            payRep.save(Pagos.builder()
                .payCash(closeBuy.getPayCash())
                .payDigital(closeBuy.getPayDigital())
                .payType(1)
                .payDoc(doc.get().getDocId())
                .payStatus(true).build());

            return ResponseEntity.ok("");

        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> cancelBuy(CprAddItem addItem) {
        return cancelBuyTransaction(addItem);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> cancelBuyTransaction(CprAddItem addItem) {
        try{
            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (doc.get().getDocType() != 1) throw new RuntimeException("«ERR-C32»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C33»");
            if (!doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C33»");

            Pagos pay = payRep.findByPayDoc(doc.get().getDocId());
            if (pay == null) throw new RuntimeException("«ERR-C35»");
            if (!pay.getPayStatus()) throw new RuntimeException("«ERR-C34»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());
            if (movs.isEmpty()) throw new RuntimeException("«ERR-C36»");

            for (Movimientos m : movs){
                if (!m.getMovCompleted()) throw new RuntimeException("«ERR-C37»");

                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-C21»");
                if (inv.getInvQty() < m.getMovQty()) throw new RuntimeException("«ERR-C38»");

                m.setMovCompleted(false);

                inv.setInvBuys(inv.getInvBuys() - m.getMovQty());
                inv.setInvBuysValue(inv.getInvBuysValue().subtract(m.getMovSubtotal()));
                inv.setInvQty(inv.getInvQty() - m.getMovQty());

                movRep.save(m);
                invRep.save(inv);
            }

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
    public ResponseEntity<Object> delBuy(CprNewDoc buyDoc) {
        return delBuyTransaction(buyDoc);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> delBuyTransaction(CprNewDoc buyDoc) {
        try {
            Optional<Documentos> doc = docRep.findById(buyDoc.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C40»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C41»");

            ClienteProveedor prov = cliRep.findByClCode(buyDoc.getClCode());
            if (prov == null) throw new RuntimeException("«ERR-C01»");
            if (!prov.getClId().equals(doc.get().getDocIdCliente())) throw new RuntimeException("«ERR-C42»");

            List<Movimientos> movs = movRep.findAllByMovDocIdOrderByMovLineAsc(doc.get().getDocId());

            for (Movimientos m : movs){
                Inventario inv = invRep.findByInvPro(m.getMovProdId());
                if (inv == null) throw new RuntimeException("«ERR-C43»");

                inv.setInvInc(inv.getInvInc() - m.getMovQty());

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
