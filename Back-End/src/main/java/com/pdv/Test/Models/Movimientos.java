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
@Table(name="movimientos")
public class Movimientos {
    @Id
    @GeneratedValue
    Integer movId;
    Integer movDocId;
    Integer movProdId;
    Integer movType; //1 = Compra, 2 = Venta
    Integer movQty;
    @Column(nullable = false)
    Boolean movCompleted;
    BigDecimal movPU;
    BigDecimal movNet;
    BigDecimal movDiscount;
    BigDecimal movSubtotal;
    BigDecimal movTax;
    BigDecimal movTotal;
}
