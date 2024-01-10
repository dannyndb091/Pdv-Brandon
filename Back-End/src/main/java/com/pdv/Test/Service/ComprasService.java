package com.pdv.Test.Service;

import com.pdv.Test.Models.*;
import com.pdv.Test.Models.DTOs.Compras.CprAddItem;
import com.pdv.Test.Models.DTOs.Compras.CprNewDoc;
import com.pdv.Test.Repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
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
            if (!prov.getClStatus()) throw new RuntimeException("«ERR-C02»"); //No se encuentra activo el provedor.
            if (!Set.of(1,2).contains(prov.getClTypeClient())) throw new RuntimeException("«ERR-C03»"); //No es proveedor.
            Documentos openDoc = docRep.findByDocIdClienteAndDocCompletedAndDocStatus(
                    prov.getClId(),false,true);
            if (openDoc != null) throw new RuntimeException("«ERR-C02»");

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

    public ResponseEntity<Object> getOpenDoc(CprNewDoc cprNewDoc) {
        try{
            ClienteProveedor prov = cliRep.findByClCode(cprNewDoc.getClCode());
            Documentos openDoc = docRep.findByDocIdClienteAndDocCompletedAndDocStatus(
                    prov.getClId(),false,true);

            return ResponseEntity.badRequest().body(CprNewDoc.builder()
                    .failed(false)
                    .openDoc(true)
                    .clCode(prov.getClCode())
                    .date(openDoc.getDocDate())
                    .invoice(openDoc.getDocId())
                    .exceptionMessage("El proveedor cuenta con un documento en proceso, " +
                            "favor de cerrarlo antes de generar uno nuevo")
                    .clName(prov.getClName()).build());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(CprNewDoc.builder()
                    .failed(true)
                    .openDoc(true)
                    .exceptionMessage("No se pudo obtener el documento del proveedor, " +
                            "pero es necesario cerrarlo antes de generar uno nuevo.").build());
        }
    }

    @Transactional
    public ResponseEntity<Object> addItem(CprAddItem addItem) {
        return addItemTransaction(addItem);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> addItemTransaction(CprAddItem addItem) {
        try {
            Optional<Documentos> doc = docRep.findById(addItem.getInvoice());
            if (doc.isEmpty()) throw new RuntimeException("«ERR-C02»");
            if (!doc.get().getDocStatus()) throw new RuntimeException("«ERR-C06»");
            if (doc.get().getDocCompleted()) throw new RuntimeException("«ERR-C07»");
            if (doc.get().getDocType() != 1) throw new RuntimeException("«ERR-C08»");
            Productos pro = proRep.findByProSku(addItem.getProSKU());
            if (pro == null) throw new RuntimeException("«ERR-C09»");
            if (!pro.getProStatus()) throw new RuntimeException("«ERR-C10»");
            if (pro.getProType() == 0) throw new RuntimeException("«ERR-C14»");
            Inventario inv = invRep.finByInvPro(pro.getProId());
            if (inv == null) inv = createInv(pro.getProId());

            doc.get().setDocNet(doc.get().getDocNet().add(addItem.getMovNet()));
            doc.get().setDocDiscount(doc.get().getDocDiscount().add(addItem.getMovDisc()));
            doc.get().setDocSubtotal(doc.get().getDocSubtotal().add(addItem.getMovSubtotal()));
            doc.get().setDocTax(doc.get().getDocTax().add(addItem.getMovTax()));
            doc.get().setDocTotal(doc.get().getDocTotal().add(addItem.getMovTotal()));
            doc.get().setDocProductQty(doc.get().getDocProductQty() + addItem.getProQty());

            inv.setInvInc(inv.getInvInc() + addItem.getProQty());

            BigDecimal result = addItem.getMovPU().multiply((BigDecimal.valueOf(addItem.getProQty())));
            if (!result.equals(addItem.getMovNet())) throw new RuntimeException("«ERR-C15»");
            result = result.subtract(addItem.getDocDisc());
            if (!result.equals(addItem.getMovSubtotal())) throw new RuntimeException("«ERR-C15»");
            BigDecimal taxes = result.multiply(BigDecimal.valueOf(0.16)).round(new MathContext(2));
            if (!result.equals(addItem.getMovTax())) throw new RuntimeException("«ERR-C15»");
            result = result.add(taxes);
            if (!result.equals(addItem.getMovTotal())) throw new RuntimeException("«ERR-C15»");


            invRep.save(inv);

            movRep.save(Movimientos.builder()
                    .movDocId(doc.get().getDocId())
                    .movProdId(pro.getProId())
                    .movType(1)
                    .movQty(addItem.getProQty())
                    .movCompleted(false)
                    .movPU(addItem.getMovPU())
                    .movNet(addItem.getMovNet())
                    .movDiscount(addItem.getMovDisc())
                    .movSubtotal(addItem.getMovSubtotal())
                    .movTax(addItem.getMovTax())
                    .movTotal(addItem.getMovTotal())
                    .build());

            docRep.save(doc.get());

            return ResponseEntity.ok(CprAddItem.builder()
                    .proQty(inv.getInvInc())
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
                    .invPro(proId)
                    .build());
        } catch (Exception e){
            throw new HibernateException("«ERR-C13»");
        }
    }
}
