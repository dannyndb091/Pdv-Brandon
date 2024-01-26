package com.pdv.Test.Models.DTOs.Ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class getMov {
    Integer movType;
    Integer movLine;
    String proCode;
    String proName;
    Integer proQty;
    BigDecimal movPu;
    BigDecimal movNet;
    BigDecimal movDiscount;
    BigDecimal movDiscountPerc;
    BigDecimal movSubtotal;
    BigDecimal movTax;
    BigDecimal movTotal;
    Boolean movCompleted;
}
