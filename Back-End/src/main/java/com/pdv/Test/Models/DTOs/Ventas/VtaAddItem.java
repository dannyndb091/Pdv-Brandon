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
public class VtaAddItem {
    Integer invoice;
    String clCode;
    String proSKU;
    Integer proQty;
    Integer movLine;
    BigDecimal movPU;
    BigDecimal movNet;
    BigDecimal movDisc;
    BigDecimal movSubtotal;
    BigDecimal movTax;
    BigDecimal movTotal;
}
