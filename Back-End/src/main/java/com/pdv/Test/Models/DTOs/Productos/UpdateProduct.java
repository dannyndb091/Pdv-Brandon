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
public class UpdateProduct {
    String oldSku;
    String proSku;
    String proName;
    Integer proType; // 0 = Servicio no inventariable, 1 = Producto Inventariable
    String proUnit;
    Boolean proStatus;
    BigDecimal proPrice;
    BigDecimal proFinalPrice;
    BigDecimal proMaxPercDisc;
}
