package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products", uniqueConstraints = {@UniqueConstraint(name = "const_pro_Sku", columnNames = {"proSku"})})
public class Productos {
    @Id
    @GeneratedValue
    Integer proId;
    @Column(nullable = false, length = 64)
    String proSku;
    @Column(nullable = false, length = 50)
    String proName;
    @Column(nullable = false)
    Integer proType; // 0 = Servicio no inventariable, 1 = Producto Inventariable
    @Column(nullable = false, length = 3)
    String proUnit;
    @Column(nullable = false)
    Boolean proStatus;
    @Column(nullable = false)
    BigDecimal proPrice;
    @Column(nullable = false)
    BigDecimal proFinalPrice;
    @Column(nullable = false)
    BigDecimal proMaxPercDisc;
}
