package com.pdv.Test.Models.DTOs.Ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDocs {
    Integer invoice;
    LocalDate date;
    String code;
    String client;
    Integer qty;
    BigDecimal net;
    BigDecimal disc;
    BigDecimal subtotal;
    BigDecimal tax;
    BigDecimal total;
    Integer status;
    String statusText;
}
