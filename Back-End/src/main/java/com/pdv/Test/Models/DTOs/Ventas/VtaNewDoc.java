package com.pdv.Test.Models.DTOs.Ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VtaNewDoc {
    Integer Invoice;
    String clCode;
}
