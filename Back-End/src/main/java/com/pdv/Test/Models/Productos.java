package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(nullable = false)
    String proSku;
    @Column(nullable = false)
    String proName;
    @Column(nullable = false)
    Integer proType; // 0 = Servicio no inventariable, 1 = Producto Inventariable
    @Column(nullable = false)
    String proUnit;
    @Column(nullable = false)
    Boolean proStatus;
}
