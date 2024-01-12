package com.pdv.Test.Models.DTOs.Cortes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CortesApertura {
    BigDecimal arcApertureCash;
}
