package com.pdv.Test.Models.DTOs.Compras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CprAddItem {
    Integer invoice;
    String clCode;
    String proSKU;
    Integer proQty;
    BigDecimal movPU;
    BigDecimal movNet;
    BigDecimal movDisc;
    BigDecimal movSubtotal;
    BigDecimal movTax;
    BigDecimal movTotal;
    BigDecimal docNet;
    BigDecimal docDisc;
    BigDecimal docSubtotal;
    BigDecimal docTax;
    BigDecimal docTotal;
}
