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
public class CprCloseBuy {
    Integer invoice;
    BigDecimal docNet;
    BigDecimal docDisc;
    BigDecimal docSubtotal;
    BigDecimal docTax;
    BigDecimal docTotal;
    BigDecimal payCash;
    BigDecimal payDigital;
}
