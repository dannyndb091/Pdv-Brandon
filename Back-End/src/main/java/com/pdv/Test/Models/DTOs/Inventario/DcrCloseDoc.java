package com.pdv.Test.Models.DTOs.Inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DcrCloseDoc {
    Integer invoice;
    BigDecimal docTotal;
}
