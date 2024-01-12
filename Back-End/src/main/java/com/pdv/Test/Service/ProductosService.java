package com.pdv.Test.Service;

import com.pdv.Test.Models.DTOs.Productos.UpdateProduct;
import com.pdv.Test.Models.Inventario;
import com.pdv.Test.Models.Productos;
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

@Service
@RequiredArgsConstructor
public class ProductosService {
    private final ProductosRepository proRep;
    private final InventarioRepository invRep;
    private final MovimientosRepository movRep;
    private final Verifiers verifiers;

    @Transactional
    public ResponseEntity<Object> newProduct(Productos pro) {
        return newProductTransaction(pro);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> newProductTransaction(Productos pro) {
        try {
            verifyProductData(pro);

            Productos product = proRep.findByProSku(pro.getProSku());
            if (product != null) throw new RuntimeException("«ERR-P01»");

            product = proRep.save(pro);

            if (product.getProType() == 1){
                invRep.save(Inventario.builder()
                        .invPro(product.getProId())
                        .invBuys(0)
                        .invBuysValue(BigDecimal.valueOf(0))
                        .invSells(0)
                        .invSellsValue(BigDecimal.valueOf(0))
                        .invQty(0)
                        .invRes(0)
                        .invInc(0).build());
            }

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    private void verifyProductData(Productos pro) {
        verifiers.verifyProSku(pro.getProSku());
        verifiers.verifyProName(pro.getProName());
        verifiers.verifyProType(pro.getProType());
        verifiers.verifyProUnit(pro.getProUnit());
        verifiers.verifyProPrice(pro.getProPrice());
        verifiers.verifyProFinalPrice(pro.getProPrice(),pro.getProFinalPrice());
        verifiers.verifyProMaxPercDisc(pro.getProMaxPercDisc());
        if (pro.getProStatus() == null) throw new RuntimeException("«ERR-P09»");
    }

    @Transactional
    public ResponseEntity<Object> updateProduct(UpdateProduct pro) {
        return updateProductTransaction(pro);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> updateProductTransaction(UpdateProduct pro) {
        try {
            Productos oldPro = proRep.findByProSku(pro.getOldSku());
            if (oldPro == null) throw new RuntimeException("«ERR-P11»");

            Productos newPro = Productos.builder()
                    .proSku(pro.getProSku())
                    .proName(pro.getProName())
                    .proType(oldPro.getProType())
                    .proUnit(pro.getProUnit())
                    .proStatus(pro.getProStatus())
                    .proPrice(pro.getProPrice())
                    .proFinalPrice(pro.getProFinalPrice())
                    .proMaxPercDisc(pro.getProMaxPercDisc()).build();

            verifyProductData(newPro);

            Productos product = proRep.findByProSku(pro.getProSku());
            if (product != null && !(product.getProId().equals(oldPro.getProId())))
                throw new RuntimeException("«ERR-P01»");

            newPro.setProId(oldPro.getProId());

            proRep.save(newPro);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteProduct(Productos pro) {
        return deleteProductoTransaction(pro);
    }

    @Transactional(rollbackFor = {HibernateException.class, RuntimeException.class})
    private ResponseEntity<Object> deleteProductoTransaction(Productos pro) {
        try {
            Productos product = proRep.findByProSku(pro.getProSku());
            if (product == null) throw new RuntimeException("«ERR-P12»");

            Integer movs = movRep.countByMovProdId(product.getProId());
            if (movs > 0) throw new RuntimeException("«ERR-P13»");

            if (product.getProType() == 1) {
                Inventario inv = invRep.findByInvPro(product.getProId());

                if (inv != null) {
                    if (inv.getInvQty() != 0 || inv.getInvInc() != 0
                            || inv.getInvRes() != 0) throw new RuntimeException("«ERR-P14»");

                    invRep.delete(inv);
                }
            }

            proRep.delete(product);

            return ResponseEntity.ok("");
        } catch (Exception e){
            throw new HibernateException(e);
        }
    }
}
