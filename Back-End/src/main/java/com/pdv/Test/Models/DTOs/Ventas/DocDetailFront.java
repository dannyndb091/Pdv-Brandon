package com.pdv.Test.Models.DTOs.Ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocDetailFront {
    Integer invoice;
    LocalDate docDate;
    String clCode;
    String clName;
    List<DocMovFront> movs;
    Integer docProductQty;
    BigDecimal docNet;
    BigDecimal docDiscount;
    BigDecimal docSubtotal;
    BigDecimal docTax;
    BigDecimal docTotal;
    Boolean docStatus;
    Boolean docCompleted;
    Integer docType;
}
