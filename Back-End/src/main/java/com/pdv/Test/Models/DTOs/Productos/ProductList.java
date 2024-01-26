package com.pdv.Test.Models.DTOs.Productos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    String proCode;
    String proName;
    String proUnit;
    BigDecimal proPrice;
    BigDecimal proFinalPrice;
    BigDecimal proMaxPercDisc;
    Integer invQty;
    Integer invRes;
    Integer invInc;
}
